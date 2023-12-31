package com.codingcritic.expensemanager.service;

import com.codingcritic.expensemanager.model.Account;
import com.codingcritic.expensemanager.model.AppUser;
import com.codingcritic.expensemanager.model.Transaction;
import com.codingcritic.expensemanager.repository.AccountRepository;
import com.codingcritic.expensemanager.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AccountServiceImpl implements AccountService{
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    @Override
    public Account saveAccount(Long userId, Account accountRequest) throws Exception {
        return userRepository.findById(userId).map(user -> {
            user.getAccounts().add(accountRequest);
            return accountRepository.save(accountRequest);
        }).orElseThrow(() -> new Exception("Not found User with id = " + userId));
    }

    @Override
    public List<Account> getAccounts(Long userId) throws Exception {
        log.info("Getting list of accounts");
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("Not found User with id = " + userId));

        List<Account> accounts = new ArrayList<Account>();
        accounts.addAll(user.getAccounts());
        return accounts;
    }
}
