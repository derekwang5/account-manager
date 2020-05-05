package com.acmebank.accountmanager.business.service;

import com.acmebank.accountmanager.data.entity.Account;
import com.acmebank.accountmanager.data.entity.AccountBalanceResponse;
import com.acmebank.accountmanager.data.entity.AccountTransferResponse;
import com.acmebank.accountmanager.data.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account getAccount(String accountNumber, String currency) throws AccountNotFoundException {
        Account account = accountRepository.findByAccountNumberAndCurrency(accountNumber, currency);
        if (account == null) {
            throw new AccountNotFoundException(accountNumber, currency);
        }
        return account;
    }

    @Override
    public AccountBalanceResponse getBalance(String accountNumber, String currency) throws AccountNotFoundException {
        BigDecimal balance = getAccount(accountNumber, currency).getBalance();
        return new AccountBalanceResponse(accountNumber, currency, balance);
    }

    @Override
    public AccountTransferResponse transfer(String fromAccountNumber, String toAccountNumber, String currency, BigDecimal amount) {
        Account fromAccount = accountRepository.findByAccountNumberAndCurrency(fromAccountNumber, currency);
        Account toAccount = accountRepository.findByAccountNumberAndCurrency(toAccountNumber, currency);
        AccountTransferResponse result = null;
        if (hasEnoughBalance(fromAccount, amount)) {
            fromAccount.setBalance(fromAccount.getBalance().subtract(amount).setScale(2));
            toAccount.setBalance(toAccount.getBalance().add(amount).setScale(2));
            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
            result = new AccountTransferResponse(new AccountBalanceResponse(fromAccount), new AccountBalanceResponse(toAccount));
        } else {
            throw new NotEnoughBalanceException(fromAccountNumber, currency, fromAccount.getBalance(), amount);
        }
        return result;
    }

    @Override
    public boolean hasEnoughBalance(Account account, BigDecimal transferAmount) {
        return account.getBalance().compareTo(transferAmount) >= 0;
    }
}
