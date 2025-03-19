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
public class WalletDto {

    private String uid;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private String name;

    private WalletTypeDto walletType;

    private String userUid;

    private String status;

    private BigDecimal balance;

    private LocalDateTime archivedAt;


}
