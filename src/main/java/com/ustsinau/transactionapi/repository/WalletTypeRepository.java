package com.ustsinau.transactionapi.repository;


import com.ustsinau.transactionapi.entity.WalletTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletTypeRepository extends JpaRepository<WalletTypeEntity, UUID> {

    <T> Optional<T> findByNameAndCurrencyCode(String walletTypeName, String currencyCode);

    WalletTypeEntity findByCurrencyCode(String currencyCode);
}
