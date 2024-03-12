package com.danzki.service;

import com.danzki.model.AccountType;
import com.danzki.repository.AccountTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AccountTypeService {
   private final AccountTypeRepo accountTypeRepo;

   @Autowired
   public AccountTypeService(AccountTypeRepo accountTypeRepo) {
      this.accountTypeRepo = accountTypeRepo;
   }

   public Optional<AccountType> findByValue(String value) {
      return this.accountTypeRepo.findByValue(value);
   }
}
