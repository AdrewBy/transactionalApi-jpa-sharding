package com.ustsinau.transactionapi.exception;

public class UuidInvalidException extends ApiException{

    public UuidInvalidException(String message, String errorCode) {
        super(message, errorCode);
    }
}
