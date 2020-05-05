package com.acmebank.accountmanager.data.entity;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AccountTransferRequest {

    @NotNull
    private String fromAccountNumber;

    @NotNull
    private String toAccountNumber;

    @NotNull
    private String currency;

    @NotNull
    @DecimalMin(value = "0.01", inclusive = true)
    @Digits(integer = 20, fraction = 2)
    private BigDecimal amount;

    public AccountTransferRequest(String fromAccountNumber, String toAccountNumber, String currency, BigDecimal amount) {
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.currency = currency;
        this.amount = amount;
    }

    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    public void setFromAccountNumber(String fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
