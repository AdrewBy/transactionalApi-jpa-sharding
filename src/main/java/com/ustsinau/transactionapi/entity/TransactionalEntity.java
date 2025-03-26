package com.ustsinau.transactionapi.entity;

import com.ustsinau.transactionapi.enums.TransactionState;
import com.ustsinau.transactionapi.enums.TypeTransaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder(toBuilder = true)
@Table( name = "transactions")
public class TransactionalEntity {

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

    @Column(length = 32, nullable = false)
    private String walletName;

    @Column(name = "amount", nullable = false, columnDefinition = "decimal(19, 2) default 0.0")
    private BigDecimal amount;

    @Column(nullable = false, length = 32)
    private TypeTransaction type;

    @Column(nullable = false, length = 32)
    private TransactionState state;

    @ManyToOne
    @JoinColumn(name = "payment_request_uid", nullable = false, foreignKey = @ForeignKey(name = "fk_your_table_payment_requests"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PaymentEntity paymentRequest;
}
