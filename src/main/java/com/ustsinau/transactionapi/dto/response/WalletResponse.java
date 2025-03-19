package com.ustsinau.transactionapi.dto.response;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WalletResponse {

    private String uid;
    private String name;
    private String walletTypeUid;
    private String userUid;
    private String status;
    private BigDecimal balance;
    private String currencyCode;
    private LocalDateTime createdAt;

}
