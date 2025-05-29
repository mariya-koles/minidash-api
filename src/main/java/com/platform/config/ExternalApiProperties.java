package com.platform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "external")
@Data
public class ExternalApiProperties {
    private Api weather;
    private Api news;
    private Api crypto;

    @Data
    public static class Api {
        private String baseUrl;
        private String additionalUrl;
        private String apiKey;
    }
}

