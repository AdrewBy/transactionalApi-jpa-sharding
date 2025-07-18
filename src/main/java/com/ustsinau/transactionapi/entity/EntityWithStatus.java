package com.ustsinau.transactionapi.entity;

public interface EntityWithStatus<T> {
    void setStatus(T status);

}