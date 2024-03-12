package com.danzki.service;

import com.danzki.controller.ProductRegisterController;
import com.danzki.model.*;
import com.danzki.repository.ProductRegisterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductRegisterService {
   private final ProductRegisterRepo productRegisterRepo;
   private final ProductRegisterTypeService productRegisterTypeService;

   @Autowired
   public ProductRegisterService(ProductRegisterRepo productRegisterRepo, ProductRegisterTypeService productRegisterTypeService) {
      this.productRegisterRepo = productRegisterRepo;
      this.productRegisterTypeService = productRegisterTypeService;
   }

   public List<ProductRegister> findByProductId(long productId) {
      return this.productRegisterRepo.findByProductId(productId);
   }

   @Transactional
   public ProductRegister saveFromRequest(ProductRegisterController.CorporateSettlementAccountRequest request, Product product, Account account) {
      ProductRegister pr = new ProductRegister();
      pr.setProductRegisterType((ProductRegisterType)this.productRegisterTypeService.findAllByValue(request.registryTypeCode()).get(0));
      pr.setProduct(product);
      pr.setAccount(account);
      pr.setCurrencyCode(request.currencyCode());
      pr.setState(State.OPEN);
      pr.setAccountNumber(account.getAccountNum());
      this.productRegisterRepo.save(pr);
      return pr;
   }

   @Transactional
   public ProductRegister saveByParams(Product product, ProductRegisterType productRegisterType, Account account, String currencyCode) {
      ProductRegister pr = new ProductRegister();
      pr.setProductRegisterType(productRegisterType);
      pr.setProduct(product);
      pr.setAccount(account);
      pr.setCurrencyCode(currencyCode);
      pr.setState(State.OPEN);
      pr.setAccountNumber(account.getAccountNum());
      this.productRegisterRepo.save(pr);
      return pr;
   }
}
