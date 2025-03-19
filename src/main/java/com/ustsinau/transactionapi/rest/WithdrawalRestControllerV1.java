package com.ustsinau.transactionapi.rest;


import com.ustsinau.transactionapi.dto.request.WithdrawalRequestDto;
import com.ustsinau.transactionapi.dto.response.TransactionResponse;
import com.ustsinau.transactionapi.service.WithdrawalService;
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
@RequestMapping("/api/v1/withdraw")
public class WithdrawalRestControllerV1 {


    private final WithdrawalService withdrawalService;


    @PostMapping
    public ResponseEntity<TransactionResponse> createPaymentRequest(@RequestBody WithdrawalRequestDto request) {
        TransactionResponse response = withdrawalService.createWithdrawalPayment(request);
        return ResponseEntity.ok(response);
    }
}
