package com.acmebank.accountmanager.data.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="ACCOUNT")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private long id;

    @Column(name="ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name="CURRENCY")
    private String currency;

    @Column(name="BALANCE")
    private BigDecimal balance;

    protected Account() {
    }

    public Account(String accountNumber, String currency, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
