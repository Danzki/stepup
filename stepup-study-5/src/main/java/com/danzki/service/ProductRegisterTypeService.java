package com.danzki.service;

import com.danzki.model.AccountType;
import com.danzki.model.ProductClass;
import com.danzki.model.ProductRegisterType;
import com.danzki.repository.ProductRegisterTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductRegisterTypeService {
   @Autowired
   private ProductRegisterTypeRepo productRegisterTypeRepo;
   private final ProductClassService productClassService;

   public ProductRegisterTypeService(ProductClassService productClassService) {
      this.productClassService = productClassService;
   }

   public List<ProductRegisterType> findAllByValue(String value) {
      return this.productRegisterTypeRepo.findAllByValue(value);
   }

   public ProductRegisterType findByRegistryTypeName(String registryTypeName) {
      return this.productRegisterTypeRepo.findByRegisterTypeName(registryTypeName);
   }

   public List<ProductRegisterType> findAllByProductClass(ProductClass productClass) {
      return this.productRegisterTypeRepo.findAllByProductClass(productClass);
   }

   public List<ProductRegisterType> getProductRegisterByCodeAndAccountType(String productCode, AccountType accountType) {
      Optional<ProductClass> productClassOpt = this.productClassService.findByGlobalCode(productCode);
      if (productClassOpt.isPresent()) {
         List<ProductRegisterType> productRegisterTypes = this.findAllByProductClass((ProductClass)productClassOpt.get()).stream().filter((x) -> {
            return x.getAccountType().equals(accountType);
         }).toList();
         return productRegisterTypes;
      } else {
         return new ArrayList();
      }
   }
}
