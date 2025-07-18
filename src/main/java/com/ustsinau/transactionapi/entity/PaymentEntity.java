package com.ustsinau.transactionapi.entity;


import com.ustsinau.transactionapi.enums.PaymentStatus;
import com.ustsinau.transactionapi.enums.TransactionState;
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
@Builder(toBuilder = true)
@Entity
@Table(name = "payment_requests")
public class PaymentEntity  implements EntityWithStatus<PaymentStatus> {

    @Id
    @GeneratedValue
    private UUID uid;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime modifiedAt;

    @Column(nullable = false)
    private UUID userUid;

    @ManyToOne
    @JoinColumn(name = "wallet_uid", nullable = false, foreignKey = @ForeignKey(name = "fk_payment_requests_wallets"))
    private WalletEntity wallet;

    @Column(name = "amount", nullable = false, columnDefinition = "decimal(19, 2) default 0.0")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(length = 256, nullable = true)
    private String comment;

    @Column(name = "payment_method_id")
    private Long paymentMethodId;

    @PrePersist
    public void onInsert() {
        createdAt = LocalDateTime.now();
        modifiedAt = createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        modifiedAt = LocalDateTime.now();
    }

    @Override
    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

}
