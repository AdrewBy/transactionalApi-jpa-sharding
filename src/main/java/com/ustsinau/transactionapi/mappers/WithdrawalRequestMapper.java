package com.ustsinau.transactionapi.mappers;

import com.ustsinau.transactionapi.dto.WithdrawalDto;
import com.ustsinau.transactionapi.entity.WithdrawalEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WithdrawalRequestMapper {

    WithdrawalDto map(WithdrawalEntity withdrawalEntity);

    @InheritInverseConfiguration
    WithdrawalEntity map(WithdrawalDto withdrawalRequestDto);

}
