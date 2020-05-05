package com.acmebank.accountmanager;

import com.acmebank.accountmanager.business.service.AccountService;
import com.acmebank.accountmanager.data.entity.AccountBalanceResponse;
import com.acmebank.accountmanager.data.entity.AccountTransferResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class ApplicationIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testGetBalance() throws Exception {
		ResponseEntity<AccountBalanceResponse> response = restTemplate.getForEntity("/api/account/balance/{accountNumber}/{currency}", AccountBalanceResponse.class, "33333333", "HKD");
		AccountBalanceResponse responseObject = response.getBody();
		assertThat(responseObject.getAccountNumber()).isEqualTo("33333333");
		assertThat(responseObject.getCurrency()).isEqualTo("HKD");
		assertThat(responseObject.getBalance()).isEqualTo("1000000.00");
	}

	@Test
	public void testTransfer() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JSONObject requestObject = new JSONObject();
		requestObject.put("fromAccountNumber", "33333333");
		requestObject.put("toAccountNumber", "44444444");
		requestObject.put("currency", "HKD");
		requestObject.put("amount", "300000");
		HttpEntity<String> request =
				new HttpEntity<String>(requestObject.toString(), headers);

		ResponseEntity<AccountTransferResponse> response = restTemplate.postForEntity("/api/account/transfer", request, AccountTransferResponse.class);

		// Check the return object
		AccountTransferResponse responseObject = response.getBody();
		assertThat(responseObject.getFromAccount().getAccountNumber()).isEqualTo("33333333");
		assertThat(responseObject.getFromAccount().getCurrency()).isEqualTo("HKD");
		assertThat(responseObject.getFromAccount().getBalance()).isEqualTo(new BigDecimal("700000.00"));
		assertThat(responseObject.getToAccount().getAccountNumber()).isEqualTo("44444444");
		assertThat(responseObject.getToAccount().getCurrency()).isEqualTo("HKD");
		assertThat(responseObject.getToAccount().getBalance()).isEqualTo(new BigDecimal("1300000.00"));

		// Check the values are updated in the database
		ResponseEntity<AccountBalanceResponse> balResponse = restTemplate.getForEntity("/api/account/balance/{accountNumber}/{currency}", AccountBalanceResponse.class, "33333333", "HKD");
		AccountBalanceResponse balResponseObj = balResponse.getBody();
		assertThat(balResponseObj.getAccountNumber()).isEqualTo("33333333");
		assertThat(balResponseObj.getCurrency()).isEqualTo("HKD");
		assertThat(balResponseObj.getBalance()).isEqualTo("700000.00");

		balResponse = restTemplate.getForEntity("/api/account/balance/{accountNumber}/{currency}", AccountBalanceResponse.class, "44444444", "HKD");
		balResponseObj = balResponse.getBody();
		assertThat(balResponseObj.getAccountNumber()).isEqualTo("44444444");
		assertThat(balResponseObj.getCurrency()).isEqualTo("HKD");
		assertThat(balResponseObj.getBalance()).isEqualTo("1300000.00");
	}

}
