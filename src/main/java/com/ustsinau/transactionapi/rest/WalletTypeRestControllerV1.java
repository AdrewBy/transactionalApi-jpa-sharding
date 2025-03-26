package com.ustsinau.transactionapi.rest;


import com.ustsinau.transactionapi.dto.WalletTypeDto;
import com.ustsinau.transactionapi.dto.request.WalletTypeCreateRequestDto;
import com.ustsinau.transactionapi.entity.WalletTypeEntity;
import com.ustsinau.transactionapi.mappers.WalletTypeMapper;
import com.ustsinau.transactionapi.service.WalletTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wallet-type")
public class WalletTypeRestControllerV1 {


    private final WalletTypeService walletTypeService;
    private final WalletTypeMapper walletTypeMapper;


    @GetMapping("/{uid}")
    public WalletTypeDto getWalletTypeByCurrencyCode(@PathVariable String uid) {
        WalletTypeEntity entity = walletTypeService.getWalletTypeById(uid);
        log.info("Mapping entity: {}", entity);

        WalletTypeDto dto = walletTypeMapper.map(entity);
        log.info("Mapped DTO: {}", dto);

        return dto;

    }

    @PostMapping("/create")
    public WalletTypeDto create(@RequestBody WalletTypeCreateRequestDto request) {

        return walletTypeMapper.map(walletTypeService.createWalletType(request));
    }

    @PostMapping
    public WalletTypeDto updateWallet(@RequestBody WalletTypeDto request) {

        return walletTypeMapper.map(walletTypeService.updateWalletType(request));
    }

    @DeleteMapping("/soft-delete/{walletType_uid}")
    public WalletTypeDto deleteSoftWallet(@PathVariable String walletType_uid) {

        return walletTypeMapper.map(walletTypeService.deleteSoftWalletType(walletType_uid));
    }
}
