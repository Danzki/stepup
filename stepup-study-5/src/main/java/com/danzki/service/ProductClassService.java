package com.danzki.service;

import com.danzki.model.ProductClass;
import com.danzki.repository.ProductClassRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ProductClassService {
   private final ProductClassRepo productClassRepo;

   @Autowired
   public ProductClassService(ProductClassRepo productClassRepo) {
      this.productClassRepo = productClassRepo;
   }

   Optional<ProductClass> findByGlobalCode(String value) {
      return this.productClassRepo.findByGlobalCode(value);
   }
}
