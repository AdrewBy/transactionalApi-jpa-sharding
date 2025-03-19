package com.ustsinau.transactionapi.mappers;


import com.ustsinau.transactionapi.dto.WalletTypeDto;
import com.ustsinau.transactionapi.entity.WalletTypeEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletTypeMapper {

    WalletTypeDto map(WalletTypeEntity walletTypeEntity);

    @InheritInverseConfiguration
    WalletTypeEntity map(WalletTypeDto walletTypeDto);

}
