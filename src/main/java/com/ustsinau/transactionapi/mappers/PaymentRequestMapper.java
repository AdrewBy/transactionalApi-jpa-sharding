package com.ustsinau.transactionapi.mappers;


import com.ustsinau.transactionapi.dto.request.PaymentRequestDto;
import com.ustsinau.transactionapi.entity.PaymentEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentRequestMapper {

    PaymentRequestDto map(PaymentEntity paymentEntity);

    @InheritInverseConfiguration
    PaymentEntity map(PaymentRequestDto paymentRequestDto);
}
