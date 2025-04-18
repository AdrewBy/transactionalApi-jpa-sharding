package com.ustsinau.transactionapi.service;


import com.ustsinau.transactionapi.dto.WalletDto;
import com.ustsinau.transactionapi.dto.request.WalletCreateRequestDto;
import com.ustsinau.transactionapi.dto.response.WalletResponse;
import com.ustsinau.transactionapi.enums.Status;
import com.ustsinau.transactionapi.entity.WalletEntity;
import com.ustsinau.transactionapi.entity.WalletTypeEntity;
import com.ustsinau.transactionapi.exception.WalletNotFoundException;
import com.ustsinau.transactionapi.mappers.WalletMapper;
import com.ustsinau.transactionapi.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

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

    private final TransactionTemplate transactionTemplate;


    public List<WalletResponse> getUserWallets(String uid) {

        return walletRepository.findByUserUid(UUID.fromString(uid))
                .stream()
                .map(walletMapper::map)
                .map(this::toResponse)
                .toList();
    }

    public WalletResponse getWalletByUserIdAndCurrency(String userUid, String currency) {

        return toResponse(walletMapper.map(walletRepository.findByUserUidAndCurrency(UUID.fromString(userUid), currency)));
    }

    @Transactional
    public WalletEntity createWallet(WalletCreateRequestDto request) {
        WalletTypeEntity walletType = walletTypeService.getWalletTypeById(request.getWalletTypeUid());
        log.info("Creating wallet with request: {}", request);

        return walletRepository.save(WalletEntity.builder()
                .name(request.getName())
                .createdAt(LocalDateTime.now())
                .walletType(walletType)
                .userUid(request.getUserUid())
                .status(Status.ACTIVE)
                .balance(BigDecimal.ZERO)
                .build());
    }



public WalletDto updateWallet(WalletDto request) {

    WalletEntity oldWallet = walletRepository.findById(UUID.fromString(request.getUid()))
            .orElseThrow(() -> new WalletNotFoundException("Wallet not found with ID: " + request.getUid(), "WALLET_NOT_FOUND"));

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


public WalletDto deleteSoftWallet(String id) {

    WalletEntity oldWallet = walletRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new WalletNotFoundException("Wallet not found with ID: " + id, "WALLET_NOT_FOUND"));

    WalletEntity originalWalletCopy = new WalletEntity();
    BeanUtils.copyProperties(oldWallet, originalWalletCopy);

    oldWallet.setStatus(Status.DELETED);

    return walletMapper.map(walletRepository.save(oldWallet));

}

private WalletResponse toResponse(WalletDto wallet) {
    WalletResponse response = new WalletResponse();
    response.setUid(wallet.getUid());
    response.setName(wallet.getName());
    response.setWalletTypeUid(wallet.getWalletType().getUid());
    response.setUserUid(wallet.getUserUid());
    response.setStatus(wallet.getStatus());
    response.setBalance(wallet.getBalance());
    response.setCurrencyCode(wallet.getWalletType().getCurrencyCode());
    response.setCreatedAt(wallet.getCreatedAt());
    return response;
}


}
