package com.ustsinau.transactionapi.service;


import com.ustsinau.transactionapi.dto.request.WalletCreateRequestDto;
import com.ustsinau.transactionapi.enums.Status;
import com.ustsinau.transactionapi.entity.WalletTypeEntity;
import com.ustsinau.transactionapi.repository.WalletTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Slf4j
@Service
@RequiredArgsConstructor
public class WalletTypeService {

    private final WalletTypeRepository walletTypeRepository;


    public WalletTypeEntity createWalletType(WalletCreateRequestDto request) {

        WalletTypeEntity walletType = WalletTypeEntity.builder()
                .name(request.getWalletTypeName())
                .currencyCode(request.getCurrencyCode())
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        return walletTypeRepository.save(walletType);
    }
}
