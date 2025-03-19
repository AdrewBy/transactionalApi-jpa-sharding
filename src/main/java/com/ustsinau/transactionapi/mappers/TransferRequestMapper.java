package com.ustsinau.transactionapi.mappers;


import com.ustsinau.transactionapi.dto.request.TransferRequestDto;
import com.ustsinau.transactionapi.entity.TransferEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransferRequestMapper {

    TransferRequestDto map(TransferEntity transferEntity);

    @InheritInverseConfiguration
    TransferEntity map(TransferRequestDto transferRequestDto);
}
