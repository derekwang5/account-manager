package com.acmebank.accountmanager.business.service;

public class AccountNotFoundException extends RuntimeException {

    private String accountNumber;
    private String currency;

    public AccountNotFoundException(String accountNumber, String currency) {
        this.accountNumber = accountNumber;
        this.currency = currency;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCurrency() {
        return currency;
    }
}
