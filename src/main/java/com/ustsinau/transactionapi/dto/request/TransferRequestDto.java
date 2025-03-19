package com.ustsinau.transactionapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequestDto {

    private String userUid;

    private String walletUidFrom;

    private String walletUidTo;

    private BigDecimal amount;

    private String type;

    private String state;

    private String comment;

    private Long PaymentMethodId;




    private String systemRate;

    private String paymentRequestTo;

    private String paymentRequestFrom;


}
