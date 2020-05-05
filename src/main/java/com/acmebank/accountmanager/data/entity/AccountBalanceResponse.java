package com.acmebank.accountmanager.data.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class AccountBalanceResponse {

    private String accountNumber;
    private String currency;
    private BigDecimal balance;

    public AccountBalanceResponse(String accountNumber, String currency, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.balance = balance;
    }

    public AccountBalanceResponse(Account account) {
        this.accountNumber = account.getAccountNumber();
        this.currency = account.getCurrency();
        this.balance = account.getBalance();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountBalanceResponse that = (AccountBalanceResponse) o;
        return Objects.equals(accountNumber, that.accountNumber) &&
                Objects.equals(currency, that.currency) &&
                Objects.equals(balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, currency, balance);
    }
}
