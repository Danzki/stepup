package com.danzki.controller;

import com.danzki.model.Account;
import com.danzki.model.Product;
import com.danzki.model.ProductRegister;
import com.danzki.service.AccountPoolService;
import com.danzki.service.AccountRequestValidationService;
import com.danzki.service.ProductRegisterService;
import com.danzki.service.ProductService;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class ProductRegisterController {
   private static Logger logger = LoggerFactory.getLogger(ProductRegisterController.class);
   private final ProductRegisterService productRegisterService;
   private final ProductService productService;
   private final AccountRequestValidationService requestValidationService;
   private final AccountPoolService accountPoolService;

   public ProductRegisterController(ProductRegisterService productRegisterService, ProductService productService, AccountRequestValidationService requestValidationService, AccountPoolService accountPoolService) {
      this.productRegisterService = productRegisterService;
      this.productService = productService;
      this.requestValidationService = requestValidationService;
      this.accountPoolService = accountPoolService;
   }

   @ResponseStatus(HttpStatus.BAD_REQUEST)
   @ExceptionHandler({MethodArgumentNotValidException.class})
   public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
      Map<String, String> errors = new HashMap();
      ex.getBindingResult().getAllErrors().forEach((error) -> {
         String fieldName = ((FieldError)error).getField();
         String errorMessage = error.getDefaultMessage();
         errors.put(fieldName, errorMessage);
      });
      return errors;
   }

   @PostMapping({"/corporate-settlement-account/create"})
   public ResponseEntity<ProductRegisterController.CreateProductRegistryResponse> createSettlementAccount(@Valid @RequestBody ProductRegisterController.CorporateSettlementAccountRequest request) {
      logger.info("Request: \n " + request.toString());
      String step2Response = this.requestValidationService.isExistsByProductIdAndType(request);
      logger.info("step2Response: " + step2Response);
      if (!step2Response.equals("")) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ProductRegisterController.CreateProductRegistryBadRequest(new ProductRegisterController.ResponseDataErr(step2Response)));
      } else {
         Optional<Product> prodOpt = this.productService.findById(request.instanceId());
         logger.info("prodOpt: " + String.valueOf(prodOpt));
         if (!prodOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ProductRegisterController.CreateProductRegistryNotFound(new ProductRegisterController.ResponseDataErr(this.productService.getProductNotFoundMessage(Long.toString(request.instanceId())))));
         } else {
            String step3Response = this.requestValidationService.isExistsProductCode(request, (Product)prodOpt.get());
            logger.info("step3Response: " + step3Response);
            if (!step3Response.equals("")) {
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ProductRegisterController.CreateProductRegistryNotFound(new ProductRegisterController.ResponseDataErr(step3Response)));
            } else {
               Account account = this.accountPoolService.getAccountFromPool(request.branchCode(), request.currencyCode(), request.mdmCode(), request.priorityCode(), request.registryTypeCode());
               new ProductRegister();
               if (account != null) {
                  logger.info("account: " + account.toString());
                  ProductRegister productRegister = this.productRegisterService.saveFromRequest(request, (Product)prodOpt.get(), account);
                  logger.info("productRegister: " + productRegister.toString());
                  if (productRegister == null) {
                     String messageError = "Продуктовый регистр не создан.";
                     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ProductRegisterController.CreateProductRegistryBadRequest(new ProductRegisterController.ResponseDataErr(messageError)));
                  } else {
                     return ResponseEntity.status(HttpStatus.OK).body(new ProductRegisterController.CreateProductRegistryOk(new ProductRegisterController.ResponseDataOk(Long.toString(productRegister.getId()))));
                  }
               } else {
                  return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ProductRegisterController.CreateProductRegistryNotFound(new ProductRegisterController.ResponseDataErr(this.accountPoolService.getAccountNotFoundMessage(request.branchCode(), request.currencyCode(), request.mdmCode(), request.priorityCode(), request.registryTypeCode()))));
               }
            }
         }
      }
   }

   public static record CorporateSettlementAccountRequest(@NotNull(message = "{instanceId не может быть null}") Long instanceId, String registryTypeCode, String accountType, String currencyCode, String branchCode, String priorityCode, String mdmCode, String clientCode, String trainRegion, String counter, String salesCode) {
      public CorporateSettlementAccountRequest(@NotNull(message = "{instanceId не может быть null}") Long instanceId, String registryTypeCode, String accountType, String currencyCode, String branchCode, String priorityCode, String mdmCode, String clientCode, String trainRegion, String counter, String salesCode) {
         this.instanceId = instanceId;
         this.registryTypeCode = registryTypeCode;
         this.accountType = accountType;
         this.currencyCode = currencyCode;
         this.branchCode = branchCode;
         this.priorityCode = priorityCode;
         this.mdmCode = mdmCode;
         this.clientCode = clientCode;
         this.trainRegion = trainRegion;
         this.counter = counter;
         this.salesCode = salesCode;
      }

      @NotNull(
         message = "{instanceId не может быть null}"
      )
      public Long instanceId() {
         return this.instanceId;
      }

      public String registryTypeCode() {
         return this.registryTypeCode;
      }

      public String accountType() {
         return this.accountType;
      }

      public String currencyCode() {
         return this.currencyCode;
      }

      public String branchCode() {
         return this.branchCode;
      }

      public String priorityCode() {
         return this.priorityCode;
      }

      public String mdmCode() {
         return this.mdmCode;
      }

      public String clientCode() {
         return this.clientCode;
      }

      public String trainRegion() {
         return this.trainRegion;
      }

      public String counter() {
         return this.counter;
      }

      public String salesCode() {
         return this.salesCode;
      }
   }

   public static record CreateProductRegistryBadRequest(ProductRegisterController.ResponseDataErr response) implements ProductRegisterController.CreateProductRegistryResponse {
      public CreateProductRegistryBadRequest(@JsonProperty("data") ProductRegisterController.ResponseDataErr response) {
         this.response = response;
      }

      @JsonProperty("data")
      public ProductRegisterController.ResponseDataErr response() {
         return this.response;
      }
   }

   public static record ResponseDataErr(String message) {
      public ResponseDataErr(@JsonProperty("message") String message) {
         this.message = message;
      }

      @JsonProperty("message")
      public String message() {
         return this.message;
      }
   }

   public static record CreateProductRegistryNotFound(ProductRegisterController.ResponseDataErr response) implements ProductRegisterController.CreateProductRegistryResponse {
      public CreateProductRegistryNotFound(@JsonProperty("data") ProductRegisterController.ResponseDataErr response) {
         this.response = response;
      }

      @JsonProperty("data")
      public ProductRegisterController.ResponseDataErr response() {
         return this.response;
      }
   }

   public static record CreateProductRegistryOk(ProductRegisterController.ResponseDataOk response) implements ProductRegisterController.CreateProductRegistryResponse {
      public CreateProductRegistryOk(@JsonProperty("data") ProductRegisterController.ResponseDataOk response) {
         this.response = response;
      }

      @JsonProperty("data")
      public ProductRegisterController.ResponseDataOk response() {
         return this.response;
      }
   }

   public static record ResponseDataOk(String productRegistryId) {
      public ResponseDataOk(@JsonProperty("accountId") String productRegistryId) {
         this.productRegistryId = productRegistryId;
      }

      @JsonProperty("accountId")
      public String productRegistryId() {
         return this.productRegistryId;
      }
   }

   public static record CreateProductRegistryNotValid(ProductRegisterController.ResponseDataErr response) implements ProductRegisterController.CreateProductRegistryResponse {
      public CreateProductRegistryNotValid(@JsonProperty("data") ProductRegisterController.ResponseDataErr response) {
         this.response = response;
      }

      @JsonProperty("data")
      public ProductRegisterController.ResponseDataErr response() {
         return this.response;
      }
   }

   interface CreateProductRegistryResponse {
   }
}
