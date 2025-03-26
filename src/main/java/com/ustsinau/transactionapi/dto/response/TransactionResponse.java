package com.ustsinau.transactionapi.dto.response;


import com.ustsinau.transactionapi.dto.TransactionalDto;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class TransactionResponse {
    private String uid;
    private String userUid;
    private String walletUid;
    private String type;
    private String state;
    private LocalDateTime createdAt;
    private BigDecimal amount;

    public static TransactionResponse toResponse(TransactionalDto transaction) {

        return TransactionResponse.builder()
                .uid(transaction.getUid())
                .userUid(transaction.getUserUid())
                .walletUid(transaction.getWallet().getUid())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .state(transaction.getState())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
