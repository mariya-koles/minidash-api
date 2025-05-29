package com.platform.controller;

import com.platform.service.CryptoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/crypto")
public class CryptoController {


    private final CryptoService cryptoService;

    @GetMapping("/top")
    @Operation(
            summary = "Get top 5 cryptocurrencies by market cap",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved top coins"),
                    @ApiResponse(responseCode = "500", description = "Failed to retrieve data")
            }
    )
    public ResponseEntity<?> getTopCoins() {
        return ResponseEntity.ok(cryptoService.getTopCoins());
    }

}
