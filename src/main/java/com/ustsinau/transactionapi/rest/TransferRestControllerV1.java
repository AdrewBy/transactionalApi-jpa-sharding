package com.ustsinau.transactionapi.rest;



import com.ustsinau.transactionapi.dto.request.TransferRequestDto;
import com.ustsinau.transactionapi.dto.response.TransactionResponse;
import com.ustsinau.transactionapi.service.TransferService;
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
@RequestMapping("/api/v1/transfer")
public class TransferRestControllerV1 {

    private final TransferService transferService;


    @PostMapping
    public ResponseEntity<TransactionResponse> createTransferRequest(@RequestBody TransferRequestDto request) {
        TransactionResponse response = transferService.createTransferPayment(request);
        return ResponseEntity.ok(response);
    }
}
