package com.ustsinau.transactionapi.repository;


import com.ustsinau.transactionapi.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface PaymentRequestRepository extends JpaRepository<PaymentEntity, UUID> {
}
