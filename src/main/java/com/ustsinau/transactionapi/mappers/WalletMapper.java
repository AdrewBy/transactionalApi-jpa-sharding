package com.ustsinau.transactionapi.mappers;


import com.ustsinau.transactionapi.dto.WalletDto;
import com.ustsinau.transactionapi.entity.WalletEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    WalletDto map(WalletEntity walletEntity);

    @InheritInverseConfiguration
    WalletEntity map(WalletDto walletDto);
}
