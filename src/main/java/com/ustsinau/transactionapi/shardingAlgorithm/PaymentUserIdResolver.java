package com.ustsinau.transactionapi.shardingAlgorithm;

import com.ustsinau.transactionapi.entity.PaymentEntity;
import com.ustsinau.transactionapi.exception.PaymentNotFoundException;
import com.ustsinau.transactionapi.repository.PaymentRequestRepository;
import com.ustsinau.transactionapi.service.PaymentService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class PaymentUserIdResolver {

    private final PaymentService paymentService;

    public PaymentUserIdResolver(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Transactional(readOnly = true)
    public UUID findUserIdByPaymentUid(UUID paymentUid) {
        PaymentEntity paymentEntity = paymentService.getById(paymentUid);
        return paymentEntity.getUserUid();
    }
}
