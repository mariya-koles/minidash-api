package com.platform.service;

import com.platform.config.ExternalApiProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CryptoServiceTest {

    @Mock private WebClient.Builder webClientBuilder;
    @Mock private WebClient webClient;
    @Mock private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock private WebClient.ResponseSpec responseSpec;

    private CryptoService cryptoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ExternalApiProperties.Api cryptoApi = new ExternalApiProperties.Api();
        cryptoApi.setBaseUrl("https://fake-crypto-api.com");

        ExternalApiProperties props = new ExternalApiProperties();
        props.setCrypto(cryptoApi);

        when(webClientBuilder.baseUrl(any())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        cryptoService = new CryptoService(props, webClientBuilder);
    }

    @Test
    void getTopCoins_shouldReturnCryptoList() {
        List<Map<String, Object>> mockResponse = List.of(
                Map.of("id", "bitcoin", "symbol", "btc", "price", 68000),
                Map.of("id", "ethereum", "symbol", "eth", "price", 3500)
        );

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(mockResponse));

        Object result = cryptoService.getTopCoins();
        assertEquals(mockResponse, result);
    }
}
