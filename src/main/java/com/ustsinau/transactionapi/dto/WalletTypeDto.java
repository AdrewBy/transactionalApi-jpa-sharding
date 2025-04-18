package com.ustsinau.transactionapi.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class WalletTypeDto {

    private String uid;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private String name;

    private String currencyCode;

    private String status;

    private LocalDateTime archivedAt;

    private String userType;

    private String creator;

    private String modifier;

}
