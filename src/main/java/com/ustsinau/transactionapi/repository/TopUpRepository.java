package com.ustsinau.transactionapi.repository;

import com.ustsinau.transactionapi.entity.TopUpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TopUpRepository extends JpaRepository<TopUpEntity, UUID> {

    @Modifying
    @Query("DELETE FROM TopUpEntity t WHERE t.uid = :uid")
    void forceDelete(@Param("uid") UUID uid);
}
