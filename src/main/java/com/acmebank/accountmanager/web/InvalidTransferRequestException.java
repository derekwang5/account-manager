package com.acmebank.accountmanager.web;

import com.acmebank.accountmanager.data.entity.AccountTransferRequest;

public class InvalidTransferRequestException extends RuntimeException {

    private AccountTransferRequest request;

    public InvalidTransferRequestException(AccountTransferRequest request) {
        this.request = request;
    }

    public InvalidTransferRequestException(String message, AccountTransferRequest request) {
        super(message);
        this.request = request;
    }

    public AccountTransferRequest getRequest() {
        return request;
    }
}
