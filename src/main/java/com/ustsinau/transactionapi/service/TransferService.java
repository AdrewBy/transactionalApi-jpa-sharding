package com.ustsinau.transactionapi.service;


import com.ustsinau.transactionapi.dto.request.TransferRequestDto;
import com.ustsinau.transactionapi.dto.response.TransactionResponse;
import com.ustsinau.transactionapi.entity.PaymentEntity;
import com.ustsinau.transactionapi.entity.TransactionalEntity;
import com.ustsinau.transactionapi.entity.TransferEntity;
import com.ustsinau.transactionapi.entity.WalletEntity;
import com.ustsinau.transactionapi.enums.PaymentStatus;
import com.ustsinau.transactionapi.exception.InsufficientFundsException;
import com.ustsinau.transactionapi.exception.TransferFailedException;
import com.ustsinau.transactionapi.repository.TransferRepository;
import com.ustsinau.transactionapi.service.compensation.CompensationTransferServiceV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepository transferRepository;
    private final WalletService walletService;
    private final TransactionService transactionService;

    private final CompensationTransferServiceV2 compensationTransferServiceV2;

    private final BigDecimal COMMISSION_RATE = new BigDecimal("0.01");


    public TransactionResponse createTransferPayment(TransferRequestDto request) {
        WalletEntity walletFrom = walletService.getById(UUID.fromString(request.getWalletUidFrom()));
        WalletEntity walletTo = walletService.getById(UUID.fromString(request.getWalletUidTo()));

        BigDecimal oldBalanceTo = walletTo.getBalance();
        BigDecimal oldBalanceFrom = walletFrom.getBalance();
        // Получаем курс конвертации с вычетом комиссии
        BigDecimal convertedAmount = getConvertedAmount(request);

        if (oldBalanceFrom.compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds in the wallet", "INSUFFICIENT_FUNDS");
        }

        PaymentEntity paymentEntityFrom = PaymentEntity.builder()
                .userUid(UUID.fromString(request.getUserUidFrom()))
                .wallet(walletFrom)
                .amount(request.getAmount())
                .status(PaymentStatus.IN_PROGRESS)
                .comment(request.getComment())
                .paymentMethodId(request.getPaymentMethodId())
                .build();

        PaymentEntity paymentEntityTo = PaymentEntity.builder()
                .userUid(UUID.fromString(request.getUserUidTo()))
                .wallet(walletTo)
                .amount(convertedAmount)
                .status(PaymentStatus.IN_PROGRESS)
                .comment(request.getComment())
                .paymentMethodId(request.getPaymentMethodId())
                .build();

        TransactionalEntity transactionFrom = transactionService.createTransactionEntityWithoutSave(
                paymentEntityFrom, walletFrom, UUID.fromString(request.getUserUidFrom()),
                request.getAmount(), request.getType());

        TransactionalEntity transactionTo = transactionService.createTransactionEntityWithoutSave(
                paymentEntityTo, walletTo, UUID.fromString(request.getUserUidTo()),
                convertedAmount, request.getType());

        TransferEntity transfer = buildTransferEntity(paymentEntityFrom, paymentEntityTo, request);

        try {
            return compensationTransferServiceV2.executeTransferTransaction(
                    transfer,
                    walletFrom,
                    walletTo,
                    paymentEntityFrom,
                    paymentEntityTo,
                    transactionFrom,
                    transactionTo,
                    oldBalanceFrom,
                    oldBalanceTo,
                    convertedAmount
            );
        } catch (Exception e) {
            log.error("Transfer failed", e);
            throw new TransferFailedException("Transfer failed: " + e.getMessage(), "TRANSFER_FAILED");
        }
    }

    public void hardDeleteById(UUID transferUid) {
        transferRepository.hardDeleteById(transferUid);
    }

    private TransferEntity buildTransferEntity(PaymentEntity paymentFrom,
                                               PaymentEntity paymentTo,
                                               TransferRequestDto request) {
        return TransferEntity.builder()
                .createdAt(LocalDateTime.now())
                .paymentRequestFrom(paymentFrom)
                .paymentRequestTo(paymentTo)
                .systemRate(request.getSystemRate())
                .build();
    }

    private BigDecimal getConvertedAmount(TransferRequestDto request) {
        BigDecimal systemRate = new BigDecimal(request.getSystemRate());
        // Рассчитываем комиссию (например, 1% от суммы перевода)
        BigDecimal commission = request.getAmount().multiply(COMMISSION_RATE);
        // Вычитаем комиссию из суммы перевода
        BigDecimal amountAfterCommission = request.getAmount().subtract(commission);
        // Конвертируем сумму в валюту кошелька-получателя
        return amountAfterCommission.multiply(systemRate);
    }
}
