package com.ustsinau.transactionapi.service.compensation;

import com.ustsinau.transactionapi.dto.response.TransactionResponse;
import com.ustsinau.transactionapi.entity.PaymentEntity;
import com.ustsinau.transactionapi.entity.TransactionalEntity;
import com.ustsinau.transactionapi.entity.TransferEntity;
import com.ustsinau.transactionapi.entity.WalletEntity;
import com.ustsinau.transactionapi.mappers.TransactionalMapper;
import com.ustsinau.transactionapi.repository.TransferRepository;
import com.ustsinau.transactionapi.service.PaymentService;
import com.ustsinau.transactionapi.service.TransactionService;
import com.ustsinau.transactionapi.service.WalletService;
import com.ustsinau.transactionapi.service.compensation.aopV2.CompensatingTransaction;
import com.ustsinau.transactionapi.service.compensation.aopV2.CompensatingTransactionAspect;
import com.ustsinau.transactionapi.service.compensation.aopV2.CompensationDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompensationTransferServiceV2 {

    private final TransferRepository transferRepository;
    private final TransactionService transactionService;
    private final PaymentService paymentService;
    private final WalletService walletService;

    private final TransactionalMapper transactionalMapper;
    private final CompensatingTransactionAspect compensatingTransactionAspect;

    @CompensatingTransaction(stepName = "transferOperation")
    @Transactional
    public TransactionResponse executeTransferTransaction(
            TransferEntity transfer,
            WalletEntity walletFrom,
            WalletEntity walletTo,
            PaymentEntity paymentFrom,
            PaymentEntity paymentTo,
            TransactionalEntity transactionFrom,
            TransactionalEntity transactionTo,
            BigDecimal oldBalanceFrom,
            BigDecimal oldBalanceTo,
            BigDecimal convertedAmount
    ) {

        // Сохранение платежей
        paymentService.save(paymentFrom);
        compensatingTransactionAspect.registerCompensation(
                () -> paymentService.hardDeleteById(paymentFrom.getUid()),
                "paymentFrom.hardDeleteById"
        );

        paymentService.save(paymentTo);
        compensatingTransactionAspect.registerCompensation(
                () -> paymentService.hardDeleteById(paymentTo.getUid()),
                "paymentTo.hardDeleteById"
        );

        // Сохранение транзакций
        transactionService.save(transactionFrom);
        compensatingTransactionAspect.registerCompensation(
                () -> transactionService.hardDeleteById(transactionFrom.getUid()),
                "transactionFrom.hardDeleteById"
        );

        transactionService.save(transactionTo);
        compensatingTransactionAspect.registerCompensation(
                () -> transactionService.hardDeleteById(transactionTo.getUid()),
                "transactionTo.hardDeleteById"
        );

        // Обновление балансов
        walletFrom.setBalance(oldBalanceFrom.subtract(paymentFrom.getAmount()));
        walletService.updateBalance(walletFrom.getUid(), walletFrom.getBalance(), walletFrom.getUserUid());
        compensatingTransactionAspect.registerCompensation(
                () -> walletService.updateBalance(walletFrom.getUid(), oldBalanceFrom, walletFrom.getUserUid()),
                "walletFrom balance rollback"
        );

        walletTo.setBalance(oldBalanceTo.add(convertedAmount));
        walletService.updateBalance(walletTo.getUid(), walletTo.getBalance(), walletTo.getUserUid());
        compensatingTransactionAspect.registerCompensation(
                () -> walletService.updateBalance(walletTo.getUid(), oldBalanceTo, walletTo.getUserUid()),
                "walletTo balance rollback"
        );

        // Сохранение трансфера
        transferRepository.save(transfer);
        compensatingTransactionAspect.registerCompensation(
                () -> transferRepository.hardDeleteById(transfer.getUid()),
                "transfer.hardDeleteById"
        );

        return TransactionResponse.toResponse(transactionalMapper.map(transactionFrom));
    }
}