package com.codingcritic.expensemanager.repository;

import com.codingcritic.expensemanager.model.Account;
import com.codingcritic.expensemanager.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
