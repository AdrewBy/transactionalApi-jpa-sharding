package com.ustsinau.transactionapi.repository;

import com.ustsinau.transactionapi.entity.TransactionalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionalEntity, UUID> {


    @Query("SELECT t FROM TransactionalEntity t WHERE " +
            "(:userUid IS NULL OR t.userUid = :userUid) AND " +
            "(:walletUid IS NULL OR t.wallet.uid = :walletUid) AND " +
            "(:type IS NULL OR t.type = :type) AND " +
            "(:state IS NULL OR t.state = :state) AND " +
            "(:dateFrom IS NULL OR t.createdAt >= :dateFrom) AND " +
            "(:dateTo IS NULL OR t.createdAt <= :dateTo)")
    List<TransactionalEntity> findByFilters(
            @Param("userUid") UUID userUid,
            @Param("walletUid") UUID walletUid,
            @Param("type") String type,
            @Param("state") String state,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("dateTo") LocalDateTime dateTo
    );

    @Modifying
    @Query("DELETE FROM TransactionalEntity t WHERE t.uid = :uid")
    void cancelById(@Param("uid") UUID transactionalUid);
}