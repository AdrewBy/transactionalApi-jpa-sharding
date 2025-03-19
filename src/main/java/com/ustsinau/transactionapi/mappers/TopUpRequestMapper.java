package com.ustsinau.transactionapi.mappers;


import com.ustsinau.transactionapi.dto.request.TopUpRequestDto;
import com.ustsinau.transactionapi.entity.TopUpEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface TopUpRequestMapper {

    TopUpRequestDto map(TopUpEntity topUpEntity);

    @InheritInverseConfiguration
    TopUpEntity map(TopUpRequestDto topUpRequestDto);
}
