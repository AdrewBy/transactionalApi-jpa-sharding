package com.ustsinau.transactionapi.dto;


import com.ustsinau.transactionapi.dto.request.PaymentRequestDto;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TransactionalDto {

    private String uid;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private String userUid;

    private WalletDto wallet;

    private String walletName;

    private BigDecimal amount;

    private String type;

    private String state;

    private PaymentDto paymentRequest;
}
