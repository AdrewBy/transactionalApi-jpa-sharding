package com.ustsinau.transactionapi.exception;

public class TransferFailedException extends ApiException{

    public TransferFailedException(String message, String errorCode) {

        super(message, errorCode);
    }
}
