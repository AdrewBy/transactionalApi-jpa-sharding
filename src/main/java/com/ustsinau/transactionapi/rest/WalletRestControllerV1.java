package com.ustsinau.transactionapi.rest;


import com.ustsinau.transactionapi.dto.WalletDto;
import com.ustsinau.transactionapi.dto.request.WalletCreateRequestDto;
import com.ustsinau.transactionapi.dto.response.WalletResponse;
import com.ustsinau.transactionapi.mappers.WalletMapper;
import com.ustsinau.transactionapi.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wallets")
public class WalletRestControllerV1 {

    private final WalletService walletService;
    private final WalletMapper walletMapper;

    @GetMapping("/user/{user_uid}")
    public ResponseEntity<List<WalletResponse>> getUserWallets(@PathVariable String user_uid) {

        List<WalletResponse> wallets = walletService.getUserWallets(user_uid);
        return ResponseEntity.ok(wallets);
    }

    @GetMapping("/user/{user_uid}/currency/{currency}")
    public ResponseEntity<WalletResponse> getWalletByUserIdAndCurrency(@PathVariable String user_uid, @PathVariable String currency) {

        WalletResponse wallet = walletService.getWalletByUserIdAndCurrency(user_uid, currency);
        return ResponseEntity.ok(wallet);
    }


    @PostMapping("/user/create-wallet")
    public WalletDto create(@RequestBody WalletCreateRequestDto request) {

        return walletMapper.map(walletService.createWallet(request));
    }


    @PostMapping
    public WalletDto updateIndividual(@RequestBody WalletDto request) {
        return walletService.updateWallet(request);
    }

    @DeleteMapping("/user/soft-delete/{wallet_uid}")
    public WalletDto deleteSoftWallet(@PathVariable String wallet_uid) {

        return walletService.deleteSoftWallet(wallet_uid);
    }
}
