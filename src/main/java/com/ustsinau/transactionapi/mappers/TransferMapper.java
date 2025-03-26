package com.ustsinau.transactionapi.mappers;


import com.ustsinau.transactionapi.dto.TransferDto;
import com.ustsinau.transactionapi.dto.request.TransferRequestDto;
import com.ustsinau.transactionapi.entity.TransferEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.util.UUID;

@Mapper(componentModel = "spring"

//        , uses = {PaymentMapper.class}
//        ,
//        imports = {LocalDateTime.class}
)
public interface TransferMapper {

//    @Mapping(target = "uid", source = "uid", qualifiedByName = "uuidToString")
//    @Mapping(target = "createdAt", source = "createdAt")
//    @Mapping(target = "systemRate", source = "systemRate")
//    @Mapping(target = "paymentRequestFrom", source = "paymentRequestFrom")
//    @Mapping(target = "paymentRequestTo", source = "paymentRequestTo")
    TransferDto toDto(TransferEntity entity);

    @InheritInverseConfiguration
//    @Mapping(target = "uid", source = "uid", qualifiedByName = "stringToUuid")
    TransferEntity toEntity(TransferDto dto);

//    @Named("uuidToString")
//    static String uuidToString(UUID uuid) {
//        return uuid != null ? uuid.toString() : null;
//    }
//
//    @Named("stringToUuid")
//    static UUID stringToUuid(String uuid) {
//        return uuid != null ? UUID.fromString(uuid) : null;
//    }
}
