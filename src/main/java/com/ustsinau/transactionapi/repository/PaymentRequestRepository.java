package com.ustsinau.transactionapi.repository;


import com.ustsinau.transactionapi.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;


public interface PaymentRequestRepository extends JpaRepository<PaymentEntity, UUID> {

    @Modifying
    @Query("DELETE FROM PaymentEntity p WHERE p.uid = :uid")
    void forceDelete(@Param("uid") UUID uid);


}
