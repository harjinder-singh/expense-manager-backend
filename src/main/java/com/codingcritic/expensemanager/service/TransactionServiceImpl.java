package com.codingcritic.expensemanager.service;

import com.codingcritic.expensemanager.model.Account;
import com.codingcritic.expensemanager.model.Transaction;
import com.codingcritic.expensemanager.repository.AccountRepository;
import com.codingcritic.expensemanager.repository.TransactionRepository;
import com.codingcritic.expensemanager.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TransactionServiceImpl implements TransactionService{
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    @Override
    public Transaction saveTransaction(Long accountId,
                                       Transaction transactionRequest) throws Exception {
        Transaction transaction = accountRepository.findById(accountId).map(account -> {
            account.getTransactions().add(transactionRequest);
            return transactionRepository.save(transactionRequest);
        }).orElseThrow(() -> new Exception("Not found Account with id = " + accountId));
        return transaction;
    }

    @Override
    public List<Transaction> getTransactions() {
        log.info("Getting list of transactions");
        return transactionRepository.findAll();
    }
}
