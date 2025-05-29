package com.platform.controller;

import com.platform.service.CryptoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

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
        List<Map<String, Object>> mockResponse = List.of(
                Map.of("symbol", "btc"),
                Map.of("symbol", "eth"),
                Map.of("symbol", "bnb"),
                Map.of("symbol", "sol"),
                Map.of("symbol", "xrp")
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
}
