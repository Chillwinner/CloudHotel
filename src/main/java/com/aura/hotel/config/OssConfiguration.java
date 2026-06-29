package com.aura.hotel.config;

import com.aura.hotel.properties.AliOssProperties;
import com.aura.hotel.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class OssConfiguration {
    @Bean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties){
        log.info("初始化文件上传对象");
        AliOssUtil aliOssUtil = new AliOssUtil(aliOssProperties.getEndpoint(),aliOssProperties.getAccessKeyId(),aliOssProperties.getAccessKeySecret(),aliOssProperties.getBucketName());
        return aliOssUtil;
    }
}
