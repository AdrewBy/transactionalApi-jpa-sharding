package com.ustsinau.transactionapi.rest;

import com.ustsinau.transactionapi.dto.request.TopUpRequestDto;
import com.ustsinau.transactionapi.dto.response.TransactionResponse;
import com.ustsinau.transactionapi.service.TopUpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/topup")
public class TopUpRestControllerV1 {

    private final TopUpService topUpService;


    @PostMapping
    public ResponseEntity<TransactionResponse> createPaymentRequest(@RequestBody TopUpRequestDto request) {
        TransactionResponse response = topUpService.createTopUpPayment(request);
        return ResponseEntity.ok(response);
    }
}
