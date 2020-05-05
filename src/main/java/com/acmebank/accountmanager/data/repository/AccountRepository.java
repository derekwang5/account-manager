package com.acmebank.accountmanager.data.repository;

import com.acmebank.accountmanager.data.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    Account findByAccountNumberAndCurrency(String accountNumber, String currency);
}
