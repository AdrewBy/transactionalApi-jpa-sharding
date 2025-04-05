package com.ustsinau.transactionapi.service;


import com.ustsinau.transactionapi.dto.request.TopUpRequestDto;
import com.ustsinau.transactionapi.dto.response.TransactionResponse;
import com.ustsinau.transactionapi.entity.PaymentEntity;
import com.ustsinau.transactionapi.entity.TopUpEntity;
import com.ustsinau.transactionapi.entity.TransactionalEntity;
import com.ustsinau.transactionapi.entity.WalletEntity;
import com.ustsinau.transactionapi.enums.TransactionState;
import com.ustsinau.transactionapi.enums.TypeTransaction;
import com.ustsinau.transactionapi.exception.WalletNotFoundException;
import com.ustsinau.transactionapi.mappers.TransactionalMapper;
import com.ustsinau.transactionapi.repository.TopUpRepository;
import com.ustsinau.transactionapi.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopUpService {

    private final TopUpRepository topUpRepository;
    private final WalletRepository walletRepository;

    private final PaymentService paymentService;
    private final TransactionService transactionService;

    private final TransactionalMapper transactionalMapper;

    @Transactional
    public TransactionResponse createTopUpPayment(TopUpRequestDto request) {

        WalletEntity wallet = walletRepository.findById(UUID.fromString(request.getWalletUid()))
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found with UID: " + request.getWalletUid(), "WALLET_NOT_FOUND"));

        PaymentEntity paymentEntity = paymentService.createPaymentRequest(
                request.getUserUid(),
                request.getWalletUid(),
                request.getAmount(),
                request.getComment(),
                request.getPaymentMethodId()
        );

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

        TopUpEntity topUp = TopUpEntity.builder()
//                .provider(request.getProvider())
                .provider(null)
                .createdAt(LocalDateTime.now())
                .paymentRequest(paymentEntity)
                .build();

        try (HintManager hintManager = HintManager.getInstance()) {
//            HintManager.clear();
            hintManager.addDatabaseShardingValue("top_up_requests", request.getUserUid());
            topUpRepository.save(topUp);

            wallet.setBalance(wallet.getBalance().add(request.getAmount()));
            walletRepository.updateBalance(wallet.getUid(), wallet.getBalance());

            return TransactionResponse.toResponse(transactionalMapper.map(transactionalEntity));
        } catch (Exception e) {
            // Любая другая ошибка (например, NullPointerException, IllegalArgumentException)
            throw new NullPointerException();
        }


    }
}