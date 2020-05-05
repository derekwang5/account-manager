package com.acmebank.accountmanager.business.service;

import com.acmebank.accountmanager.data.entity.Account;
import com.acmebank.accountmanager.data.entity.AccountBalanceResponse;
import com.acmebank.accountmanager.data.entity.AccountTransferResponse;
import com.acmebank.accountmanager.data.repository.AccountRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

public class AccountServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private AccountRepository accountRepository;

    private AccountService accountService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.accountService = new AccountServiceImpl((accountRepository));
    }

    @Test
    public void getBalanceWhenAccountNumberAndCurrencyNotFoundShouldThrowException() {
        given(this.accountRepository.findByAccountNumberAndCurrency(anyString(), anyString())).willReturn(null);
        this.thrown.expect(AccountNotFoundException.class);
        this.accountService.getBalance("01234567", "HKD");
    }

    @Test
    public void getBalanceShouldReturnBalance() {
        Account account = new Account("12345678", "HKD", BigDecimal.TEN);
        given(this.accountRepository.findByAccountNumberAndCurrency(anyString(), anyString())).willReturn(account);
        AccountBalanceResponse balanceResult = new AccountBalanceResponse("12345678", "HKD", BigDecimal.TEN);
        AccountBalanceResponse actualResult = this.accountService.getBalance("12345678", "HKD");
        assertThat(actualResult).isEqualTo(balanceResult);
    }

    @Test
    public void hasEnoughBalanceWhenHasEnoughBalanceShouldReturnTrue() {
        Account account = new Account("12345678", "HKD", BigDecimal.TEN);
        given(this.accountRepository.findByAccountNumberAndCurrency(anyString(), anyString())).willReturn(account);
        boolean hasEnoughBalance = accountService.hasEnoughBalance(account, BigDecimal.ONE);
        assertThat(hasEnoughBalance).isTrue();
    }

    @Test
    public void hasEnoughBalanceWhenHasNotEnoughBalanceShouldReturnFalse() {
        Account account = new Account("12345678", "HKD", BigDecimal.TEN);
        given(this.accountRepository.findByAccountNumberAndCurrency(anyString(), anyString())).willReturn(account);
        boolean hasEnoughBalance = accountService.hasEnoughBalance(account, BigDecimal.valueOf(20));
        assertThat(hasEnoughBalance).isFalse();
    }

    @Test
    public void transferWhenHasNotEnoughBalanceShouldThrowException() {
        Account from = new Account("11111111", "HKD", BigDecimal.valueOf(100));
        Account to = new Account("22222222", "HKD", BigDecimal.valueOf(50));
        given(this.accountRepository.findByAccountNumberAndCurrency("11111111", "HKD")).willReturn(from);
        given(this.accountRepository.findByAccountNumberAndCurrency("22222222", "HKD")).willReturn(to);
        this.thrown.expect(NotEnoughBalanceException.class);
        accountService.transfer("11111111", "22222222", "HKD", BigDecimal.valueOf(200));
    }

    @Test
    public void transferWhenHasEnoughBalanceShouldReturnResult() {
        Account from = new Account("11111111", "HKD", new BigDecimal("100.00"));
        Account to = new Account("22222222", "HKD", new BigDecimal("50.00"));
        given(this.accountRepository.findByAccountNumberAndCurrency("11111111", "HKD")).willReturn(from);
        given(this.accountRepository.findByAccountNumberAndCurrency("22222222", "HKD")).willReturn(to);
        AccountTransferResponse result = accountService.transfer("11111111", "22222222", "HKD", new BigDecimal("30.00"));
        assertThat(result.getFromAccount().getAccountNumber()).isEqualTo("11111111");
        assertThat(result.getFromAccount().getCurrency()).isEqualTo("HKD");
        assertThat(result.getFromAccount().getBalance()).isEqualTo(new BigDecimal("70.00"));
        assertThat(result.getToAccount().getAccountNumber()).isEqualTo("22222222");
        assertThat(result.getToAccount().getCurrency()).isEqualTo("HKD");
        assertThat(result.getToAccount().getBalance()).isEqualTo(new BigDecimal("80.00"));
    }

}