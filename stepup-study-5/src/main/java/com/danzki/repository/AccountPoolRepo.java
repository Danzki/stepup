package com.danzki.repository;

import com.danzki.model.AccountPool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountPoolRepo extends JpaRepository<AccountPool, Long> {
   AccountPool findByBranchCodeAndCurrencyCodeAndMdmCodeAndPriorityCodeAndRegistryTypeCode(String var1, String var2, String var3, String var4, String var5);
}
