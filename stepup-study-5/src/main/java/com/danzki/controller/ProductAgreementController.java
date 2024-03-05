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
         String step2Response = this.instanceRequestValidationService.isExistsProductByContractNumber(request.contractNumber());
         logger.info("step2Response: " + step2Response);
         if (!step2Response.equals("")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ProductAgreementController.CreateProductAgreementBadRequest(new ProductAgreementController.ResponseDataErr(step2Response)));
         } else {
            var accountTypeClient = this.accountTypeService.findByValue(accountTypeValue);
            if (!accountTypeClient.isPresent()) {
               String step4Response = this.instanceRequestValidationService.getAccountTypeNotFound(accountTypeValue);
               logger.info("step4Response: " + step4Response);
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ProductAgreementController.CreateProductAgreementNotFound(new ProductAgreementController.ResponseDataErr(step4Response)));
            } else {
               List<ProductRegisterType> productRegisterTypeList = this.productRegisterTypeService.getProductRegisterByCodeAndAccountType(request.productCode(), (AccountType)accountTypeClient.get());
               if (productRegisterTypeList.size() == 0) {
                  String step5Response = this.instanceRequestValidationService.getProductRegistryNotFound(request.productCode());
                  logger.info("step5Response: " + step5Response);
                  if (!step5Response.equals("")) {
                     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ProductAgreementController.CreateProductAgreementNotFound(new ProductAgreementController.ResponseDataErr(step5Response)));
                  }
               }

               Product product = this.productService.saveFromRequest(request);
               logger.info("product: " + String.valueOf(product));
               List<ProductRegister> productRegisters = new ArrayList();
               Iterator var24 = productRegisterTypeList.iterator();

               while(var24.hasNext()) {
                  var productRegisterType = (ProductRegisterType)var24.next();
                  logger.info("branchCode: " + request.branchCode());
                  logger.info("isoCurrencyCode: " + request.isoCurrencyCode());
                  logger.info("mdmCode: " + request.mdmCode());
                  logger.info("urgencyCode: " + request.urgencyCode());
                  logger.info("productRegisterType: " + String.valueOf(productRegisterType));
                  var account = this.accountPoolService.getAccountFromPool(request.branchCode(), request.isoCurrencyCode(), request.mdmCode(), request.urgencyCode(), productRegisterType.getValue());
                  logger.info("account: " + String.valueOf(account));
                  if (account == null) {
                     this.productService.delete(product);
                     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ProductAgreementController.CreateProductAgreementNotFound(new ProductAgreementController.ResponseDataErr(this.accountPoolService.getAccountNotFoundMessage(request.branchCode(), request.isoCurrencyCode(), request.mdmCode(), request.urgencyCode(), productRegisterType.getValue()))));
                  }

                  productRegisters.add(this.productRegisterService.saveByParams(product, productRegisterType, account, request.isoCurrencyCode()));
               }

               Long[] resisterId = productRegisters.stream()
                       .map((x) -> x.getId())
                       .toArray((size) -> new Long[size]);
               var responseOk = new ProductAgreementController.ResponseDataOk(product.getId(), resisterId, (Long[])null);
               return ResponseEntity.status(HttpStatus.OK).body(new ProductAgreementController.CreateProductAgreementOk(responseOk));
            }
         }
      } else {
         if (request.instanceArrangements().length > 0) {
            ProductAgreementController.InstanceArrangement[] var2 = request.instanceArrangements();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               ProductAgreementController.InstanceArrangement roll = var2[var4];
               String step3Response = this.instanceRequestValidationService.isExistsRollByArrangementNumber(request.contractNumber(), roll.number());
               logger.info("step3Response: " + step3Response);
               if (!step3Response.equals("")) {
                  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ProductAgreementController.CreateProductAgreementBadRequest(new ProductAgreementController.ResponseDataErr(step3Response)));
               }
            }
         }

         Optional<Product> prodOpt = this.productService.findById(request.instanceId());
         if (!prodOpt.isPresent()) {
            logger.info("Не нашли продукт по instanceId <" + request.instanceId() + ">");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ProductAgreementController.CreateProductAgreementNotFound(new ProductAgreementController.ResponseDataErr(this.productService.getProductNotFoundMessage(Long.toString(request.instanceId())))));
         } else {
            Logger var10000 = logger;
            Long var10001 = request.instanceId();
            var10000.info("Нашли продукт по instanceId <" + var10001 + ">: " + ((Product)prodOpt.get()).toString());
            List<Agreement> agreements = new ArrayList();
            if (request.instanceArrangements().length > 0) {
               ProductAgreementController.InstanceArrangement[] var14 = request.instanceArrangements();
               int var17 = var14.length;

               for(int var22 = 0; var22 < var17; ++var22) {
                  ProductAgreementController.InstanceArrangement rollRequest = var14[var22];
                  agreements.add(this.agreementService.saveFromRequest((Product)prodOpt.get(), rollRequest));
               }
            }

            Long[] agreementId = (Long[])agreements.stream().map((x) -> {
               return x.getId();
            }).toArray((size) -> {
               return new Long[size];
            });
            ProductAgreementController.ResponseDataOk responseOk = new ProductAgreementController.ResponseDataOk(request.instanceId(), (Long[])null, agreementId);
            return ResponseEntity.status(HttpStatus.OK).body(new ProductAgreementController.CreateProductAgreementOk(responseOk));
         }
      }
   }

   public static record CreateProductAgreementRequest(Long instanceId, @NotNull(message = "{productType не может быть null}") @NotBlank(message = "{productType не может быть пустое }") String productType, @NotNull(message = "{productType не может быть null}") @NotBlank(message = "{productType не может быть пустое }") String productCode, @NotNull(message = "{registerType не может быть null}") @NotBlank(message = "{registerType не может быть пустое }") String registerType, @NotNull(message = "{mdmCode не может быть null}") @NotBlank(message = "{mdmCode не может быть пустое }") String mdmCode, @NotNull(message = "{contractNumber не может быть null}") @NotBlank(message = "{contractNumber не может быть пустое }") String contractNumber, @NotNull(message = "{contractDate не может быть null}") Date contractDate, @NotNull int priority, Float interestRatePenalty, Float minimalBalance, Float thresholdAmount, String accountingDetails, String rateType, Float taxPercentageRate, Float technicalOverdraftLimitAmount, @NotNull(message = "{contractId не может быть null}") long contractId, @NotNull(message = "{branchCode не может быть null}") @NotBlank(message = "{branchCode не может быть пустое }") String branchCode, @NotNull(message = "{isoCurrencyCode не может быть null}") @NotBlank(message = "{isoCurrencyCode не может быть пустое }") String isoCurrencyCode, @NotNull(message = "{urgencyCode не может быть null}") @NotBlank(message = "{urgencyCode не может быть пустое }") String urgencyCode, Integer referenceCode, ProductAgreementController.AdditionalPropertiesVipData additionalPropertiesVip, ProductAgreementController.InstanceArrangement[] instanceArrangements) {
      public CreateProductAgreementRequest(Long instanceId, @NotNull(message = "{productType не может быть null}") @NotBlank(message = "{productType не может быть пустое }") String productType, @NotNull(message = "{productType не может быть null}") @NotBlank(message = "{productType не может быть пустое }") String productCode, @NotNull(message = "{registerType не может быть null}") @NotBlank(message = "{registerType не может быть пустое }") String registerType, @NotNull(message = "{mdmCode не может быть null}") @NotBlank(message = "{mdmCode не может быть пустое }") String mdmCode, @NotNull(message = "{contractNumber не может быть null}") @NotBlank(message = "{contractNumber не может быть пустое }") String contractNumber, @NotNull(message = "{contractDate не может быть null}") Date contractDate, @NotNull int priority, Float interestRatePenalty, Float minimalBalance, Float thresholdAmount, String accountingDetails, String rateType, Float taxPercentageRate, Float technicalOverdraftLimitAmount, @NotNull(message = "{contractId не может быть null}") long contractId, @NotNull(message = "{branchCode не может быть null}") @NotBlank(message = "{branchCode не может быть пустое }") String branchCode, @NotNull(message = "{isoCurrencyCode не может быть null}") @NotBlank(message = "{isoCurrencyCode не может быть пустое }") String isoCurrencyCode, @NotNull(message = "{urgencyCode не может быть null}") @NotBlank(message = "{urgencyCode не может быть пустое }") String urgencyCode, Integer referenceCode, ProductAgreementController.AdditionalPropertiesVipData additionalPropertiesVip, ProductAgreementController.InstanceArrangement[] instanceArrangements) {
         this.instanceId = instanceId;
         this.productType = productType;
         this.productCode = productCode;
         this.registerType = registerType;
         this.mdmCode = mdmCode;
         this.contractNumber = contractNumber;
         this.contractDate = contractDate;
         this.priority = priority;
         this.interestRatePenalty = interestRatePenalty;
         this.minimalBalance = minimalBalance;
         this.thresholdAmount = thresholdAmount;
         this.accountingDetails = accountingDetails;
         this.rateType = rateType;
         this.taxPercentageRate = taxPercentageRate;
         this.technicalOverdraftLimitAmount = technicalOverdraftLimitAmount;
         this.contractId = contractId;
         this.branchCode = branchCode;
         this.isoCurrencyCode = isoCurrencyCode;
         this.urgencyCode = urgencyCode;
         this.referenceCode = referenceCode;
         this.additionalPropertiesVip = additionalPropertiesVip;
         this.instanceArrangements = instanceArrangements;
      }

      public Long instanceId() {
         return this.instanceId;
      }

      @NotNull(
         message = "{productType не может быть null}"
      )
      @NotBlank(
         message = "{productType не может быть пустое }"
      )
      public String productType() {
         return this.productType;
      }

      @NotNull(
         message = "{productType не может быть null}"
      )
      @NotBlank(
         message = "{productType не может быть пустое }"
      )
      public String productCode() {
         return this.productCode;
      }

      @NotNull(
         message = "{registerType не может быть null}"
      )
      @NotBlank(
         message = "{registerType не может быть пустое }"
      )
      public String registerType() {
         return this.registerType;
      }

      @NotNull(
         message = "{mdmCode не может быть null}"
      )
      @NotBlank(
         message = "{mdmCode не может быть пустое }"
      )
      public String mdmCode() {
         return this.mdmCode;
      }

      @NotNull(
         message = "{contractNumber не может быть null}"
      )
      @NotBlank(
         message = "{contractNumber не может быть пустое }"
      )
      public String contractNumber() {
         return this.contractNumber;
      }

      @NotNull(
         message = "{contractDate не может быть null}"
      )
      public Date contractDate() {
         return this.contractDate;
      }

      @NotNull
      public int priority() {
         return this.priority;
      }

      public Float interestRatePenalty() {
         return this.interestRatePenalty;
      }

      public Float minimalBalance() {
         return this.minimalBalance;
      }

      public Float thresholdAmount() {
         return this.thresholdAmount;
      }

      public String accountingDetails() {
         return this.accountingDetails;
      }

      public String rateType() {
         return this.rateType;
      }

      public Float taxPercentageRate() {
         return this.taxPercentageRate;
      }

      public Float technicalOverdraftLimitAmount() {
         return this.technicalOverdraftLimitAmount;
      }

      @NotNull(
         message = "{contractId не может быть null}"
      )
      public long contractId() {
         return this.contractId;
      }

      @NotNull(
         message = "{branchCode не может быть null}"
      )
      @NotBlank(
         message = "{branchCode не может быть пустое }"
      )
      public String branchCode() {
         return this.branchCode;
      }

      @NotNull(
         message = "{isoCurrencyCode не может быть null}"
      )
      @NotBlank(
         message = "{isoCurrencyCode не может быть пустое }"
      )
      public String isoCurrencyCode() {
         return this.isoCurrencyCode;
      }

      @NotNull(
         message = "{urgencyCode не может быть null}"
      )
      @NotBlank(
         message = "{urgencyCode не может быть пустое }"
      )
      public String urgencyCode() {
         return this.urgencyCode;
      }

      public Integer referenceCode() {
         return this.referenceCode;
      }

      public ProductAgreementController.AdditionalPropertiesVipData additionalPropertiesVip() {
         return this.additionalPropertiesVip;
      }

      public ProductAgreementController.InstanceArrangement[] instanceArrangements() {
         return this.instanceArrangements;
      }
   }

   public static record CreateProductAgreementBadRequest(ProductAgreementController.ResponseDataErr response) implements ProductAgreementController.CreateProductAgreementResponse {
      public CreateProductAgreementBadRequest(@JsonProperty("data") ProductAgreementController.ResponseDataErr response) {
         this.response = response;
      }

      @JsonProperty("data")
      public ProductAgreementController.ResponseDataErr response() {
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

   public static record CreateProductAgreementNotFound(ProductAgreementController.ResponseDataErr response) implements ProductAgreementController.CreateProductAgreementResponse {
      public CreateProductAgreementNotFound(@JsonProperty("data") ProductAgreementController.ResponseDataErr response) {
         this.response = response;
      }

      @JsonProperty("data")
      public ProductAgreementController.ResponseDataErr response() {
         return this.response;
      }
   }

   public static record ResponseDataOk(long instanceId, Long[] registerId, Long[] supplementaryAgreementId) {
      public ResponseDataOk(@JsonProperty("instanceId") long instanceId, @JsonProperty("registerId") Long[] registerId, @JsonProperty("supplementaryAgreementId") Long[] supplementaryAgreementId) {
         this.instanceId = instanceId;
         this.registerId = registerId;
         this.supplementaryAgreementId = supplementaryAgreementId;
      }

      @JsonProperty("instanceId")
      public long instanceId() {
         return this.instanceId;
      }

      @JsonProperty("registerId")
      public Long[] registerId() {
         return this.registerId;
      }

      @JsonProperty("supplementaryAgreementId")
      public Long[] supplementaryAgreementId() {
         return this.supplementaryAgreementId;
      }
   }

   public static record CreateProductAgreementOk(ProductAgreementController.ResponseDataOk response) implements ProductAgreementController.CreateProductAgreementResponse {
      public CreateProductAgreementOk(@JsonProperty("data") ProductAgreementController.ResponseDataOk response) {
         this.response = response;
      }

      @JsonProperty("data")
      public ProductAgreementController.ResponseDataOk response() {
         return this.response;
      }
   }

   public static record InstanceArrangement(String generalAgreementId, String supplementaryAgreementId, String arrangementType, long shedulerJobId, @NotNull(message = "{number не может быть null}") @NotBlank(message = "{number пустое }") String number, @NotNull(message = "{openingDate не может быть null}") Date openingDate, Date closingDate, Date cancelDate, int validityDuration, String cancellationReason, String status, Date interestCalculationDate, float interestRate, float coefficient, String coefficientAction, Float minimumInterestRate, String minimumInterestRateCoefficient, String minimumInterestRateCoefficientAction, Float maximumInterestRate, String maximumInterestRateCoefficient, String maximumInterestRateCoefficientAction) {
      public InstanceArrangement(String generalAgreementId, String supplementaryAgreementId, String arrangementType, long shedulerJobId, @NotNull(message = "{number не может быть null}") @NotBlank(message = "{number пустое }") String number, @NotNull(message = "{openingDate не может быть null}") Date openingDate, Date closingDate, Date cancelDate, int validityDuration, String cancellationReason, String status, Date interestCalculationDate, float interestRate, float coefficient, String coefficientAction, Float minimumInterestRate, String minimumInterestRateCoefficient, String minimumInterestRateCoefficientAction, Float maximumInterestRate, String maximumInterestRateCoefficient, String maximumInterestRateCoefficientAction) {
         this.generalAgreementId = generalAgreementId;
         this.supplementaryAgreementId = supplementaryAgreementId;
         this.arrangementType = arrangementType;
         this.shedulerJobId = shedulerJobId;
         this.number = number;
         this.openingDate = openingDate;
         this.closingDate = closingDate;
         this.cancelDate = cancelDate;
         this.validityDuration = validityDuration;
         this.cancellationReason = cancellationReason;
         this.status = status;
         this.interestCalculationDate = interestCalculationDate;
         this.interestRate = interestRate;
         this.coefficient = coefficient;
         this.coefficientAction = coefficientAction;
         this.minimumInterestRate = minimumInterestRate;
         this.minimumInterestRateCoefficient = minimumInterestRateCoefficient;
         this.minimumInterestRateCoefficientAction = minimumInterestRateCoefficientAction;
         this.maximumInterestRate = maximumInterestRate;
         this.maximumInterestRateCoefficient = maximumInterestRateCoefficient;
         this.maximumInterestRateCoefficientAction = maximumInterestRateCoefficientAction;
      }

      public String generalAgreementId() {
         return this.generalAgreementId;
      }

      public String supplementaryAgreementId() {
         return this.supplementaryAgreementId;
      }

      public String arrangementType() {
         return this.arrangementType;
      }

      public long shedulerJobId() {
         return this.shedulerJobId;
      }

      @NotNull(
         message = "{number не может быть null}"
      )
      @NotBlank(
         message = "{number пустое }"
      )
      public String number() {
         return this.number;
      }

      @NotNull(
         message = "{openingDate не может быть null}"
      )
      public Date openingDate() {
         return this.openingDate;
      }

      public Date closingDate() {
         return this.closingDate;
      }

      public Date cancelDate() {
         return this.cancelDate;
      }

      public int validityDuration() {
         return this.validityDuration;
      }

      public String cancellationReason() {
         return this.cancellationReason;
      }

      public String status() {
         return this.status;
      }

      public Date interestCalculationDate() {
         return this.interestCalculationDate;
      }

      public float interestRate() {
         return this.interestRate;
      }

      public float coefficient() {
         return this.coefficient;
      }

      public String coefficientAction() {
         return this.coefficientAction;
      }

      public Float minimumInterestRate() {
         return this.minimumInterestRate;
      }

      public String minimumInterestRateCoefficient() {
         return this.minimumInterestRateCoefficient;
      }

      public String minimumInterestRateCoefficientAction() {
         return this.minimumInterestRateCoefficientAction;
      }

      public Float maximumInterestRate() {
         return this.maximumInterestRate;
      }

      public String maximumInterestRateCoefficient() {
         return this.maximumInterestRateCoefficient;
      }

      public String maximumInterestRateCoefficientAction() {
         return this.maximumInterestRateCoefficientAction;
      }
   }

   static record AdditionalPropertiesVipData(AdditionalPropertiesVipRecord[] additionalPropertiesVipRecords) {
      AdditionalPropertiesVipData(@JsonProperty("data") AdditionalPropertiesVipRecord[] additionalPropertiesVipRecords) {
         this.additionalPropertiesVipRecords = additionalPropertiesVipRecords;
      }

      @JsonProperty("data")
      public AdditionalPropertiesVipRecord[] additionalPropertiesVipRecords() {
         return this.additionalPropertiesVipRecords;
      }
   }

   static record AdditionalPropertiesVipRecord(String key, String value, String name) {
      AdditionalPropertiesVipRecord(String key, String value, String name) {
         this.key = key;
         this.value = value;
         this.name = name;
      }

      public String key() {
         return this.key;
      }

      public String value() {
         return this.value;
      }

      public String name() {
         return this.name;
      }
   }

   public static record CreateProductAgreementServerError(ProductAgreementController.ResponseDataErr response) implements ProductAgreementController.CreateProductAgreementResponse {
      public CreateProductAgreementServerError(@JsonProperty("data") ProductAgreementController.ResponseDataErr response) {
         this.response = response;
      }

      @JsonProperty("data")
      public ProductAgreementController.ResponseDataErr response() {
         return this.response;
      }
   }

   public static record CreateProductAgreementNotValid(ProductAgreementController.ResponseDataErr response) implements ProductAgreementController.CreateProductAgreementResponse {
      public CreateProductAgreementNotValid(@JsonProperty("data") ProductAgreementController.ResponseDataErr response) {
         this.response = response;
      }

      @JsonProperty("data")
      public ProductAgreementController.ResponseDataErr response() {
         return this.response;
      }
   }

   interface CreateProductAgreementResponse {
   }
}
