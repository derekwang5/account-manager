package com.acmebank.accountmanager;

import com.acmebank.accountmanager.data.entity.Account;
import com.acmebank.accountmanager.data.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataLoader implements ApplicationRunner {

    private AccountRepository accountRepository;

    @Autowired
    public DataLoader(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void run(ApplicationArguments args) {
        Account firstAccount = accountRepository.findByAccountNumberAndCurrency("12345678", "HKD");
        Account secondAccount = accountRepository.findByAccountNumberAndCurrency("88888888", "HKD");
        if (firstAccount == null) {
            accountRepository.save(new Account("12345678", "HKD", BigDecimal.valueOf(1000000)));
        }
        if (secondAccount == null) {
            accountRepository.save(new Account("88888888", "HKD", BigDecimal.valueOf(1000000)));
        }
    }
}
