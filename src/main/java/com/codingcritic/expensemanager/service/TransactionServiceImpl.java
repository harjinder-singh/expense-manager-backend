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
import java.util.Properties;
import java.util.Map;
import java.util.HashMap;
import java.util.Calendar;

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

    @Override
    public Properties getTransactionsForChart(Long accountId) throws Exception {
        log.info("Getting list of transactions");
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new Exception("Not found Account with id = " + accountId));

        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.addAll(account.getTransactions());
        Properties jo = getJsonForChart(transactions);
        return jo;
    }

    private Properties getJsonForChart(List<Transaction> transactions){
        Properties object = new Properties();
        for(Transaction transaction : transactions) {
            Map<String, String> hm = new HashMap<>();
            hm.put("0", "January");
            hm.put("1", "Febraury");
            hm.put("2", "March");
            hm.put("3", "April");
            hm.put("4", "May");
            hm.put("5", "June");
            hm.put("6", "July");
            hm.put("7", "August");
            hm.put("8", "September");
            hm.put("9", "October");
            hm.put("10", "November");
            hm.put("11","December");

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(transaction.getTransactionDate());
            if(object.containsKey(transaction.getTransactionSubType().toString())){
                Properties ob = (Properties) object.get(transaction.getTransactionSubType().toString());
                if(ob != null && !ob.isEmpty() && ob.containsKey(hm.get(Integer.toString(calendar.get(Calendar.MONTH))))){
                    System.out.println(ob);
                    System.out.println(ob.get(hm.get(Integer.toString(calendar.get(Calendar.MONTH)))));
                    Double currentAmt = Double.valueOf(ob.get(hm.get(Integer.toString(calendar.get(Calendar.MONTH)))).toString());
                    System.out.println(currentAmt);
                    Double newAmt = currentAmt + (Double) transaction.getAmount();
                    ob.put(hm.get(Integer.toString(calendar.get(Calendar.MONTH))), String.format("%.2f", newAmt));
                    object.put(transaction.getTransactionSubType().toString(), ob);
                }else{
                    Properties ob1 = new Properties();
                    ob1.put(hm.get(Integer.toString(calendar.get(Calendar.MONTH))), String.format("%.2f", transaction.getAmount()));
                    object.put(transaction.getTransactionSubType().toString(), ob1);
                }
            }else{
                Properties ob = new Properties();
                ob.put(hm.get(Integer.toString(calendar.get(Calendar.MONTH))), String.format("%.2f", transaction.getAmount()));
                object.put(transaction.getTransactionSubType().toString(), ob);
            }
        }
        return object;
    }

}
