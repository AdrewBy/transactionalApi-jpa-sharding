package com.ustsinau.transactionapi.errorhandling;

import com.ustsinau.transactionapi.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = {AuthException.class, UnauthorizedException.class})
    public ResponseEntity<Object> handleUnauthorizedExceptions(ApiException ex) {
        return buildErrorResponse(ex, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<Object> handleTransactionNotFoundException(TransactionNotFoundException ex) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<Object> handleWalletNotFoundException(WalletNotFoundException ex) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<Object> handleInsufficientFundsException(InsufficientFundsException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(UuidInvalidException.class)
    public ResponseEntity<Object> handleUuidInvalidException(UuidInvalidException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleApiException(ApiException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        var errorMap = new LinkedHashMap<String, Object>();
        errorMap.put("code", "INTERNAL_ERROR");
        errorMap.put("message", ex.getMessage() != null ? ex.getMessage() : ex.getClass().getName());

        var errorList = new ArrayList<Map<String, Object>>();
        errorList.add(errorMap);

        var errors = new HashMap<String, Object>();
        errors.put("errors", errorList);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
    }

    private ResponseEntity<Object> buildErrorResponse(ApiException ex, HttpStatus status) {
        var errorMap = new LinkedHashMap<String, Object>();
        errorMap.put("code", ex.getErrorCode());
        errorMap.put("message", ex.getMessage());

        var errorList = new ArrayList<Map<String, Object>>();
        errorList.add(errorMap);

        var errors = new HashMap<String, Object>();
        errors.put("errors", errorList);

        return ResponseEntity.status(status).body(errors);
    }
}

