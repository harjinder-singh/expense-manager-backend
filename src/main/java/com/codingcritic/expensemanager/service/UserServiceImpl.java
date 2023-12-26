package com.codingcritic.expensemanager.service;

import com.codingcritic.expensemanager.model.AppUser;
import com.codingcritic.expensemanager.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public AppUser saveUser(AppUser appUser) {
        log.info("Saving new user {}", appUser.getEmail());
        return userRepository.save(appUser);
    }

    @Override
    public AppUser getUser(String email) {
        log.info("Fetching user with email {}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    public List<AppUser> getUsers() {
        log.info("Getting list of users");
        return userRepository.findAll();
    }
}

