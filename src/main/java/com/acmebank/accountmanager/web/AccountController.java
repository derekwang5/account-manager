package com.acmebank.accountmanager.web;

import com.acmebank.accountmanager.business.service.AccountNotFoundException;
import com.acmebank.accountmanager.business.service.AccountService;
import com.acmebank.accountmanager.business.service.NotEnoughBalanceException;
import com.acmebank.accountmanager.data.entity.AccountBalanceResponse;
import com.acmebank.accountmanager.data.entity.AccountTransferRequest;
import com.acmebank.accountmanager.data.entity.AccountTransferResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/account")
public class AccountController {

    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/balance/{accountNumber}/{currency}")
    public AccountBalanceResponse getAccountBalance(@PathVariable(value = "accountNumber") String accountNumber, @PathVariable(value = "currency") String currency) {
        try {
            return this.accountService.getBalance(accountNumber, currency);
        } catch (AccountNotFoundException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Account Not Found", ex);
        }
    }

    @PostMapping("/transfer")
    public AccountTransferResponse transfer(@Valid @RequestBody AccountTransferRequest request) {
        validateTransferRequest(request);
        try {
            return accountService.transfer(request.getFromAccountNumber(), request.getToAccountNumber(), request.getCurrency(), request.getAmount());
        } catch (AccountNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Account Not Found: " + e.getAccountNumber() + "/" + e.getCurrency(), e);
        } catch (NotEnoughBalanceException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Source account does not have enough balance", e);
        }
    }

    private void validateTransferRequest(AccountTransferRequest request) {
        String errorMessage = null;
        if (request.getFromAccountNumber().equals(request.getToAccountNumber())) {
            errorMessage = "Source and destination accounts are same";
        }

        if (errorMessage != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, errorMessage, new InvalidTransferRequestException(request));
        }
    }
}
