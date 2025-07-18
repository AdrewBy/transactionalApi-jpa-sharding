package com.ustsinau.transactionapi.dto.request;


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
public class TopUpRequestDto {

    private String userUid;
    private String walletUid;
    private BigDecimal amount;
    private String type;
    private String comment;
    private Long paymentMethodId;

    private String provider;


}
