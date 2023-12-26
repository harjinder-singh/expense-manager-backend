package com.codingcritic.expensemanager.repository;

import com.codingcritic.expensemanager.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByEmail(String email);
}
