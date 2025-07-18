package com.ustsinau.transactionapi.service;


import com.ustsinau.transactionapi.dto.WalletTypeDto;
import com.ustsinau.transactionapi.dto.request.WalletTypeCreateRequestDto;
import com.ustsinau.transactionapi.entity.WalletTypeEntity;
import com.ustsinau.transactionapi.enums.Status;
import com.ustsinau.transactionapi.exception.WalletTypeException;
import com.ustsinau.transactionapi.repository.WalletTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class WalletTypeService {

    private final WalletTypeRepository walletTypeRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public WalletTypeEntity createWalletType(WalletTypeCreateRequestDto request) {

        if (walletTypeRepository.existsByName(request.getName()) && walletTypeRepository.existsByCurrencyCode(request.getCurrencyCode())) {
            throw new WalletTypeException("WalletType already exists with this name and currency code.", "DUPLICATE_WALLETTYPE");
        }

        WalletTypeEntity walletType = WalletTypeEntity.builder()
                .name(request.getName())
                .currencyCode(request.getCurrencyCode())
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .creator(request.getCreator())
                .userType(request.getUserType())
                .build();

        return walletTypeRepository.save(walletType);
    }

    public WalletTypeEntity getWalletTypeById(String uid) {

        return walletTypeRepository.findById(UUID.fromString(uid))
                .map(walletType -> {
                    log.info("Found wallet type: {}", walletType);
                    return walletType;
                })
                .orElseThrow(() -> new WalletTypeException("WalletType not found with ID: " + uid, "WALLET_NOT_FOUND"));
    }

    public WalletTypeEntity getWalletTypeByName(String name) {
        WalletTypeEntity walletType = walletTypeRepository.findByName(name);
        log.info("Found wallet type: {}", walletType);
        return walletType;
    }

    @Transactional
    public WalletTypeEntity updateWalletType(WalletTypeDto request) {

        WalletTypeEntity oldWalletType = walletTypeRepository.findById(UUID.fromString(request.getUid()))
                .orElseThrow(() -> new WalletTypeException("WalletType not found with ID: " + request.getUid(), "WALLET_NOT_FOUND"));

        WalletTypeEntity originalWalletTypeCopy = new WalletTypeEntity();
        BeanUtils.copyProperties(oldWalletType, originalWalletTypeCopy);

        if (request.getName() != null) {
            oldWalletType.setName(request.getName());
        }
        if (request.getStatus() != null) {
            oldWalletType.setStatus(Status.valueOf(request.getStatus()));
        }
        if (request.getCurrencyCode() != null) {
            oldWalletType.setName(request.getCurrencyCode());
        }

        oldWalletType.setModifiedAt(LocalDateTime.now());
        oldWalletType.setModifier(request.getModifier());

        return walletTypeRepository.save(oldWalletType);
    }

    @Transactional
    public WalletTypeEntity deleteSoftWalletType(String walletTypeUid) {

        WalletTypeEntity oldWalletType = walletTypeRepository.findById(UUID.fromString(walletTypeUid))
                .orElseThrow(() -> new WalletTypeException("WalletType not found with ID: " + walletTypeUid, "WALLET_NOT_FOUND"));

        oldWalletType.setStatus(Status.ARCHIVED);

        return walletTypeRepository.save(oldWalletType);
    }
}
