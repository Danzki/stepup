package com.danzki.services;

import com.danzki.model.Login;
import com.danzki.repo.LoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginsService {
    private LoginRepo loginRepo;

    @Autowired
    public LoginsService(LoginRepo loginRepo) {
        this.loginRepo = loginRepo;
    }

    public Iterable<Login> findAll() {
        return loginRepo.findAll();
    }

    public void save(Login login) {
        loginRepo.save(login);
    }
}
