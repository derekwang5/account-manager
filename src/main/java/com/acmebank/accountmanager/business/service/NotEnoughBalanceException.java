package com.acmebank.accountmanager.business.service;

import java.math.BigDecimal;

public class NotEnoughBalanceException extends RuntimeException {

    private String fromAccountNumber;
    private String currency;
    private BigDecimal existingBalance;
    private BigDecimal transferAmount;

    public NotEnoughBalanceException(String fromAccountNumber, String currency, BigDecimal existingBalance, BigDecimal transferAmount) {
        this.fromAccountNumber = fromAccountNumber;
        this.currency = currency;
        this.existingBalance = existingBalance;
        this.transferAmount = transferAmount;
    }

    public NotEnoughBalanceException(Throwable cause, String fromAccountNumber, String currency, BigDecimal existingBalance, BigDecimal transferAmount) {
        super(cause);
        this.fromAccountNumber = fromAccountNumber;
        this.currency = currency;
        this.existingBalance = existingBalance;
        this.transferAmount = transferAmount;
    }

    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getExistingBalance() {
        return existingBalance;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }
}
