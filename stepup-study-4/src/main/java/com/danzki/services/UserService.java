package com.danzki.services;

import com.danzki.model.User;
import com.danzki.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private UsersRepo usersRepo;

    @Autowired
    public UserService(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    public Iterable<User> findAll() {
        return usersRepo.findAll();
    }

    public void save(User user) {
        usersRepo.save(user);
    }

    public User findByUsername(String username) {
        return usersRepo.findByUsername(username);
    }
}
