package com.ustsinau.transactionapi.dto.request;


import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequestDto {

    private String userUid;

    private String walletUid;

    private BigDecimal amount;

    private String status;

    private String comment;

    private Long paymentMethodId;

}
