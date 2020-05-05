package com.acmebank.accountmanager.data.entity;

public class AccountTransferResponse {

    private AccountBalanceResponse fromAccount;
    private AccountBalanceResponse toAccount;

    public AccountTransferResponse(AccountBalanceResponse fromAccount, AccountBalanceResponse toAccount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }

    public AccountBalanceResponse getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(AccountBalanceResponse fromAccount) {
        this.fromAccount = fromAccount;
    }

    public AccountBalanceResponse getToAccount() {
        return toAccount;
    }

    public void setToAccount(AccountBalanceResponse toAccount) {
        this.toAccount = toAccount;
    }
}
