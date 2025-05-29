package com.platform.service;

import com.platform.config.ExternalApiProperties;
import com.platform.dto.CryptoDto;
import com.platform.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;


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

    public List<CryptoDto> getTopCoins() {
        List<CryptoDto> coins = webClient.get()
                .uri(externalApiProperties.getCrypto().getBaseUrl())
                .retrieve()
                .bodyToFlux(CryptoDto.class)
                .collectList()
                .block();
        if (coins == null ||coins.isEmpty()) {
            throw new ResourceNotFoundException("No coins found.");
        }
        return coins;
    }
}

