package com.danzki.controller;

import com.danzki.model.Product;
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

@RestController
public class ProductRegisterController {
   private static Logger logger = LoggerFactory.getLogger(ProductRegisterController.class);
   private final ProductRegisterService productRegisterService;
   private final ProductService productService;
   private final AccountRequestValidationService requestValidationService;
   private final AccountPoolService accountPoolService;

   public ProductRegisterController(
           ProductRegisterService productRegisterService,
           ProductService productService,
           AccountRequestValidationService requestValidationService,
           AccountPoolService accountPoolService) {
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
   public ResponseEntity<CreateProductRegistryResponse> createSettlementAccount(@Valid @RequestBody CorporateSettlementAccountRequest request) {
      logger.info("Request: \n " + request.toString());
      var step2Response = this.requestValidationService.isExistsByProductIdAndType(request);
      logger.info("step2Response: " + step2Response);
      if (!step2Response.equals("")) {
         return ResponseEntity
                 .status(HttpStatus.BAD_REQUEST)
                 .body(new CreateProductRegistryBadRequest(new ResponseDataErr(step2Response)));
      } else {
         var prodOpt = this.productService.findById(request.instanceId());
         logger.info("prodOpt: " + prodOpt);
         if (!prodOpt.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new CreateProductRegistryNotFound(new ResponseDataErr(this.productService.getProductNotFoundMessage(request.instanceId()))));
         } else {
            var step3Response = this.requestValidationService.isExistsProductCode(request, (Product)prodOpt.get());
            logger.info("step3Response: " + step3Response);
            if (!step3Response.equals("")) {
               return ResponseEntity
                       .status(HttpStatus.NOT_FOUND)
                       .body(new CreateProductRegistryNotFound(new ResponseDataErr(step3Response)));
            } else {
               var account = this.accountPoolService.getAccountFromPool(
                       request.branchCode(),
                       request.currencyCode(),
                       request.mdmCode(),
                       request.priorityCode(),
                       request.registryTypeCode());
               if (account != null) {
                  logger.info("account: " + account.toString());
                  var productRegister = this.productRegisterService.saveFromRequest(request, prodOpt.get(), account);
                  logger.info("productRegister: " + productRegister.toString());
                  if (productRegister == null) {
                     String messageError = "Продуктовый регистр не создан.";
                     return ResponseEntity
                             .status(HttpStatus.BAD_REQUEST)
                             .body(new CreateProductRegistryBadRequest(new ResponseDataErr(messageError)));
                  } else {
                     return ResponseEntity
                             .status(HttpStatus.OK)
                             .body(new CreateProductRegistryOk(new ResponseDataOk(Long.toString(productRegister.getId()))));
                  }
               } else {
                  return ResponseEntity
                          .status(HttpStatus.NOT_FOUND)
                          .body(new CreateProductRegistryNotFound(new ResponseDataErr(
                                  this.accountPoolService.getAccountNotFoundMessage(request.branchCode(),
                                          request.currencyCode(),
                                          request.mdmCode(),
                                          request.priorityCode(),
                                          request.registryTypeCode()))));
               }
            }
         }
      }
   }

   public record CorporateSettlementAccountRequest(
           @NotNull(message = "{instanceId не может быть null}")
           Long instanceId,
           String registryTypeCode,
           String accountType,
           String currencyCode,
           String branchCode,
           String priorityCode,
           String mdmCode,
           String clientCode,
           String trainRegion,
           String counter,
           String salesCode) {}

   sealed interface CreateProductRegistryResponse {}
   public record CreateProductRegistryBadRequest(@JsonProperty("data") ResponseDataErr response) implements CreateProductRegistryResponse {}

   public record ResponseDataErr(@JsonProperty("message") String message) {}

   public record CreateProductRegistryNotFound(@JsonProperty("data") ResponseDataErr response) implements ProductRegisterController.CreateProductRegistryResponse {}

   public record CreateProductRegistryOk(@JsonProperty("data") ResponseDataOk response) implements CreateProductRegistryResponse {}

   public record ResponseDataOk(@JsonProperty("accountId") String productRegistryId) {}

   public record CreateProductRegistryNotValid(@JsonProperty("data") ResponseDataErr response) implements ProductRegisterController.CreateProductRegistryResponse {}


}
