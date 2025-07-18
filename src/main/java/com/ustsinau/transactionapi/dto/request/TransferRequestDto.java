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

    private String userUidFrom;
    private String walletUidFrom;
    private String userUidTo;
    private String walletUidTo;
    private BigDecimal amount;
    private String comment;
    private String type;

    private Long PaymentMethodId;

    private String systemRate;


}
