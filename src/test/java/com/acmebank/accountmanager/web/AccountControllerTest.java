package com.acmebank.accountmanager.web;

import com.acmebank.accountmanager.business.service.AccountNotFoundException;
import com.acmebank.accountmanager.business.service.AccountService;
import com.acmebank.accountmanager.business.service.NotEnoughBalanceException;
import com.acmebank.accountmanager.data.entity.AccountBalanceResponse;
import com.acmebank.accountmanager.data.entity.AccountTransferRequest;
import com.acmebank.accountmanager.data.entity.AccountTransferResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @MockBean
	private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAccountBalanceShouldReturnJson() throws Exception {
        AccountBalanceResponse result = new AccountBalanceResponse("12345678", "HKD", BigDecimal.TEN);

        given(accountService.getBalance("12345678", "HKD")).willReturn(result);
        mockMvc.perform(get("/api/account/balance/12345678/HKD"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'accountNumber': '12345678','currency': 'HKD','balance': 10.00}"));
    }

    @Test
    public void getAccountBalanceWhenAccountNotFoundShouldReturnNotFound() throws Exception {
        given(accountService.getBalance("12345678", "HKD")).willThrow(new AccountNotFoundException("12345678", "HKD"));
        mockMvc.perform(get("/api/account/balance/12345678/HKD"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void transferShouldReturnJson() throws Exception {
        AccountBalanceResponse fromAccountResult = new AccountBalanceResponse("11111111", "HKD", BigDecimal.TEN);
        AccountBalanceResponse toAccountResult = new AccountBalanceResponse("22222222", "HKD", BigDecimal.TEN);
        given(accountService.transfer("11111111", "22222222", "HKD", new BigDecimal("100.00")))
                .willReturn(new AccountTransferResponse(fromAccountResult, toAccountResult));
        AccountTransferRequest request = new AccountTransferRequest("11111111", "22222222", "HKD", new BigDecimal("100.00"));
        mockMvc.perform(post("/api/account/transfer").contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json("{'fromAccount': {'accountNumber': '11111111','currency': 'HKD','balance': 10.00}, 'toAccount': {'accountNumber': '22222222','currency': 'HKD','balance': 10.00}}"));
    }

    @Test
    public void transferWhenNotEnoughBalanceShouldReturnError() throws Exception {
        given(accountService.transfer("11111111", "22222222", "HKD", new BigDecimal("100.00")))
                .willThrow(new NotEnoughBalanceException("11111111", "HKD", new BigDecimal(50.00), new BigDecimal("100.00")));
        AccountTransferRequest request = new AccountTransferRequest("11111111", "22222222", "HKD", new BigDecimal("100.00"));
        MvcResult mvcResult = mockMvc.perform(post("/api/account/transfer").contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        assertThat(errorMessage).contains("Source account does not have enough balance");
    }

    @Test
    public void transferWhenNoAccountFoundShouldReturnError() throws Exception {
        given(accountService.transfer("11111111", "22222222", "HKD", new BigDecimal("100.00")))
                .willThrow(new AccountNotFoundException("11111111", "HKD"));
        AccountTransferRequest request = new AccountTransferRequest("11111111", "22222222", "HKD", new BigDecimal("100.00"));
        MvcResult mvcResult = mockMvc.perform(post("/api/account/transfer").contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
                .andExpect(status().isNotFound())
                .andReturn();
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        assertThat(errorMessage).contains("Account Not Found");
    }

    @Test
    public void transferWhenSourceAccountIsNullShouldReturnError() throws Exception {
        AccountTransferRequest request = new AccountTransferRequest(null, "22222222", "HKD", new BigDecimal("100.00"));
        MvcResult mvcResult = mockMvc.perform(post("/api/account/transfer").contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void transferWhenDestinationAccountIsNullShouldReturnError() throws Exception {
        AccountTransferRequest request = new AccountTransferRequest("11111111", null, "HKD", new BigDecimal("100.00"));
        MvcResult mvcResult = mockMvc.perform(post("/api/account/transfer").contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void transferWhenCurrencyIsNullShouldReturnError() throws Exception {
        AccountTransferRequest request = new AccountTransferRequest("11111111", "22222222", null, new BigDecimal("100.00"));
        MvcResult mvcResult = mockMvc.perform(post("/api/account/transfer").contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void transferWhenAmountIsNullShouldReturnError() throws Exception {
        AccountTransferRequest request = new AccountTransferRequest("11111111", "22222222", "HKD", null);
        MvcResult mvcResult = mockMvc.perform(post("/api/account/transfer").contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void transferWhenAmountIsNegativeShouldReturnError() throws Exception {
        AccountTransferRequest request = new AccountTransferRequest("11111111", "22222222", "HKD", new BigDecimal("-100.00"));
        MvcResult mvcResult = mockMvc.perform(post("/api/account/transfer").contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void transferWhenAmountHasThreeDecimalPlacesShouldReturnError() throws Exception {
        AccountTransferRequest request = new AccountTransferRequest("11111111", "22222222", "HKD", new BigDecimal("100.123"));
        MvcResult mvcResult = mockMvc.perform(post("/api/account/transfer").contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void transferWhenSourceAccountIsSameAsDestinationAccountShouldReturnError() throws Exception {
        AccountTransferRequest request = new AccountTransferRequest("22222222", "22222222", "HKD", new BigDecimal("100.00"));
        MvcResult mvcResult = mockMvc.perform(post("/api/account/transfer").contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        assertThat(errorMessage).contains("Source and destination accounts are same");
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}