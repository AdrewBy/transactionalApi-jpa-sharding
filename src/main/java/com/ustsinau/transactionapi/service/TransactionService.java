package com.ustsinau.transactionapi.service;

import com.ustsinau.transactionapi.dto.TransactionalDto;
import com.ustsinau.transactionapi.dto.response.TransactionResponse;
import com.ustsinau.transactionapi.dto.response.TransactionStatusResponse;
import com.ustsinau.transactionapi.entity.*;
import com.ustsinau.transactionapi.exception.TransactionNotFoundException;
import com.ustsinau.transactionapi.mappers.TransactionalMapper;
import com.ustsinau.transactionapi.repository.TransactionRepository;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final TransactionalMapper transactionalMapper;


    public List<TransactionResponse> searchTransactions(UUID userUid, UUID walletUid, String type, String state, LocalDateTime dateFrom, LocalDateTime dateTo) {
        // Реализация поиска транзакций по фильтрам
        return transactionRepository.findByFilters(userUid, walletUid, type, state, dateFrom, dateTo)
                .stream()
                .map(transactionalMapper::map)
                .map(TransactionResponse::toResponse)
                .toList();
    }

    public TransactionStatusResponse getTransactionStatus(String uid) {

        return toResponseStatus(transactionalMapper.map(transactionRepository.findById(UUID.fromString(uid))
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with ID: " + uid, "TRANSACTION_NOT_FOUND"))));
    }

    @Transactional
    public TransactionalEntity createTransaction(TransactionalEntity entity) {

        return transactionRepository.save(entity);
    }


    private TransactionStatusResponse toResponseStatus(TransactionalDto transaction) {
        TransactionStatusResponse response = new TransactionStatusResponse();
        response.setUid(transaction.getUid());
        response.setState(transaction.getState());
        response.setUpdatedAt(transaction.getModifiedAt());
        return response;
    }


    public void cancelTransaction(UUID uid) {

        transactionRepository.deleteById(uid);
    }
}
