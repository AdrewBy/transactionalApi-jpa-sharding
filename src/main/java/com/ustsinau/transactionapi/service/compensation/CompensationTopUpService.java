package com.ustsinau.transactionapi.service.compensation;

import com.ustsinau.transactionapi.repository.TopUpRepository;
import com.ustsinau.transactionapi.service.PaymentService;
import com.ustsinau.transactionapi.service.TopUpService;
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
public class CompensationTopUpService {

    private final TopUpRepository topUpRepository;

    private final TransactionService transactionService;
    private final PaymentService paymentService;
    private final WalletService walletService;
    private final EntityManager entityManager;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void compensateTopUp(
            UUID topUpUid,
            UUID transactionUid,
            UUID paymentUid,
            UUID walletUid,
            BigDecimal oldBalance,
            UUID userUid
    ) {
        try {
            if (topUpUid != null) {
                topUpRepository.forceDelete(topUpUid);
                log.info("TopUp {} was force-deleted", topUpUid);
            }

            if (transactionUid != null) {
                transactionService.hardDeleteById(transactionUid);
                log.info("Transaction {} was force-deleted", transactionUid);
            }

            if (paymentUid != null) {
                paymentService.hardDeleteById(paymentUid);
                log.info("Payment {} was force-deleted", paymentUid);
            }

            walletService.updateBalance(walletUid, oldBalance, userUid);
            entityManager.flush(); // Принудительно применяем изменения

            log.info("Compensation completed successfully for wallet {}", walletUid);
        } catch (Exception e) {
            log.error("Compensation failed for withdrawal {}", topUpUid, e);
            throw e; // Пробрасываем исключение, чтобы пометить транзакцию как failed
        }
    }
}
