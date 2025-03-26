package com.ustsinau.transactionapi.mappers;


import com.ustsinau.transactionapi.dto.WalletTypeDto;
import com.ustsinau.transactionapi.entity.WalletTypeEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WalletTypeMapper {

//    @Mapping(target = "uid", expression = "java(entity.getUid().toString())")
    WalletTypeDto map(WalletTypeEntity entity);

//    @Mapping(target = "uid", expression = "java(UUID.fromString(dto.getUid()))")
    @InheritInverseConfiguration
    WalletTypeEntity map(WalletTypeDto dto);

}
