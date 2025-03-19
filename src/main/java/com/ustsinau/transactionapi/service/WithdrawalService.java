package com.ustsinau.transactionapi.service;


import com.ustsinau.transactionapi.dto.request.WithdrawalRequestDto;
import com.ustsinau.transactionapi.dto.response.TransactionResponse;
import com.ustsinau.transactionapi.entity.PaymentEntity;
import com.ustsinau.transactionapi.entity.TransactionalEntity;
import com.ustsinau.transactionapi.entity.WalletEntity;
import com.ustsinau.transactionapi.entity.WithdrawalEntity;
import com.ustsinau.transactionapi.enums.TransactionState;
import com.ustsinau.transactionapi.enums.TypeTransaction;
import com.ustsinau.transactionapi.exception.InsufficientFundsException;
import com.ustsinau.transactionapi.exception.WalletNotFoundException;
import com.ustsinau.transactionapi.mappers.TransactionalMapper;
import com.ustsinau.transactionapi.repository.WalletRepository;
import com.ustsinau.transactionapi.repository.WithdrawRepository;
import com.ustsinau.transactionapi.utils.UuidFromString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Slf4j
@Service
@RequiredArgsConstructor
public class WithdrawalService {

    private final WithdrawRepository withdrawRepository;
    private final WalletRepository walletRepository;

    private final PaymentService paymentService;
    private final TransactionService transactionService;

    private final UuidFromString fromString;
    private final TransactionalMapper transactionalMapper;

    @Transactional
    public TransactionResponse createWithdrawalPayment(WithdrawalRequestDto request) {

        WalletEntity wallet = walletRepository.findById(fromString.getUuidFromString(request.getWalletUid()))
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found with UID: " + request.getWalletUid(), "WALLET_NOT_FOUND"));

        // Проверяем, достаточно ли средств на кошельке
        BigDecimal currentBalance = wallet.getBalance();
        if (currentBalance.compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds in the wallet", "INSUFFICIENT_FUNDS");
        }

        PaymentEntity paymentEntity = paymentService.createPaymentRequest(
                request.getUserUid(),
                request.getWalletUid(),
                request.getAmount(),
                request.getComment(),
                request.getPaymentMethodId());

        TransactionalEntity transactionalEntity = transactionService
                .createTransaction(TransactionalEntity
                        .builder()
                        .paymentRequest(paymentEntity)
                        .type(TypeTransaction.valueOf(request.getType()))
                        .state(TransactionState.valueOf(request.getState()))
                        .amount(request.getAmount())
                        .userUid(fromString.getUuidFromString(request.getUserUid()))
                        .walletName(wallet.getName())
                        .wallet(wallet)
                        .build());

        withdrawRepository.save(WithdrawalEntity
                .builder()
                .createdAt(LocalDateTime.now())
                .paymentRequest(paymentEntity)
                .build());

        // Снимаем сумму с баланса кошелька
        BigDecimal newBalance = currentBalance.subtract(request.getAmount());
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);

        return TransactionResponse.toResponse(transactionalMapper.map(transactionalEntity));
    }
}
