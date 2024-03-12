package com.danzki.service;

import com.danzki.ErrorMessage;
import com.danzki.controller.ProductAgreementController;
import com.danzki.model.Product;
import com.danzki.model.ProductType;
import com.danzki.model.RateType;
import com.danzki.model.State;
import com.danzki.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService implements ErrorMessage {
   private final ProductRepo productRepo;

   @Autowired
   public ProductService(ProductRepo productRepo) {
      this.productRepo = productRepo;
   }

   public Optional<Product> findById(long id) {
      return this.productRepo.findById(id);
   }

   public String getProductNotFoundMessage(long instanceId) {
      return PRODUCT_NOT_FOUND.replaceAll(INSTANCE_ID, Long.toString(instanceId));
   }

   public Optional<Product> findByNumber(String number) {
      return this.productRepo.findByNumber(number);
   }

   @Transactional
   public Product saveFromRequest(ProductAgreementController.CreateProductAgreementRequest request) {
      Product product = new Product();
      product.setProductCodeId(request.productCode());
      product.setClientId(Long.valueOf(request.mdmCode()));
      product.setType(ProductType.getProductType(request.productType()).getName());
      product.setNumber(request.contractNumber());
      product.setPriority(request.priority());
      product.setDateConclusion(request.contractDate());
      product.setStartDate(request.contractDate());
      product.setPenaltyRate(request.interestRatePenalty() == null ? 0.0F : request.interestRatePenalty());
      product.setNso(request.minimalBalance());
      product.setThresholdAmount(request.thresholdAmount() == null ? 0.0F : request.thresholdAmount());
      product.setInterestRateType(RateType.getRateTypeCode(request.rateType()).getRateTypeCode());
      product.setTaxRate(request.taxPercentageRate() == null ? 0.0F : request.taxPercentageRate());
      product.setState(State.OPEN);
      this.productRepo.save(product);
      return product;
   }

   public void delete(Product product) {
      this.productRepo.delete(product);
   }
}
