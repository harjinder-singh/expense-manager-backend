package com.codingcritic.expensemanager.service;

import com.codingcritic.expensemanager.model.Transaction;

import java.util.List;
import java.util.Properties;

import org.springframework.web.multipart.MultipartFile;

public interface TransactionService {
    Transaction saveTransaction(Long accountId,
                                Transaction transaction) throws  Exception;
    List<Transaction> getTransactions(Long accountId) throws Exception;

    Properties getTransactionsForChart(Long accountId) throws Exception;

    void saveTransactionsFromCSV(Long accountId, MultipartFile file) throws Exception;
}
