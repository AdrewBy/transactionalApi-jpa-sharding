package com.ustsinau.transactionapi.mappers;


import com.ustsinau.transactionapi.dto.TopUpDto;
import com.ustsinau.transactionapi.entity.TopUpEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface TopUpRequestMapper {

    TopUpDto map(TopUpEntity topUpEntity);

    @InheritInverseConfiguration
    TopUpEntity map(TopUpDto topUpRequestDto);
}
