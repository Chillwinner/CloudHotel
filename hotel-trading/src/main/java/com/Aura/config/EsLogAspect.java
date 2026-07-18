package com.Aura.config;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Aspect
@Component
public class EsLogAspect {

    @Value("${es.url:http://localhost:9200}")
    private String esUrl;

    @Value("${es.index:app-log}")
    private String esIndex;

    private final RestTemplate restTemplate = new RestTemplate();
    private final LinkedBlockingQueue<Map<String, Object>> queue = new LinkedBlockingQueue<>(500);
    private volatile boolean running = true;

    public EsLogAspect() {
        Thread t = new Thread(this::flushLoop, "es-log-flusher");
        t.setDaemon(true);
        t.start();
    }

    @PreDestroy
    public void shutdown() {
        running = false;
        flush();
    }

    @Pointcut("execution(* com.Aura.Controller..*.*(..))")
    public void controllerLayer() {}

    @Around("controllerLayer()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        String method = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        Object result = null;
        String status = "SUCCESS";
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            status = "ERROR";
            throw e;
        } finally {
            long cost = System.currentTimeMillis() - start;
            log.debug("[请求日志] 方法: {} | 状态: {} | 耗时: {}ms", method, status, cost);
            if ("ERROR".equals(status)) {
                Map<String, Object> doc = new HashMap<>();
                doc.put("method", method);
                doc.put("params", argsToString(args));
                doc.put("status", status);
                doc.put("cost", cost);
                doc.put("time", Instant.now().toString());
                if (!queue.offer(doc)) {
                    log.warn("ES日志队列已满，丢弃日志: {}", method);
                }
            }
        }
    }

    private void flushLoop() {
        while (running) {
            try {
                Thread.sleep(5000);
                flush();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        flush();
    }

    private void flush() {
        List<Map<String, Object>> batch = new ArrayList<>();
        queue.drainTo(batch, 100);
        if (batch.isEmpty()) return;

        StringBuilder sb = new StringBuilder();
        for (Map<String, Object> doc : batch) {
            sb.append("{\"index\":{\"_index\":\"").append(esIndex).append("\"}}\n");
            sb.append("{\"method\":\"").append(doc.get("method")).append("\"");
            sb.append(",\"params\":\"").append(escapeJson(String.valueOf(doc.get("params")))).append("\"");
            sb.append(",\"status\":\"").append(doc.get("status")).append("\"");
            sb.append(",\"cost\":").append(doc.get("cost"));
            sb.append(",\"time\":\"").append(doc.get("time")).append("\"}\n");
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(sb.toString(), headers);
            restTemplate.postForEntity(esUrl + "/_bulk", entity, String.class);
            log.debug("ES批量写入{}条日志", batch.size());
        } catch (Exception e) {
            log.warn("ES批量写入失败: {}", e.getMessage());
            for (Map<String, Object> doc : batch) {
                queue.offer(doc);
            }
        }
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String argsToString(Object[] args) {
        if (args == null || args.length == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < args.length; i++) {
            if (i > 0) sb.append(", ");
            String s = String.valueOf(args[i]);
            sb.append(s.length() > 100 ? s.substring(0, 100) + "..." : s);
        }
        return sb.append("]").toString();
    }
}
