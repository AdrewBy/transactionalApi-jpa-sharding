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
import com.ustsinau.transactionapi.exception.WalletNotFoundException;
import com.ustsinau.transactionapi.mappers.TransactionalMapper;
import com.ustsinau.transactionapi.repository.TransferRepository;
import com.ustsinau.transactionapi.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    @Transactional
    public TransactionResponse createTransferPayment(TransferRequestDto request) {

        WalletEntity walletFrom = walletRepository.findById(UUID.fromString(request.getWalletUidFrom()))
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found with UID: " + request.getWalletUidFrom(), "WALLET_NOT_FOUND"));

        WalletEntity walletTo = walletRepository.findById(UUID.fromString(request.getWalletUidTo()))
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found with UID: " + request.getWalletUidTo(), "WALLET_NOT_FOUND"));

        // Получаем курс конвертации с вычетом комиссии
        BigDecimal convertedAmount = getConvertedAmount(request);

        // Проверяем, достаточно ли средств на кошельке
        BigDecimal currentBalanceFrom = walletFrom.getBalance();
        if (currentBalanceFrom.compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds in the wallet", "INSUFFICIENT_FUNDS");
        }

        // Снимаем сумму с баланса кошелька-отправителя
        BigDecimal newBalanceFrom = currentBalanceFrom.subtract(request.getAmount());
        walletFrom.setBalance(newBalanceFrom);
        walletRepository.save(walletFrom);

        // Добавляем конвертированную сумму на баланс кошелька-получателя
        BigDecimal currentBalanceTo = walletTo.getBalance();
        BigDecimal newBalanceTo = currentBalanceTo.add(convertedAmount);
        walletTo.setBalance(newBalanceTo);
        walletRepository.save(walletTo);

        PaymentEntity paymentEntityFrom = paymentService.createPaymentRequest(
                request.getUserUid(),
                request.getWalletUidFrom(),
                request.getAmount(),
                request.getComment(),
                request.getPaymentMethodId());

        PaymentEntity paymentEntityTo = paymentService.createPaymentRequest(
                request.getUserUid(),
                request.getWalletUidTo(),
                convertedAmount,
                request.getComment(),
                request.getPaymentMethodId());

        TransactionalEntity transactionalEntity = transactionService
                .createTransaction(TransactionalEntity
                                .builder()
                                .paymentRequest(paymentEntityFrom)
                                .type(TypeTransaction.valueOf(request.getType()))
                                .state(TransactionState.valueOf(request.getState()))
                                .amount(request.getAmount())
                                .userUid(UUID.fromString(request.getUserUid()))
                                .walletName(walletFrom.getName())
                                .wallet(walletFrom)
                                .build());

        transferRepository.save(TransferEntity
                .builder()
                .createdAt(LocalDateTime.now())
                .paymentRequestFrom(paymentEntityFrom)
                .paymentRequestTo(paymentEntityTo)
                .build());

        return TransactionResponse.toResponse(transactionalMapper.map(transactionalEntity));

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
