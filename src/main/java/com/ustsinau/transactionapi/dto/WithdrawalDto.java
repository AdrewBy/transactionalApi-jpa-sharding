package com.ustsinau.transactionapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawalDto {

    private String uid;

    private LocalDateTime createdAt;

    private PaymentDto paymentRequest;
}
