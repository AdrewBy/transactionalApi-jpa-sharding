package com.ustsinau.transactionapi.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    private String uid;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private String userUid;

    private WalletDto wallet;

    private BigDecimal amount;

    private String status;

    private String comment;

    private Long paymentMethodId;
}
