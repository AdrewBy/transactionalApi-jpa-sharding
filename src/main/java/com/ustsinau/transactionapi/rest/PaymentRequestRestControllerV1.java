package com.ustsinau.transactionapi.rest;


import com.ustsinau.transactionapi.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments-requests")
public class PaymentRequestRestControllerV1 {

   // private final PaymentService paymentService;


//    @PostMapping
//    public ResponseEntity<TransactionResponse> createPaymentRequest(@RequestBody PaymentRequestDto request) {
//        TransactionResponse response = paymentService.createPaymentRequest(request.getUserUid(),
//                request.getWalletUid(), request.getAmount(), request.getComment(), request.getPaymentMethodId());
//        return ResponseEntity.ok(response);
//    }


}
