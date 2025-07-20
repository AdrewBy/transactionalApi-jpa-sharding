package com.ustsinau.transactionapi.utils;

import com.ustsinau.transactionapi.dto.request.WalletCreateRequestDto;
import com.ustsinau.transactionapi.dto.request.WalletTypeCreateRequestDto;

import java.math.BigDecimal;
import java.util.UUID;

public class DataWalletUtils {

    public static WalletCreateRequestDto getWalletFirstCreateRequestDtoTransient(UUID walletTypeUid) {
        return WalletCreateRequestDto.builder()
                .name("First wallet")
                .userUid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .walletTypeUid(String.valueOf(walletTypeUid))
                .build();
    }

    public static WalletCreateRequestDto getWalletSecondCreateRequestDtoTransient(UUID walletTypeUid) {
        return WalletCreateRequestDto.builder()
                .name("Second wallet")
                .userUid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .walletTypeUid(String.valueOf(walletTypeUid))
                .build();
    }

    public static WalletCreateRequestDto getWalletFirstCreateRequestDtoSecondPersonTransient(UUID walletTypeUid) {
        return WalletCreateRequestDto.builder()
                .name("First wallet")
                .userUid(UUID.fromString("223e4567-e89b-12d3-a456-426614174002"))
                .walletTypeUid(String.valueOf(walletTypeUid))
                .build();
    }

    public static WalletCreateRequestDto getWalletDtoTransient(UUID walletTypeUid) {
        return WalletCreateRequestDto.builder()
                .name("First wallet")
                .userUid(UUID.fromString("223e4567-e89b-12d3-a456-426614174002"))
                .walletTypeUid(String.valueOf(walletTypeUid))
                .build();
    }

}
