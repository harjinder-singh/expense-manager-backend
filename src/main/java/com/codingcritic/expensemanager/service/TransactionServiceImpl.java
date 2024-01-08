package com.codingcritic.expensemanager.service;

import com.codingcritic.expensemanager.model.Account;
import com.codingcritic.expensemanager.model.Transaction;
import com.codingcritic.expensemanager.model.TransactionType;
import com.codingcritic.expensemanager.helper.CSVHelper;
import com.codingcritic.expensemanager.repository.AccountRepository;
import com.codingcritic.expensemanager.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Map;
import java.util.Calendar;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Override
    public void saveTransactionsFromCSV(Long accountId, MultipartFile file) throws Exception{
        try {
            List<Transaction> transactions = CSVHelper.csvToTransactions(file.getInputStream());
            accountRepository.findById(accountId).map(account -> {
                double balance = getNewAmount(account.getBalance(), transactions);
                account.getTransactions().addAll(transactions);
                account.setBalance(Double.valueOf(String.format("%.2f", balance)));
                return account;
            });
        } catch (IOException e) {
            throw new Exception("Fail to store csv data: " + e.getMessage());
        }
    }

    private double getNewAmount(double oldBalance, List<Transaction> transactions){
        double balance = oldBalance;
        for(Transaction transaction: transactions){
            TransactionType transactionType = transaction.getTransactionType();
            if(transactionType == TransactionType.DEBIT){
                balance -= transaction.getAmount();
            }else if(transactionType == TransactionType.CREDIT){
                balance += transaction.getAmount();
            }
        }
        return balance;
    }

    private Properties getJsonForChart(List<Transaction> transactions){
        Properties object = new Properties();
        for(Transaction transaction : transactions) {
            Map<String, String> months = getMonths();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(transaction.getTransactionDate());
            TransactionType transactionType = transaction.getTransactionType();
            if(object.containsKey(transaction.getTransactionSubType().toString())){
                Properties ob = (Properties) object.get(transaction.getTransactionSubType().toString());
                System.out.println(ob.containsKey(months.get(Integer.toString(calendar.get(Calendar.MONTH)))));
                if(ob != null && !ob.isEmpty() && ob.containsKey(months.get(Integer.toString(calendar.get(Calendar.MONTH))))){
                    System.out.println("Inside if");
                    Double currentAmt = Double.valueOf(ob.get(months.get(Integer.toString(calendar.get(Calendar.MONTH)))).toString());
                    Double newAmt = currentAmt + (Double) transaction.getAmount();
                    ob.put(months.get(Integer.toString(calendar.get(Calendar.MONTH))), String.format("%.2f", newAmt));
                    object.put(transaction.getTransactionSubType().toString(), ob);
                }else{
                    System.out.println("Inside else");
                    ob.put(months.get(Integer.toString(calendar.get(Calendar.MONTH))), transaction.getAmount());
                    object.put(transaction.getTransactionSubType().toString(), ob);
                }
            }else{
                Properties ob = new Properties();
                ob.put(months.get(Integer.toString(calendar.get(Calendar.MONTH))), transaction.getAmount());
                object.put(transaction.getTransactionSubType().toString(), ob);
            }
            System.out.println(object);
        }
        return object;
    }

    private Map<String, String> getMonths(){
        return Stream.of(new Object[][] {
                {"0", "January"},
                {"1", "February"},
                {"2", "March"},
                {"3", "April"},
                {"4", "May"},
                {"5", "June"},
                {"6", "July"},
                {"7", "August"},
                {"8", "September"},
                {"9", "October"},
                {"10", "November"},
                {"11","December"}
        }).collect(Collectors.toMap(data -> (String) data[0], data -> (String) data[1]));
    }

}
