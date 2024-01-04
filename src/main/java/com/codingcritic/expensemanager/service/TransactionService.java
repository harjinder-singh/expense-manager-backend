package com.codingcritic.expensemanager.service;

import com.codingcritic.expensemanager.model.Transaction;

import java.util.List;
import java.util.Properties;

public interface TransactionService {
    Transaction saveTransaction(Long accountId,
                                Transaction transaction) throws  Exception;
    List<Transaction> getTransactions(Long accountId) throws Exception;

    Properties getTransactionsForChart(Long accountId) throws Exception;
}
