package com.danzki.repository;

import com.danzki.model.Account;
import com.danzki.model.AccountPool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {
   Account findFirstByAccountPool(AccountPool var1);
}
