package com.ustsinau.transactionapi.entity;


import com.ustsinau.transactionapi.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(schema = "transactional", name = "wallet_types")
public class WalletTypeEntity {

    @Id
    @GeneratedValue
    private UUID uid;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime modifiedAt;

    @Column(nullable = false,length = 32)
    private String name;

    @Column(nullable = false, length = 3)
    private String currencyCode;

    @Enumerated(EnumType.STRING)
    @Column(length = 18)
    private Status status;

    @Column(nullable = true)
    private LocalDateTime archivedAt;

    @Column(length = 15, nullable = true)
    private String userType;

    @Column(length = 255, nullable = true)
    private String creator;

    @Column(length = 255, nullable = true)
    private String modifier;


}
