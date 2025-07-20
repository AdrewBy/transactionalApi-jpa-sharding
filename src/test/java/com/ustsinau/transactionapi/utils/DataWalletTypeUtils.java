package com.ustsinau.transactionapi.utils;

import com.ustsinau.transactionapi.dto.request.WalletTypeCreateRequestDto;

public class DataWalletTypeUtils {

    public static WalletTypeCreateRequestDto getWalletTypeFirstCreateRequestDtoTransient() {
        return WalletTypeCreateRequestDto.builder()
                .name("First")
                .currencyCode("RUB")
                .userType("INDIVIDUAL")
                .creator("admin@example.com")
                .build();

    }

    public static WalletTypeCreateRequestDto getWalletTypeSecondCreateRequestDtoTransient() {
        return WalletTypeCreateRequestDto.builder()
                .name("Second")
                .currencyCode("RUB")
                .userType("INDIVIDUAL")
                .creator("admin@example.com")
                .build();

    }
}
