package com.platform.controller;

import com.platform.dto.CryptoDto;
import com.platform.service.CryptoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/crypto")
public class CryptoController {

    private static Logger log = LoggerFactory.getLogger(CryptoController.class);
    private final CryptoService cryptoService;

    @GetMapping("/top")
    @Operation(
            summary = "Get top 5 cryptocurrencies by market cap",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved top coins"),
                    @ApiResponse(responseCode = "500", description = "Failed to retrieve data")
            }
    )
    public ResponseEntity<List<CryptoDto>> getTopCoins() {
        List<CryptoDto> coins = cryptoService.getTopCoins();
        return ResponseEntity.ok(coins);
    }

}
