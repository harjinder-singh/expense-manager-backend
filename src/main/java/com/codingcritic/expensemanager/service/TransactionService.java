package com.codingcritic.expensemanager.service;

import com.codingcritic.expensemanager.model.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction saveTransaction(Long accountId,
                                Transaction transaction) throws  Exception;
    List<Transaction> getTransactions(Long accountId) throws Exception;
}
