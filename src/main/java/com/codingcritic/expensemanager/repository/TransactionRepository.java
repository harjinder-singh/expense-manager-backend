package com.codingcritic.expensemanager.repository;

import com.codingcritic.expensemanager.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
