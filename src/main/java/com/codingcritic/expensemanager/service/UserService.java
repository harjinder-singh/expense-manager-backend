package com.codingcritic.expensemanager.service;

import com.codingcritic.expensemanager.model.AppUser;

import java.util.List;

public interface UserService {
    AppUser saveUser(AppUser appUser);
    AppUser getUser(String email);
    List<AppUser> getUsers();
}
