package com.ustsinau.transactionapi.mappers;

import com.ustsinau.transactionapi.dto.request.WithdrawalRequestDto;
import com.ustsinau.transactionapi.entity.WithdrawalEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WithdrawalRequestMapper {

    WithdrawalRequestDto map(WithdrawalEntity withdrawalEntity);

    @InheritInverseConfiguration
    WithdrawalEntity map(WithdrawalRequestDto withdrawalRequestDto);

}
