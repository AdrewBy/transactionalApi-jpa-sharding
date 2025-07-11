package com.ustsinau.transactionapi.service.compensation;


import com.ustsinau.transactionapi.repository.WithdrawRepository;
import com.ustsinau.transactionapi.service.PaymentService;
import com.ustsinau.transactionapi.service.TransactionService;
import com.ustsinau.transactionapi.service.WalletService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompensationWithdrawService {
    private final WithdrawRepository withdrawRepository;
    private final TransactionService transactionService;
    private final PaymentService paymentService;
    private final WalletService walletService;
    private final EntityManager entityManager;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void compensateWithdrawal(
            UUID withdrawalUid,
            UUID transactionUid,
            UUID paymentUid,
            UUID walletUid,
            BigDecimal oldBalance,
            UUID userUid
    ) {
        try {
            if (withdrawalUid != null) {
                withdrawRepository.forceDelete(withdrawalUid);
                log.info("Withdraw {} was force-deleted", withdrawalUid);
            }

            if (transactionUid != null) {
                transactionService.forceDelete(transactionUid);
                log.info("Transaction {} was force-deleted", transactionUid);
            }

            if (paymentUid != null) {
                paymentService.forceDelete(paymentUid);
                log.info("Payment {} was force-deleted", paymentUid);
            }

            walletService.updateBalance(walletUid, oldBalance, userUid);
            entityManager.flush(); // Принудительно применяем изменения

            log.info("Compensation completed successfully for wallet {}", walletUid);
        } catch (Exception e) {
            log.error("Compensation failed for withdrawal {}", withdrawalUid, e);
            throw e; // Пробрасываем исключение, чтобы пометить транзакцию как failed
        }
    }
}
