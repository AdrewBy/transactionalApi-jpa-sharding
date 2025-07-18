package com.ustsinau.transactionapi.service;


import com.ustsinau.transactionapi.dto.request.TopUpRequestDto;
import com.ustsinau.transactionapi.dto.response.TransactionResponse;
import com.ustsinau.transactionapi.entity.PaymentEntity;
import com.ustsinau.transactionapi.entity.TopUpEntity;
import com.ustsinau.transactionapi.entity.TransactionalEntity;
import com.ustsinau.transactionapi.entity.WalletEntity;
import com.ustsinau.transactionapi.exception.TransferFailedException;
import com.ustsinau.transactionapi.mappers.TransactionalMapper;
import com.ustsinau.transactionapi.repository.TopUpRepository;
import com.ustsinau.transactionapi.service.compensation.CompensationTopUpService;
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
public class TopUpService {

    private final TopUpRepository topUpRepository;
    private final PaymentService paymentService;
    private final TransactionService transactionService;
    private final WalletService walletService;
    private final TransactionalMapper transactionalMapper;
    private final CompensationTopUpService compensationTopUpService;

    @Transactional
    public TransactionResponse createTopUpPayment(TopUpRequestDto request) {

        WalletEntity wallet = walletService.getById(UUID.fromString(request.getWalletUid()));
        BigDecimal oldBalance = wallet.getBalance();

        PaymentEntity paymentEntity = null;
        TransactionalEntity transaction = null;
        TopUpEntity topUp = null;
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

            topUp = topUpRepository.save(TopUpEntity
                    .builder()
                    .createdAt(LocalDateTime.now())
                    .paymentRequest(paymentEntity)
                    .provider(request.getProvider())
                    .build());

            wallet.setBalance(wallet.getBalance().add(request.getAmount()));
            walletService.updateBalance(wallet.getUid(),
                    wallet.getBalance(),
                    wallet.getUserUid());

            return TransactionResponse.toResponse(transactionalMapper.map(transaction));

        } catch (Exception ex) {
            try {
                compensationTopUpService.compensateTopUp(
                        topUp != null ? topUp.getUid() : null,
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

    public void hardDeleteById(UUID topUpUid) {
        topUpRepository.forceDelete(topUpUid);
    }


}