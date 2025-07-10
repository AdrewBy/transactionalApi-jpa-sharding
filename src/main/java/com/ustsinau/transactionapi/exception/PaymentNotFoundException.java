package com.ustsinau.transactionapi.exception;

public class PaymentNotFoundException extends ApiException{

    public PaymentNotFoundException(String message, String errorCode) {

        super(message, errorCode);
    }
}
