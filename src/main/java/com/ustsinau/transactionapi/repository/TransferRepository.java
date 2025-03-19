package com.ustsinau.transactionapi.repository;

import com.ustsinau.transactionapi.entity.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransferRepository extends JpaRepository<TransferEntity, UUID>{

}
