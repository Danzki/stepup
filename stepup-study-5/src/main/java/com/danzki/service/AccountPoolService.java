package com.danzki.service;

import com.danzki.ErrorMessage;
import com.danzki.model.Account;
import com.danzki.model.AccountPool;
import com.danzki.repository.AccountPoolRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountPoolService implements ErrorMessage {
   private final AccountPoolRepo accountPoolRepo;
   private final AccountService accountService;

   @Autowired
   public AccountPoolService(AccountPoolRepo accountPoolRepo, AccountService accountService) {
      this.accountPoolRepo = accountPoolRepo;
      this.accountService = accountService;
   }

   public AccountPool findByBranchCodeAndCurrencyCodeAndMdmCodeAndPriorityCodeAndRegistryTypeCode(String branchCode, String currencyCode, String mdmCode, String priorityCode, String registryTypeCode) {
      return this.accountPoolRepo.findByBranchCodeAndCurrencyCodeAndMdmCodeAndPriorityCodeAndRegistryTypeCode(branchCode, currencyCode, mdmCode, priorityCode, registryTypeCode);
   }

   public Account getAccountFromPool(String branchCode, String currencyCode, String mdmCode, String priorityCode, String registryTypeCode) {
      if (!registryTypeCode.equals("")) {
         AccountPool accountPool = this.findByBranchCodeAndCurrencyCodeAndMdmCodeAndPriorityCodeAndRegistryTypeCode(branchCode, currencyCode, mdmCode, priorityCode, registryTypeCode);
         if (accountPool != null) {
            return this.accountService.findFirstByAccountPool(accountPool);
         }
      }

      return null;
   }

   public String getAccountNotFoundMessage(String branchCode, String currencyCode, String mdmCode, String priorityCode, String registryTypeCode) {
      return ACCOUNT_NOT_FOUND.replaceAll(POOL_PARAMS, branchCode + "," + currencyCode + "," + mdmCode + "," + priorityCode + "," + registryTypeCode);
   }
}
