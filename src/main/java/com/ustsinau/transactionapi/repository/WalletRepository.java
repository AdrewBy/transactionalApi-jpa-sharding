package com.ustsinau.transactionapi.repository;


import com.ustsinau.transactionapi.entity.WalletEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {

  //  @Query("SELECT w FROM WalletEntity w JOIN FETCH w.walletType WHERE w.userUid = :uid")
    //   @EntityGraph(attributePaths = {"walletType"})
    List<WalletEntity> findByUserUid(UUID uid);

    @Query("SELECT w FROM WalletEntity w " +
            "JOIN w.walletType wt " +
            "WHERE w.userUid = :userUid AND wt.currencyCode = :currency")
    WalletEntity findByUserUidAndCurrency(
            @Param("userUid") UUID userUid,
            @Param("currency") String currency
    );

    @Modifying
    @Query("UPDATE WalletEntity w " +
            "SET w.balance = :balance " +
            "WHERE w.uid = :uid AND w.userUid = :userUid")
    void updateBalance(@Param("uid") UUID uid,
                       @Param("balance") BigDecimal balance,
                       @Param("userUid") UUID userUid);


    boolean existsByNameAndUserUid(String name, UUID userUid);

}
