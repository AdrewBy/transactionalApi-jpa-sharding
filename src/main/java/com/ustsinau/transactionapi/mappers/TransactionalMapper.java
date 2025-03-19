package com.ustsinau.transactionapi.mappers;


import com.ustsinau.transactionapi.dto.TransactionalDto;
import com.ustsinau.transactionapi.entity.TransactionalEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionalMapper {

    TransactionalDto map(TransactionalEntity transactionalEntity);

    @InheritInverseConfiguration
    TransactionalEntity map(TransactionalDto transactionalDto);
}
