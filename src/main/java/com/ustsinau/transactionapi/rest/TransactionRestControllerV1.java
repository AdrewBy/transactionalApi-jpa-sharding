package com.ustsinau.transactionapi.rest;


import com.ustsinau.transactionapi.dto.response.TransactionResponse;
import com.ustsinau.transactionapi.dto.response.TransactionStatusResponse;
import com.ustsinau.transactionapi.service.TransactionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionRestControllerV1 {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> searchTransactions(
            @RequestParam(required = false) UUID userUid,
            @RequestParam(required = false) UUID walletUid,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo
    ) {
        List<TransactionResponse> transactions = transactionService.searchTransactions(userUid, walletUid, type, state, dateFrom, dateTo);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{uid}/status")
    public ResponseEntity<TransactionStatusResponse> getTransactionStatus(@PathVariable String uid){

        TransactionStatusResponse statusResponse = transactionService.getTransactionStatus(uid);
     return ResponseEntity.ok(statusResponse);

    }

}
