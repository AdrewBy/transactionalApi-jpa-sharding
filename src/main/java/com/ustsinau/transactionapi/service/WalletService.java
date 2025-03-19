package com.ustsinau.transactionapi.service;


import com.ustsinau.transactionapi.dto.WalletDto;
import com.ustsinau.transactionapi.dto.request.WalletCreateRequestDto;
import com.ustsinau.transactionapi.dto.response.WalletResponse;
import com.ustsinau.transactionapi.enums.Status;
import com.ustsinau.transactionapi.entity.WalletEntity;
import com.ustsinau.transactionapi.entity.WalletTypeEntity;
import com.ustsinau.transactionapi.exception.WalletNotFoundException;
import com.ustsinau.transactionapi.exception.UuidInvalidException;
import com.ustsinau.transactionapi.mappers.WalletMapper;
import com.ustsinau.transactionapi.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;
    private final WalletTypeService walletTypeService;

    public List<WalletResponse> getUserWallets(String uid) {

        UUID uuid;
        try {
            uuid = UUID.fromString(uid);
            log.info("UUID = " + uuid);
        } catch (IllegalArgumentException e) {
            throw new UuidInvalidException("Invalid UUID format: " + uid, "INVALID_UUID_FORMAT");
        }

        return walletRepository.findByUserUid(uuid)
                .stream()
                .map(walletMapper::map)
                .map(this::toResponse)
                .toList();
    }

    public WalletResponse getWalletByUserIdAndCurrency(String userUid, String currency) {

        UUID uuid;
        try {
            uuid = UUID.fromString(userUid);
            log.info("UUID = " + uuid);
        } catch (IllegalArgumentException e) {
            throw new UuidInvalidException("Invalid UUID format: " + userUid, "INVALID_UUID_FORMAT");
        }

        return toResponse(walletMapper.map(walletRepository.findByUserUidAndCurrency(uuid, currency)));

    }

    @Transactional
    public WalletEntity createWallet(WalletCreateRequestDto request) {

        WalletTypeEntity walletType = walletTypeService.createWalletType(request);

        return walletRepository.save(WalletEntity.builder()
                .name(request.getName())
                .createdAt(LocalDateTime.now())
                .walletType(walletType)
                .userUid(request.getUserUid())
                .status(Status.ACTIVE)
                .balance(BigDecimal.ZERO)
                .build());
    }

    @Transactional
    public WalletDto updateWallet(WalletDto request) {

        UUID uuid;
        try {
            uuid = UUID.fromString(request.getUid());
            log.info("UUID = " + uuid);
        } catch (IllegalArgumentException e) {
            throw new UuidInvalidException("Invalid UUID format: " + request.getUid(), "INVALID_UUID_FORMAT");
        }

        WalletEntity oldWallet = walletRepository.findById(uuid)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found with ID: " + uuid, "WALLET_NOT_FOUND"));

        WalletEntity originalWalletCopy = new WalletEntity();
        BeanUtils.copyProperties(oldWallet, originalWalletCopy);

        if (request.getName() != null) {
            oldWallet.setName(request.getName());
        }
        if (request.getStatus() != null) {
            oldWallet.setStatus(Status.valueOf(request.getStatus()));
        }

        oldWallet.setModifiedAt(LocalDateTime.now());

        return walletMapper.map(walletRepository.save(oldWallet));

    }

    @Transactional
    public WalletDto deleteSoftWallet(String id) {
        try {

            UUID uuid = UUID.fromString(id);

            WalletEntity oldWallet = walletRepository.findById(uuid)
                    .orElseThrow(() -> new WalletNotFoundException("Wallet not found with ID: " + id, "WALLET_NOT_FOUND"));

            WalletEntity originalWalletCopy = new WalletEntity();
            BeanUtils.copyProperties(oldWallet, originalWalletCopy);

            oldWallet.setStatus(Status.DELETED);

            return walletMapper.map(walletRepository.save(oldWallet));

        } catch (IllegalArgumentException e) {
            throw new UuidInvalidException("Invalid UUID format: " + id, "INVALID_UUID_FORMAT");
        }

    }

    private WalletResponse toResponse(WalletDto wallet) {
        WalletResponse response = new WalletResponse();
        response.setUid(wallet.getUid());
        response.setUid(wallet.getName());
        response.setWalletTypeUid(wallet.getWalletType().getUid());
        response.setUserUid(wallet.getUserUid());
        response.setStatus(wallet.getStatus().toString());
        response.setBalance(wallet.getBalance());
        response.setCurrencyCode(wallet.getWalletType().getCurrencyCode());
        response.setCreatedAt(wallet.getCreatedAt());
        return response;
    }


}
