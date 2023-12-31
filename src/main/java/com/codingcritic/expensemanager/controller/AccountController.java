package com.codingcritic.expensemanager.controller;

import com.codingcritic.expensemanager.model.Account;
import com.codingcritic.expensemanager.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    @GetMapping("/accounts")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<List<Account>> getAccounts(){
        return ResponseEntity.ok().body(accountService.getAccounts());
    }

    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@PathVariable("userId") Long userId,
                                                 @RequestBody Account account) throws Exception {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().toUriString());
        return ResponseEntity.created(uri).body(accountService.saveAccount(userId, account));
    }
}
