package com.platform.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CryptoService {

    private final WebClient webClient = WebClient.create("https://api.coingecko.com/api/v3");

    public Object getTopCoins() {
        return webClient.get()
                .uri("/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=5&page=1&sparkline=false")
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }
}

