package com.ustsinau.transactionapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class WalletCreateRequestDto {


    private String name;

    private UUID userUid;

    private String walletTypeName;

    private String currencyCode;

}
