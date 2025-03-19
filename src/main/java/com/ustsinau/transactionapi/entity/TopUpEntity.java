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
@Table(schema = "transactional",name = "top_up_requests")
public class TopUpEntity {


    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "uid", updatable = false, nullable = false)
    private UUID uid;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "provider", nullable = false)
    private String provider;

    @ManyToOne
    @JoinColumn(name = "payment_request_uid", nullable = false, foreignKey = @ForeignKey(name = "fk_top_up_requests_payment_requests"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PaymentEntity paymentRequest;

}
