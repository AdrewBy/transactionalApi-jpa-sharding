package com.ustsinau.transactionapi.service;


import com.ustsinau.transactionapi.entity.PaymentEntity;
import com.ustsinau.transactionapi.enums.TransactionState;
import com.ustsinau.transactionapi.entity.WalletEntity;
import com.ustsinau.transactionapi.exception.WalletNotFoundException;
import com.ustsinau.transactionapi.repository.PaymentRequestRepository;
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
public class PaymentService {

    private final PaymentRequestRepository paymentRequestRepository;
    private final WalletRepository walletRepository;


    @Transactional
    public PaymentEntity createPaymentRequest(String userUid,
                                              String walletUid,
                                              BigDecimal amount,
                                              String comment,
                                              Long paymentMethodId) {

        WalletEntity wallet = walletRepository.findById(UUID.fromString(walletUid))
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found with UID: " + walletUid, "WALLET_NOT_FOUND"));

        // Создаем платежный запрос
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setUserUid(UUID.fromString(userUid));
        paymentEntity.setWallet(wallet);
        paymentEntity.setAmount(amount);
        paymentEntity.setStatus(TransactionState.PENDING); // Статус по умолчанию
        paymentEntity.setComment(comment);
        paymentEntity.setPaymentMethodId(paymentMethodId);
        paymentEntity.setCreatedAt(LocalDateTime.now());


        return paymentRequestRepository.save(paymentEntity);

    }
}
