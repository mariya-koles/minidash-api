package com.platform.controller;

import com.platform.dto.CryptoDto;
import com.platform.service.CryptoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CryptoController.class)
class CryptoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CryptoService cryptoService;

    @Test
    void getTopCoins_shouldReturnTopFive() throws Exception {
        List<CryptoDto> mockResponse = List.of(
                createCrypto("btc"),
                createCrypto("eth"),
                createCrypto("bnb"),
                createCrypto("sol"),
                createCrypto("xrp")
        );

        when(cryptoService.getTopCoins()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/crypto/top"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].symbol").value("btc"))
                .andExpect(jsonPath("$[1].symbol").value("eth"))
                .andExpect(jsonPath("$[2].symbol").value("bnb"))
                .andExpect(jsonPath("$[3].symbol").value("sol"))
                .andExpect(jsonPath("$[4].symbol").value("xrp"));
    }

    // Helper method
    private CryptoDto createCrypto(String symbol) {
        CryptoDto dto = new CryptoDto();
        dto.setSymbol(symbol);
        return dto;
    }
}
