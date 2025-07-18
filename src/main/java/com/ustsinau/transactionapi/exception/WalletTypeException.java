package com.ustsinau.transactionapi.exception;

public class WalletTypeException extends ApiException{

    public WalletTypeException(String message, String errorCode) {

        super(message, errorCode);
    }
}