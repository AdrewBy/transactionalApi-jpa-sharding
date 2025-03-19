package com.ustsinau.transactionapi.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionStatusResponse {

    private String uid;
    private String state;
    private LocalDateTime updatedAt;


}
