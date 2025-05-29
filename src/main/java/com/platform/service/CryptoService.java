package com.platform.service;

import com.platform.config.ExternalApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
public class CryptoService {

    private final ExternalApiProperties externalApiProperties;
    private final WebClient webClient;

    public CryptoService(ExternalApiProperties externalApiProperties, WebClient.Builder builder) {
        this.externalApiProperties = externalApiProperties;
        this.webClient = builder
                .baseUrl(externalApiProperties.getCrypto().getBaseUrl())
                .build();
    }

    public Object getTopCoins() {
        return webClient.get()
                .uri(externalApiProperties.getCrypto().getBaseUrl())
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }
}

