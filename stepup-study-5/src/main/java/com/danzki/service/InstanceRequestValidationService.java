package com.danzki.service;

import com.danzki.ErrorMessage;
import com.danzki.model.Product;
import com.danzki.model.ProductRegisterType;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InstanceRequestValidationService implements ErrorMessage, ValidationService {
   private static Logger logger = LoggerFactory.getLogger(InstanceRequestValidationService.class);
   private final ProductService productService;
   private final AgreementService agreementService;
   @Autowired
   private EntityManager entityManager;

   public InstanceRequestValidationService(ProductService productService, AgreementService agreementService) {
      this.productService = productService;
      this.agreementService = agreementService;
   }

   public String isExistsProductByContractNumber(String contractNumber) {
      Optional<Product> prodOpt = this.productService.findByNumber(contractNumber);
      String response = "";
      if (prodOpt.isPresent()) {
         response = PRODUCT_EXISTS.replaceAll(PRODUCT_NUM, contractNumber);
         response = response.replaceAll(IDS, Long.toString(prodOpt.get().getId()));
      }

      return response;
   }

   public String isExistsRollByArrangementNumber(String contractNumber, String number) {
      String response = "";
      Optional<Product> prodOpt = this.productService.findByNumber(contractNumber);
      if (prodOpt.isPresent()) {
         var product = prodOpt.get();
         var agreementOpt = this.agreementService.findByProductAndNumber(product, number);
         if (agreementOpt.isPresent()) {
            response = ROLL_EXISTS.replaceAll(VALUE, number);
            response = response.replaceAll(IDS, Long.toString(prodOpt.get().getId()));
         }
      }
      return response;
   }

   public String getAccountTypeNotFound(String value) {
      return ACCOUNT_TYPE_NOT_FOUND.replaceAll(VALUE, value);
   }

   public String getProductRegistryNotFound(String productCode) {
      return PRODUCT_REGISTRY_NOT_FOUND.replaceAll(VALUE, productCode)
              .replaceAll(BD_TABLE_NAME, this.getSchemaAndTableByClass(this.entityManager, ProductRegisterType.class));
   }
}
