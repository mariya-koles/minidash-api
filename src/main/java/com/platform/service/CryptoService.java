package com.platform.service;

import com.platform.config.ExternalApiProperties;
import com.platform.dto.CryptoDto;
import com.platform.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;


@Service
public class CryptoService {

    private static final Logger log = LoggerFactory.getLogger(CryptoService.class);
    private final ExternalApiProperties externalApiProperties;
    private final WebClient webClient;

    public CryptoService(ExternalApiProperties externalApiProperties, WebClient.Builder builder) {
        this.externalApiProperties = externalApiProperties;
        this.webClient = builder
                .baseUrl(externalApiProperties.getCrypto().getBaseUrl())
                .build();
    }


    @CachePut("crypto")
    public List<CryptoDto> updateTopCoinsCache() {
        log.info("Refreshing cache with new top coins from API");
        List<CryptoDto> coins = webClient.get()
                .uri(externalApiProperties.getCrypto().getBaseUrl())
                .retrieve()
                .bodyToFlux(CryptoDto.class)
                .collectList()
                .block();
        return coins != null ? coins : List.of();
    }

    @Scheduled(fixedRate = 60000)
    public void preloadTopCoins() {
        try {
            updateTopCoinsCache();  // directly updates the cache
        } catch (Exception e) {
            log.warn("Scheduled preload failed: {}", e.getMessage());
        }
    }



    @Cacheable("crypto")
    public List<CryptoDto> getTopCoins() {
        try {
            log.info("Fetching top coins...");
            List<CryptoDto> coins = webClient.get()
                    .uri(externalApiProperties.getCrypto().getBaseUrl())
                    .retrieve()
                    .bodyToFlux(CryptoDto.class)
                    .collectList()
                    .block();

            if (coins == null || coins.isEmpty()) {
                throw new ResourceNotFoundException("No coins found.");
            }

            return coins;

        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 429) {
                log.warn("Rate limit exceeded: {}", e.getMessage());
                return List.of();  // safe fallback
            } else {
                log.error("Error fetching crypto data", e);
                throw new RuntimeException("Failed to fetch crypto data", e);
            }
        }
    }
}

