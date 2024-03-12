package com.danzki.repository;

import com.danzki.model.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountTypeRepo extends JpaRepository<AccountType, Long> {
   Optional<AccountType> findById(Long var1);

   Optional<AccountType> findByValue(String var1);
}
