package com.codingcritic.expensemanager.service;

import com.codingcritic.expensemanager.model.Account;

import java.util.List;

public interface AccountService {

    Account saveAccount(Long userId, Account accountRequest) throws Exception;

    List<Account> getAccounts();
}
