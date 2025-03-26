package com.ustsinau.transactionapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class WalletTypeCreateRequestDto {

    private String name;

    private String creator;

    private String userType;

    private String currencyCode;
}
