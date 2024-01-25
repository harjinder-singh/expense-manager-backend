package com.codingcritic.expensemanager.controller;

import com.codingcritic.expensemanager.model.Transaction;
import com.codingcritic.expensemanager.service.TransactionService;
import com.codingcritic.expensemanager.helper.CSVHelper;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    @PostMapping("/uploadCSV")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> uploadFile(
            @PathVariable("accountId") Long accountId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("formatType") String format) {
        String message = "";
        System.out.println("Format: " + format);

        if (CSVHelper.hasCSVFormat(file)) {
            try {
                transactionService.saveTransactionsFromCSV(accountId, file, format);
                message = "Uploaded the file successfully: ";
                return ResponseEntity.status(HttpStatus.OK).body(message);
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "! " + e.getMessage();
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
            }
        }

        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
  }
}
