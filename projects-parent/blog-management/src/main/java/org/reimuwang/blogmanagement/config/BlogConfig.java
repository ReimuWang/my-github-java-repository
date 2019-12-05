package org.reimuwang.blogmanagement.config;

import org.reimuwang.commonability.alibaba.oss.AlibabaOssHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlogConfig {

    @Bean
    @ConfigurationProperties("reimuwang.alibaba.oss")
    public AlibabaOssHandler alibabaOssHandler() {
        return new AlibabaOssHandler();
    }
}
