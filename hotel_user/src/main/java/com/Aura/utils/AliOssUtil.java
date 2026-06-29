package com.Aura.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Component
@Slf4j
public class AliOssUtil {

    @Value("${sky.alioss.endpoint}")
    private String endpoint;

    @Value("${sky.alioss.access-key-id}")
    private String accessKeyId;

    @Value("${sky.alioss.access-key-secret}")
    private String accessKeySecret;

    @Value("${sky.alioss.bucket-name}")
    private String bucketName;

    public String upload(byte[] bytes, String objectName) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));
        } catch (OSSException | ClientException e) {
            log.error("OSS上传失败", e);
            throw new RuntimeException("上传失败");
        } finally {
            if (ossClient != null) ossClient.shutdown();
        }
        String url = "https://" + bucketName + "." + endpoint + "/" + objectName;
        log.info("上传成功:{}", url);
        return url;
    }
}
