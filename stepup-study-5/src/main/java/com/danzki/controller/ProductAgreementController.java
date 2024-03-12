package com.danzki.controller;

import com.danzki.model.*;
import com.danzki.service.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ProductAgreementController {
   private static Logger logger = LoggerFactory.getLogger(ProductAgreementController.class);
   private final String accountTypeValue = "Клиентский";
   private final InstanceRequestValidationService instanceRequestValidationService;
   private final AccountTypeService accountTypeService;
   private final ProductService productService;
   private final ProductRegisterService productRegisterService;
   private final AccountPoolService accountPoolService;
   private final AgreementService agreementService;
   private final ProductRegisterTypeService productRegisterTypeService;

   @Autowired
   public ProductAgreementController(
           InstanceRequestValidationService instanceRequestValidationService,
           AccountTypeService accountTypeService,
           ProductService productService,
           ProductRegisterService productRegisterService,
           AccountPoolService accountPoolService,
           AgreementService agreementService,
           ProductRegisterTypeService productRegisterTypeService) {
      this.instanceRequestValidationService = instanceRequestValidationService;
      this.accountTypeService = accountTypeService;
      this.productService = productService;
      this.productRegisterService = productRegisterService;
      this.accountPoolService = accountPoolService;
      this.agreementService = agreementService;
      this.productRegisterTypeService = productRegisterTypeService;
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

   @PostMapping({"/corporate-settlement-instance/create"})
   public ResponseEntity<CreateProductAgreementResponse> createProductAgreement(@Valid @RequestBody ProductAgreementController.CreateProductAgreementRequest request) {
      if (request.instanceId() == null) {
         logger.info("instanceId пустой. Создаем новый продукт.");
         var step2Response = this.instanceRequestValidationService.isExistsProductByContractNumber(request.contractNumber());
         logger.info("step2Response: " + step2Response);
         if (!step2Response.equals("")) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new CreateProductAgreementBadRequest(new ResponseDataErr(step2Response)));
         } else {
            var accountTypeClient = this.accountTypeService.findByValue(accountTypeValue);
            if (!accountTypeClient.isPresent()) {
               var step4Response = this.instanceRequestValidationService.getAccountTypeNotFound(accountTypeValue);
               logger.info("step4Response: " + step4Response);
               return ResponseEntity
                       .status(HttpStatus.NOT_FOUND)
                       .body(new CreateProductAgreementNotFound(new ResponseDataErr(step4Response)));
            } else {
               List<ProductRegisterType> productRegisterTypeList = this.productRegisterTypeService.getProductRegisterByCodeAndAccountType(request.productCode(), (AccountType)accountTypeClient.get());
               if (productRegisterTypeList.size() == 0) {
                  String step5Response = this.instanceRequestValidationService.getProductRegistryNotFound(request.productCode());
                  logger.info("step5Response: " + step5Response);
                  if (!step5Response.equals("")) {
                     return ResponseEntity
                             .status(HttpStatus.NOT_FOUND)
                             .body(new CreateProductAgreementNotFound(new ResponseDataErr(step5Response)));
                  }
               }

               var product = this.productService.saveFromRequest(request);
               logger.info("product: " + String.valueOf(product));
               List<ProductRegister> productRegisters = new ArrayList();
               Iterator var24 = productRegisterTypeList.iterator();

               while(var24.hasNext()) {
                  var productRegisterType = (ProductRegisterType)var24.next();
                  logger.info("branchCode: " + request.branchCode());
                  logger.info("isoCurrencyCode: " + request.isoCurrencyCode());
                  logger.info("mdmCode: " + request.mdmCode());
                  logger.info("urgencyCode: " + request.urgencyCode());
                  logger.info("productRegisterType: " + productRegisterType);
                  var account = this.accountPoolService.getAccountFromPool(
                          request.branchCode(),
                          request.isoCurrencyCode(),
                          request.mdmCode(),
                          request.urgencyCode(),
                          productRegisterType.getValue());
                  logger.info("account: " + account);
                  if (account == null) {
                     this.productService.delete(product);
                     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ProductAgreementController.CreateProductAgreementNotFound(new ProductAgreementController.ResponseDataErr(this.accountPoolService.getAccountNotFoundMessage(request.branchCode(), request.isoCurrencyCode(), request.mdmCode(), request.urgencyCode(), productRegisterType.getValue()))));
                  }

                  productRegisters.add(this.productRegisterService.saveByParams(product, productRegisterType, account, request.isoCurrencyCode()));
               }

               Long[] resisterId = productRegisters.stream()
                       .map((x) -> x.getId())
                       .toArray((size) -> new Long[size]);
               var responseOk = new ResponseDataOk(product.getId(), resisterId, (Long[])null);
               return ResponseEntity
                       .status(HttpStatus.OK)
                       .body(new CreateProductAgreementOk(responseOk));
            }
         }
      } else {
         if (request.instanceArrangements().length > 0) {
            InstanceArrangement[] var2 = request.instanceArrangements();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               ProductAgreementController.InstanceArrangement roll = var2[var4];
               var step3Response = this.instanceRequestValidationService.isExistsRollByArrangementNumber(request.contractNumber(), roll.number());
               logger.info("step3Response: " + step3Response);
               if (!step3Response.equals("")) {
                  return ResponseEntity
                          .status(HttpStatus.BAD_REQUEST)
                          .body(new CreateProductAgreementBadRequest(new ResponseDataErr(step3Response)));
               }
            }
         }

         var prodOpt = this.productService.findById(request.instanceId());
         if (!prodOpt.isPresent()) {
            logger.info("Не нашли продукт по instanceId <" + request.instanceId() + ">");
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new CreateProductAgreementNotFound(new ResponseDataErr(this.productService.getProductNotFoundMessage(request.instanceId()))));
         } else {
            Logger var10000 = logger;
            var var10001 = request.instanceId();
            var10000.info("Нашли продукт по instanceId <" + var10001 + ">: " + ((Product)prodOpt.get()).toString());
            List<Agreement> agreements = new ArrayList();
            if (request.instanceArrangements().length > 0) {
               ProductAgreementController.InstanceArrangement[] var14 = request.instanceArrangements();
               var var17 = var14.length;

               for(int var22 = 0; var22 < var17; ++var22) {
                  ProductAgreementController.InstanceArrangement rollRequest = var14[var22];
                  agreements.add(this.agreementService.saveFromRequest((Product)prodOpt.get(), rollRequest));
               }
            }

            Long[] agreementId = (Long[])agreements.stream()
                    .map((x) -> x.getId())
                    .toArray((size) -> new Long[size]);
            var responseOk = new ResponseDataOk(request.instanceId(), (Long[])null, agreementId);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new CreateProductAgreementOk(responseOk));
         }
      }
   }

   public static record CreateProductAgreementRequest(
           Long instanceId,
           @NotNull(message = "{productType не может быть null}")
           @NotBlank(message = "{productType не может быть пустое }")
           String productType,
           @NotNull(message = "{productType не может быть null}")
           @NotBlank(message = "{productType не может быть пустое }")
           String productCode,
           @NotNull(message = "{registerType не может быть null}")
           @NotBlank(message = "{registerType не может быть пустое }")
           String registerType,
           @NotNull(message = "{mdmCode не может быть null}")
           @NotBlank(message = "{mdmCode не может быть пустое }")
           String mdmCode,
           @NotNull(message = "{contractNumber не может быть null}")
           @NotBlank(message = "{contractNumber не может быть пустое }")
           String contractNumber,
           @NotNull(message = "{contractDate не может быть null}")
           Date contractDate,
           @NotNull int priority,
           Float interestRatePenalty,
           Float minimalBalance,
           Float thresholdAmount,
           String accountingDetails,
           String rateType,
           Float taxPercentageRate,
           Float technicalOverdraftLimitAmount,
           @NotNull(message = "{contractId не может быть null}")
           long contractId,
           @NotNull(message = "{branchCode не может быть null}")
           @NotBlank(message = "{branchCode не может быть пустое }")
           String branchCode,
           @NotNull(message = "{isoCurrencyCode не может быть null}")
           @NotBlank(message = "{isoCurrencyCode не может быть пустое }")
           String isoCurrencyCode,
           @NotNull(message = "{urgencyCode не может быть null}")
           @NotBlank(message = "{urgencyCode не может быть пустое }")
           String urgencyCode,
           Integer referenceCode,
           AdditionalPropertiesVipData additionalPropertiesVip,
           InstanceArrangement[] instanceArrangements) {}

   sealed interface CreateProductAgreementResponse {}
   public record CreateProductAgreementServerError(@JsonProperty("data") ResponseDataErr response) implements ProductAgreementController.CreateProductAgreementResponse {}

   public record CreateProductAgreementNotValid(@JsonProperty("data") ResponseDataErr response) implements ProductAgreementController.CreateProductAgreementResponse {}

   public record CreateProductAgreementBadRequest(@JsonProperty("data") ResponseDataErr response) implements CreateProductAgreementResponse {}

   public record ResponseDataErr(@JsonProperty("message") String message) {}

   public record CreateProductAgreementNotFound(@JsonProperty("data") ResponseDataErr response) implements ProductAgreementController.CreateProductAgreementResponse {}

   public record ResponseDataOk(
           @JsonProperty("instanceId") long instanceId,
           @JsonProperty("registerId") Long[] registerId,
           @JsonProperty("supplementaryAgreementId") Long[] supplementaryAgreementId) {}

   public record CreateProductAgreementOk(@JsonProperty("data") ResponseDataOk response) implements ProductAgreementController.CreateProductAgreementResponse {}

   public record InstanceArrangement(
           String generalAgreementId,
           String supplementaryAgreementId,
           String arrangementType,
           long shedulerJobId,
           @NotNull(message = "{number не может быть null}")
           @NotBlank(message = "{number пустое }")
           String number,
           @NotNull(message = "{openingDate не может быть null}")
           Date openingDate,
           Date closingDate,
           Date cancelDate,
           int validityDuration,
           String cancellationReason,
           String status,
           Date interestCalculationDate,
           float interestRate,
           float coefficient,
           String coefficientAction,
           Float minimumInterestRate,
           String minimumInterestRateCoefficient,
           String minimumInterestRateCoefficientAction,
           Float maximumInterestRate,
           String maximumInterestRateCoefficient,
           String maximumInterestRateCoefficientAction) {}

   record AdditionalPropertiesVipData(@JsonProperty("data") AdditionalPropertiesVipRecord[] additionalPropertiesVipRecords) {}

   record AdditionalPropertiesVipRecord(String key, String value, String name) {}
}
