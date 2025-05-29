package com.platform.service;

import com.platform.config.ExternalApiProperties;
import com.platform.dto.CryptoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;


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
        List<CryptoDto> mockResponse = List.of(
                createCrypto("bitcoin", "btc", 68000),
                createCrypto("ethereum", "eth", 3500)
        );

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(CryptoDto.class)).thenReturn(Flux.fromIterable(mockResponse));

        List<CryptoDto> result = cryptoService.getTopCoins();
        assertEquals(mockResponse, result);
    }

    // Helper
    private CryptoDto createCrypto(String id, String symbol, double price) {
        CryptoDto dto = new CryptoDto();
        dto.setId(id);
        dto.setSymbol(symbol);
        dto.setCurrentPrice(price);
        return dto;
    }
}
