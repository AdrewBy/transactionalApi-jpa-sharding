package com.ustsinau.transactionapi.exception;

public class WalletNotFoundException extends ApiException{

    public WalletNotFoundException(String message, String errorCode) {

        super(message, errorCode);
    }
}
