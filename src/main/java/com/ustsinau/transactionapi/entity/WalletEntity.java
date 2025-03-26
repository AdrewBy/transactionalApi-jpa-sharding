package com.ustsinau.transactionapi.entity;


import com.ustsinau.transactionapi.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder(toBuilder = true)
@Table( name = "wallets")
public class WalletEntity {

    @Id
    @GeneratedValue
    private UUID uid;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime modifiedAt;

    @Column(nullable = false, length = 32)
    private String name;

    @ManyToOne
    @JoinColumn(name = "wallet_type_uid", nullable = false, foreignKey = @ForeignKey(name = "fk_wallets_wallet_types"))
    private WalletTypeEntity walletType;

    @Column(nullable = false)
    private UUID userUid;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private Status status;

    @Column(name = "balance", nullable = false, columnDefinition = "decimal(19, 2) default 0.0")
    private BigDecimal balance;

    @Column(nullable = true)
    private LocalDateTime archivedAt;



}
