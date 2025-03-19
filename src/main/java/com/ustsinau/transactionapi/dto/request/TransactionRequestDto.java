package com.ustsinau.transactionapi.dto.request;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransactionRequestDto {

    private String userUid;

    private String walletUid;

    private String walletName;

    private BigDecimal amount;

    private String type;

    private String state;

    private String comment;

    private Long paymentMethodId;

}
