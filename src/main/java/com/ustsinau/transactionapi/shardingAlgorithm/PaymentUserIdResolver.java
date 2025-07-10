package com.ustsinau.transactionapi.shardingAlgorithm;

import com.ustsinau.transactionapi.entity.PaymentEntity;
import com.ustsinau.transactionapi.exception.PaymentNotFoundException;
import com.ustsinau.transactionapi.repository.PaymentRequestRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class PaymentUserIdResolver {

    private final PaymentRequestRepository paymentRepository;

    public PaymentUserIdResolver(PaymentRequestRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional(readOnly = true)
    public UUID findUserIdByPaymentUid(UUID paymentUid) {
        PaymentEntity paymentEntity = paymentRepository.findById(paymentUid).orElseThrow(() ->
                new PaymentNotFoundException("Payment not found by id: " + paymentUid, "PAYMENT_NOT_FOUND")
        );
        return paymentEntity.getUserUid();
    }
}
