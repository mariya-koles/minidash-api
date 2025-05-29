package com.platform.service;

import com.platform.config.ExternalApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Service
public class CryptoService {

    private final ExternalApiProperties externalApiProperties;
    private final WebClient webClient = WebClient.create("https://api.coingecko.com/api/v3");

    public Object getTopCoins() {
        return webClient.get()
                .uri(externalApiProperties.getCrypto().getBaseUrl())
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }
}

