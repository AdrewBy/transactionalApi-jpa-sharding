package com.ustsinau.transactionapi.service;


import com.ustsinau.transactionapi.dto.request.WithdrawalRequestDto;
import com.ustsinau.transactionapi.dto.response.TransactionResponse;
import com.ustsinau.transactionapi.entity.PaymentEntity;
import com.ustsinau.transactionapi.entity.TransactionalEntity;
import com.ustsinau.transactionapi.entity.WalletEntity;
import com.ustsinau.transactionapi.entity.WithdrawalEntity;
import com.ustsinau.transactionapi.exception.InsufficientFundsException;
import com.ustsinau.transactionapi.exception.TransferFailedException;
import com.ustsinau.transactionapi.mappers.TransactionalMapper;
import com.ustsinau.transactionapi.repository.WithdrawRepository;
import com.ustsinau.transactionapi.service.compensation.CompensationWithdrawService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Slf4j
@RestController
@RequiredArgsConstructor
public class WithdrawalService {

    private final WithdrawRepository withdrawRepository;
    private final WalletService walletService;
    private final PaymentService paymentService;
    private final TransactionService transactionService;
    private final TransactionalMapper transactionalMapper;
    private final CompensationWithdrawService compensateWithdrawal;


    @Transactional(rollbackFor = Exception.class)
    public TransactionResponse createWithdrawalPayment(WithdrawalRequestDto request) {

        WalletEntity wallet = walletService.getById(UUID.fromString(request.getWalletUid()));

        BigDecimal oldBalance = wallet.getBalance();

        if (oldBalance.compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds in the wallet", "INSUFFICIENT_FUNDS");
        }

        PaymentEntity paymentEntity = null;
        TransactionalEntity transaction = null;
        WithdrawalEntity withdrawal = null;

        try {
            paymentEntity = paymentService.createPaymentRequest(
                    request.getUserUid(),
                    request.getWalletUid(),
                    request.getAmount(),
                    request.getComment(),
                    request.getPaymentMethodId());

            transaction = transactionService.createTransaction(
                    paymentEntity,
                    wallet,
                    UUID.fromString(request.getUserUid()),
                    request.getAmount(),
                    request.getType());

            withdrawal = withdrawRepository.save(WithdrawalEntity
                    .builder()
                    .createdAt(LocalDateTime.now())
                    .paymentRequest(paymentEntity)
                    .build());

            // Снимаем сумму с баланса кошелька
            BigDecimal newBalance = oldBalance.subtract(request.getAmount());
            wallet.setBalance(newBalance);
            walletService.updateBalance(wallet.getUid(),
                    wallet.getBalance(),
                    wallet.getUserUid());

            return TransactionResponse.toResponse(transactionalMapper.map(transaction));

        } catch (Exception ex) {
            try {
                compensateWithdrawal.compensateWithdrawal(
                        withdrawal != null ? withdrawal.getUid() : null,
                        transaction != null ? transaction.getUid() : null,
                        paymentEntity != null ? paymentEntity.getUid() : null,
                        wallet.getUid(),
                        oldBalance,
                        wallet.getUserUid()
                );
            } catch (Exception compEx) {
                log.error("Compensation failed", compEx);
                // Добавляем информацию о проблеме компенсации в основное исключение
                ex.addSuppressed(compEx);
            }
            throw new TransferFailedException("Withdraw failed: " + ex.getMessage(), "WITHDRAW_FAILED");
        }

    }

    public void hardDeleteById(UUID withdrawUid) {
        withdrawRepository.forceDelete(withdrawUid);
    }


}
