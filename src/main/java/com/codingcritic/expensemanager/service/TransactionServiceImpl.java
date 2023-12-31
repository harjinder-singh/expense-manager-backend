package com.codingcritic.expensemanager.service;

import com.codingcritic.expensemanager.model.Account;
import com.codingcritic.expensemanager.model.Transaction;
import com.codingcritic.expensemanager.model.TransactionType;
import com.codingcritic.expensemanager.repository.AccountRepository;
import com.codingcritic.expensemanager.repository.TransactionRepository;
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
public class TransactionServiceImpl implements TransactionService{
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    @Override
    public Transaction saveTransaction(Long accountId,
                                       Transaction transactionRequest) throws Exception {
        return accountRepository.findById(accountId).map(account -> {
            account.getTransactions().add(transactionRequest);
            TransactionType transactionType = transactionRequest.getTransactionType();
            if(transactionType == TransactionType.DEBIT){
                account.setBalance(account.getBalance() - transactionRequest.getAmount());
            }else if(transactionType == TransactionType.CREDIT){
                account.setBalance(account.getBalance() + transactionRequest.getAmount());
            }
            return transactionRepository.save(transactionRequest);
        }).orElseThrow(() -> new Exception("Not found Account with id = " + accountId));

    }

    @Override
    public List<Transaction> getTransactions(Long accountId) throws Exception {
        log.info("Getting list of transactions");
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new Exception("Not found Account with id = " + accountId));

        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.addAll(account.getTransactions());
        return transactions;
    }

}
