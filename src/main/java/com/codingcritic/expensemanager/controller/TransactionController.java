package com.codingcritic.expensemanager.controller;

import com.codingcritic.expensemanager.model.Transaction;
import com.codingcritic.expensemanager.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("/api/v1/accounts/{accountId}")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    @GetMapping("/transactions")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable("accountId") Long accountId) throws Exception{
        return ResponseEntity.ok().body(transactionService.getTransactions(accountId));
    }

    @PostMapping("/transactions")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Transaction> addTransaction(@PathVariable("accountId") Long accountId,
                                                  @RequestBody Transaction transaction) throws Exception{
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().toUriString());
        return ResponseEntity.created(uri).body(transactionService.saveTransaction(accountId, transaction));
    }

    @GetMapping("/transactionsForChart")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Properties> getTransactionsForChat(@PathVariable("accountId") Long accountId) throws Exception{
        return ResponseEntity.ok().body(transactionService.getTransactionsForChart(accountId));
    }
}
