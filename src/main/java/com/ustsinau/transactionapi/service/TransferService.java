package com.ustsinau.transactionapi.service;


import com.ustsinau.transactionapi.dto.request.TransferRequestDto;
import com.ustsinau.transactionapi.dto.response.TransactionResponse;
import com.ustsinau.transactionapi.entity.PaymentEntity;
import com.ustsinau.transactionapi.entity.TransactionalEntity;
import com.ustsinau.transactionapi.entity.TransferEntity;
import com.ustsinau.transactionapi.entity.WalletEntity;
import com.ustsinau.transactionapi.enums.TransactionState;
import com.ustsinau.transactionapi.enums.TypeTransaction;
import com.ustsinau.transactionapi.exception.InsufficientFundsException;
import com.ustsinau.transactionapi.exception.TransactionNotFoundException;
import com.ustsinau.transactionapi.exception.WalletNotFoundException;
import com.ustsinau.transactionapi.mappers.TransactionalMapper;
import com.ustsinau.transactionapi.repository.TransferRepository;
import com.ustsinau.transactionapi.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepository transferRepository;
    private final WalletRepository walletRepository;

    private final PaymentService paymentService;
    private final TransactionService transactionService;

    private final TransactionalMapper transactionalMapper;

    private final BigDecimal COMMISSION_RATE = new BigDecimal("0.01");

    @Transactional(rollbackFor = Exception.class)
    public TransactionResponse createTransferPayment(TransferRequestDto request) {

        WalletEntity walletFrom = walletRepository.findById(UUID.fromString(request.getWalletUidFrom()))
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found with UID: " + request.getWalletUidFrom(), "WALLET_NOT_FOUND"));

        WalletEntity walletTo = walletRepository.findById(UUID.fromString(request.getWalletUidTo()))
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found with UID: " + request.getWalletUidTo(), "WALLET_NOT_FOUND"));

        BigDecimal oldBalanceTo = walletTo.getBalance();

        // Получаем курс конвертации с вычетом комиссии
        BigDecimal convertedAmount = getConvertedAmount(request);

        // Проверяем, достаточно ли средств на кошельке
        BigDecimal currentBalanceFrom = walletFrom.getBalance();
        if (currentBalanceFrom.compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds in the wallet", "INSUFFICIENT_FUNDS");
        }

        // Храним созданные сущности для возможной компенсации
        PaymentEntity paymentEntityFrom = null;
        PaymentEntity paymentEntityTo = null;
        TransactionalEntity transaction = null;
        TransferEntity transfer = null;

        try {
            //   HintManager.clear();
            // 1. Операции с платежами
            paymentEntityFrom = paymentService.createPaymentRequest(
                    request.getUserUid(),
                    request.getWalletUidFrom(),
                    request.getAmount(),
                    request.getComment(),
                    request.getPaymentMethodId());

            paymentEntityTo = paymentService.createPaymentRequest(
                    request.getUserUid(),
                    request.getWalletUidTo(),
                    convertedAmount,
                    request.getComment(),
                    request.getPaymentMethodId());


            // 2. Создание транзакции
            transaction = transactionService.createTransaction(
                    buildTransactionalEntity(paymentEntityFrom, walletFrom, request));

            // 3. Обновление балансов
            BigDecimal newBalanceFrom = currentBalanceFrom.subtract(request.getAmount());
            walletFrom.setBalance(newBalanceFrom);
            walletRepository.updateBalance(
                    walletFrom.getUid(),
                    walletFrom.getBalance(),
                    walletFrom.getUserUid() // Используем user_uid для шардирования
            );
            // Обновление баланса получателя
            BigDecimal newBalanceTo = walletTo.getBalance().add(convertedAmount)
                    .setScale(2, RoundingMode.HALF_UP);
            walletTo.setBalance(newBalanceTo);
            walletRepository.updateBalance(
                    walletTo.getUid(),
                    walletTo.getBalance(),
                    walletTo.getUserUid());

            // 4. Создание трансфера
            transfer = transferRepository.save(buildTransferEntity(
                    paymentEntityFrom
                    , paymentEntityTo
                    , request));

            return TransactionResponse.toResponse(transactionalMapper.map(transaction));

        } catch (Exception e) {
            // Компенсирующие операции
            try {
                if (transfer != null) transferRepository.delete(transfer);
                if (transaction != null) transactionService.cancelTransaction(transaction.getUid());
                if (paymentEntityTo != null) paymentService.cancelPayment(paymentEntityTo.getUid());
                if (paymentEntityFrom != null) paymentService.cancelPayment(paymentEntityFrom.getUid());

                // Возвращаем балансы
                if (walletFrom != null && walletTo != null) {
                    walletRepository.updateBalance(walletFrom.getUid(), currentBalanceFrom, UUID.fromString(request.getUserUid()));
                    walletRepository.updateBalance(walletTo.getUid(), oldBalanceTo, UUID.fromString(request.getUserUid()));
                }

            } catch (Exception compEx) {
                log.error("Compensation failed", compEx);
            }
            throw new TransactionNotFoundException("Transfer failed", "" + e);
        }
    }

    private TransactionalEntity buildTransactionalEntity(PaymentEntity paymentEntityFrom,
                                                         WalletEntity walletFrom,
                                                         TransferRequestDto request) {
        return TransactionalEntity
                .builder()
                .createdAt(LocalDateTime.now())
                .paymentRequest(paymentEntityFrom)
                .type(TypeTransaction.valueOf(request.getType()))
                .state(TransactionState.valueOf(request.getState()))
                .amount(request.getAmount())
                .userUid(UUID.fromString(request.getUserUid()))
                .walletName(walletFrom.getName())
                .wallet(walletFrom)
                .build();
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
