package com.danzki.service;

import com.danzki.ErrorMessage;
import com.danzki.model.Agreement;
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
         response = "Параметр ContractNumber № договора <PRODUCT_NUM> уже существует для ЭП с ИД  <IDS>.".replaceAll("PRODUCT_NUM", contractNumber);
         response = response.replaceAll("IDS", String.valueOf(((Product)prodOpt.get()).getId()));
      }

      return response;
   }

   public String isExistsRollByArrangementNumber(String contractNumber, String number) {
      String response = "";
      Optional<Product> prodOpt = this.productService.findByNumber(contractNumber);
      if (prodOpt.isPresent()) {
         Product product = (Product)prodOpt.get();
         Optional<Agreement> agreementOpt = this.agreementService.findByProductAndNumber(product, number);
         if (agreementOpt.isPresent()) {
            response = "Параметр ContractNumber № договора <PRODUCT_NUM> уже существует для ЭП с ИД  <IDS>.".replaceAll("PRODUCT_NUM", contractNumber);
            response = response.replaceAll("IDS", String.valueOf(((Product)prodOpt.get()).getId()));
         }
      }

      return response;
   }

   public String getAccountTypeNotFound(String value) {
      return "AccountType со значение <VALUE> не найден.".replaceAll("VALUE", value);
   }

   public String getProductRegistryNotFound(String productCode) {
      return "КодПродукта <VALUE> не найдено в Каталоге продуктов <BD_TABLE_NAME>".replaceAll("VALUE", productCode).replaceAll("BD_TABLE_NAME", this.getSchemaAndTableByClass(this.entityManager, ProductRegisterType.class));
   }
}
