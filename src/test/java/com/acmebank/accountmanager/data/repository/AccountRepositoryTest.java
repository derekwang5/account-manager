package com.acmebank.accountmanager.data.repository;

import com.acmebank.accountmanager.data.entity.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository repository;

    @Test
    public void findAccountByAccountNumberAndCurrencyShouldReturnAccount() {
        Account account = repository.findByAccountNumberAndCurrency("33333333", "HKD");
        assertThat(account.getAccountNumber()).isEqualTo("33333333");
        assertThat(account.getCurrency()).isEqualTo("HKD");
        assertThat(account.getBalance()).isEqualTo(new BigDecimal("1000000.00"));
    }

    @Test
    public void findAccountByAccountNumberAndCurrencyWhenNoAccountShouldReturnNull() {
        Account account = repository.findByAccountNumberAndCurrency("99999999", "HKD");
        assertThat(account).isNull();
    }

}