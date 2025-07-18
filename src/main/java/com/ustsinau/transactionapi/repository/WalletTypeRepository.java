package com.ustsinau.transactionapi.repository;


import com.ustsinau.transactionapi.entity.WalletTypeEntity;
import com.ustsinau.transactionapi.mappers.PaymentMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletTypeRepository extends JpaRepository<WalletTypeEntity, UUID> {

    <T> Optional<T> findByNameAndCurrencyCode(String walletTypeName, String currencyCode);

    WalletTypeEntity findByCurrencyCode(String currencyCode);

    WalletTypeEntity findByName(String name);


    void deleteAll();

    boolean existsByName(String name);

    boolean existsByCurrencyCode(String currencyCode);
}
