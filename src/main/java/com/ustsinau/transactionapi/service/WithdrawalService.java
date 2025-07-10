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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class WithdrawalService {

    private final WithdrawRepository withdrawRepository;
    private final WalletRepository walletRepository;

    private final PaymentService paymentService;
    private final TransactionService transactionService;

    private final TransactionalMapper transactionalMapper;

    @Transactional
    public TransactionResponse createWithdrawalPayment(WithdrawalRequestDto request) {

        WalletEntity wallet = walletRepository.findById(UUID.fromString(request.getWalletUid()))
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
                        .createdAt(LocalDateTime.now())
                        .paymentRequest(paymentEntity)
                        .type(TypeTransaction.valueOf(request.getType()))
                        .state(TransactionState.valueOf(request.getState()))
                        .amount(request.getAmount())
                        .userUid(UUID.fromString(request.getUserUid()))
                        .walletName(wallet.getName())
                        .wallet(wallet)
                        .build());

        try (HintManager hintManager = HintManager.getInstance()) {
//            HintManager.clear();
            hintManager.addDatabaseShardingValue("withdrawal_requests", request.getUserUid());

            withdrawRepository.save(WithdrawalEntity
                    .builder()
                    .createdAt(LocalDateTime.now())
                    .paymentRequest(paymentEntity)
                    .build());

            // Снимаем сумму с баланса кошелька
            log.info("Current balance: " + currentBalance);
            BigDecimal newBalance = currentBalance.subtract(request.getAmount());
            wallet.setBalance(newBalance);
            walletRepository.updateBalance(wallet.getUid(),
                    wallet.getBalance(),
                    wallet.getUserUid());
//            walletRepository.save(wallet);
            log.info("newBalance balance: " + newBalance);
            return TransactionResponse.toResponse(transactionalMapper.map(transactionalEntity));


        } catch (Exception e) {

            log.error("Withdrawal failed for request: {}", request, e);
            // Пробрасываем оригинальное исключение
            throw e;
        }

    }

}
