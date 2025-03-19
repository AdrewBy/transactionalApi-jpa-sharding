package com.ustsinau.transactionapi.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder(toBuilder = true)
@Table(schema = "transactional",name = "transfer_requests")
public class TransferEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "uid", updatable = false, nullable = false)
    private UUID uid;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "system_rate", nullable = false)
    private String systemRate;

    @ManyToOne
    @JoinColumn(name = "payment_request_uid_from", nullable = false, foreignKey = @ForeignKey(name = "fk_transfer_requests_payment_requests_from"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PaymentEntity paymentRequestFrom;

    @ManyToOne
    @JoinColumn(name = "payment_request_uid_to", nullable = false, foreignKey = @ForeignKey(name = "fk_transfer_requests_payment_requests_to"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PaymentEntity paymentRequestTo;
}
