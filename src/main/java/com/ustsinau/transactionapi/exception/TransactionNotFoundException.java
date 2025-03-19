package com.ustsinau.transactionapi.exception;

public class TransactionNotFoundException extends ApiException{

    public TransactionNotFoundException(String message, String errorCode) {

        super(message, errorCode);
    }
}
