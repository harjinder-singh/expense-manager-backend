package com.codingcritic.expensemanager.repository;

import com.codingcritic.expensemanager.model.Account;
import com.codingcritic.expensemanager.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
