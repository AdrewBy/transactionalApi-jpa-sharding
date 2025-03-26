package com.ustsinau.transactionapi.mappers;


import com.ustsinau.transactionapi.dto.PaymentDto;
import com.ustsinau.transactionapi.entity.PaymentEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentDto map(PaymentEntity paymentEntity);

    @InheritInverseConfiguration
    PaymentEntity map(PaymentDto paymentRequestDto);
}
