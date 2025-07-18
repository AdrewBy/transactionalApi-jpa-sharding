package com.ustsinau.transactionapi.service.compensation;

import com.ustsinau.transactionapi.repository.TransferRepository;
import com.ustsinau.transactionapi.service.PaymentService;
import com.ustsinau.transactionapi.service.TransactionService;
import com.ustsinau.transactionapi.service.TransferService;
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
public class CompensationTransferService {

    private final TransferRepository transferRepository;
    private final TransactionService transactionService;
    private final PaymentService paymentService;
    private final WalletService walletService;
    private final EntityManager entityManager;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void compensateTransfer(
            UUID transferUid,
            UUID transactionUid,
            UUID paymentUidFrom,
            UUID paymentUidTo,
            UUID walletUidFrom,
            UUID walletUidTo,
            BigDecimal oldBalanceFrom,
            BigDecimal oldBalanceTo,
            UUID userUid
    ) {
        try {

            if (transferUid != null) {
                transferRepository.forceDelete(transferUid);
     //           transferService.hardDeleteById(transferUid);  // Явное удаление
                log.info("Transfer UID {} was force-deleted, transfer failed.", transferUid);
            }
            if (transactionUid != null) {
                transactionService.hardDeleteById(transactionUid);
                log.info("Transaction with UID {} was force-deleted, transfer failed", transactionUid);
            }
            if (paymentUidTo != null) {
                paymentService.hardDeleteById(paymentUidTo);
                log.info("PaymentEntityTo with UID {} was force-deleted, transfer failed.", paymentUidTo);
            }
            if (paymentUidFrom != null) {
                paymentService.hardDeleteById(paymentUidFrom);
                log.info("PaymentEntityFrom with UID {} was force-deleted, transfer failed.", paymentUidFrom);
            }
            walletService.updateBalance(walletUidTo, oldBalanceTo, userUid);
            walletService.updateBalance(walletUidFrom, oldBalanceFrom, userUid);

            entityManager.flush(); // Принудительно применяем изменения

            log.info("Compensation completed successfully for wallet {}", walletUidFrom);
            log.info("Compensation completed successfully for wallet {}", walletUidTo);
        } catch (Exception e) {
            log.error("Compensation failed for withdrawal {}", transferUid, e);
            throw e; // Пробрасываем исключение, чтобы пометить транзакцию как failed
        }
    }
}
