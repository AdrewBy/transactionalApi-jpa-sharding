package com.ustsinau.transactionapi.repository;


import com.ustsinau.transactionapi.entity.WithdrawalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WithdrawRepository extends JpaRepository<WithdrawalEntity, UUID> {
}
