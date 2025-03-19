package com.ustsinau.transactionapi.exception;

public class InsufficientFundsException extends ApiException{

    public InsufficientFundsException(String message, String errorCode) {

        super(message, errorCode);
    }
}
