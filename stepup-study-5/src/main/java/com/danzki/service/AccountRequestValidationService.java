package com.danzki.service;

import com.danzki.ErrorMessage;
import com.danzki.controller.ProductRegisterController.CorporateSettlementAccountRequest;
import com.danzki.model.Product;
import com.danzki.model.ProductRegister;
import com.danzki.model.ProductRegisterType;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class AccountRequestValidationService implements ErrorMessage, ValidationService {
   private static Logger logger = LoggerFactory.getLogger(AccountRequestValidationService.class);
   private final ProductRegisterService productRegisterService;
   private final ProductRegisterTypeService productRegisterTypeService;
   @Autowired
   private EntityManager entityManager;

   @Autowired
   public AccountRequestValidationService(ProductRegisterService productRegisterService, ProductRegisterTypeService productRegisterTypeService) {
      this.productRegisterService = productRegisterService;
      this.productRegisterTypeService = productRegisterTypeService;
   }

   public String isValidRequest(CorporateSettlementAccountRequest request) {
      String response = "";
      if (request.instanceId() == null) {
         response = ERROR_NECESSARY_FIELDS.replaceAll(INSTANCE_ID, "");
      }

      return response;
   }

   public String isExistsByProductIdAndType(CorporateSettlementAccountRequest request) {
      String response = "";
      List<ProductRegister> prWithProductIdAndType = this.productRegisterService.findByProductId(request.instanceId()).stream().filter((x) -> {
         return x.getProductRegisterType().getRegisterTypeName().equals(request.registryTypeCode());
      }).toList();
      if (prWithProductIdAndType.size() > 0) {
         response = ERROR_PR_EXISTS.replaceAll(REGISTRY_TYPE_CODE_VALUE, request.registryTypeCode());
         String ids = "";

         ProductRegister pr;
         for(Iterator var5 = prWithProductIdAndType.iterator(); var5.hasNext(); ids = ids + pr.getProduct().getId() + ",") {
            pr = (ProductRegister)var5.next();
         }

         response = response.replaceAll(IDS, ids);
      }

      return response;
   }

   public String isExistsProductCode(CorporateSettlementAccountRequest request, Product product) {
      String response = "";
      List<ProductRegisterType> prTypes = this.productRegisterTypeService.findAllByValue(product.getProductCodeId());
      boolean isExists = false;
      Iterator var6 = prTypes.iterator();

      while(var6.hasNext()) {
         ProductRegisterType prt = (ProductRegisterType)var6.next();
         if (prt.getValue().equals(request.registryTypeCode())) {
            isExists = true;
            break;
         }
      }

      if (!isExists) {
         response = ERROR_PRODUCT_NOT_FOUND.replaceAll(PRODUCT_CODE, product.getProductCodeId());
         response = response.replaceAll(BD_TABLE_NAME, this.getSchemaAndTableByClass(this.entityManager, ProductRegisterType.class));
      }

      return response;
   }
}
