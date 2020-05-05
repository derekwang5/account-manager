package com.acmebank.accountmanager.business.service;

import com.acmebank.accountmanager.data.entity.Account;
import com.acmebank.accountmanager.data.entity.AccountBalanceResponse;
import com.acmebank.accountmanager.data.entity.AccountTransferResponse;

import java.math.BigDecimal;

public interface AccountService {

    public Account getAccount(String accountNumber, String currency) throws AccountNotFoundException;

    public AccountBalanceResponse getBalance(String accountNumber, String currency) throws AccountNotFoundException;

    public AccountTransferResponse transfer(String fromAccountNumber, String toAccountNumber, String currency, BigDecimal amount) throws AccountNotFoundException, NotEnoughBalanceException;

    public boolean hasEnoughBalance(Account account, BigDecimal transferAmount) throws NotEnoughBalanceException;

}
