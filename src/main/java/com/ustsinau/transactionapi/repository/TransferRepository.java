package com.ustsinau.transactionapi.repository;

import com.ustsinau.transactionapi.entity.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface TransferRepository extends JpaRepository<TransferEntity, UUID>{

    @Modifying
    @Query("DELETE FROM TransferEntity t WHERE t.uid = :uid")
    void forceDelete(@Param("uid") UUID uid);

}
