package com.danzki;

import com.danzki.controller.ProductRegisterController;
import com.danzki.controller.ProductRegisterController.CorporateSettlementAccountRequest;
import com.danzki.controller.ProductRegisterController.CreateProductRegistryOk;
import com.danzki.controller.ProductRegisterController.ResponseDataOk;
import com.danzki.model.Account;
import com.danzki.model.Product;
import com.danzki.model.ProductRegister;
import com.danzki.service.AccountPoolService;
import com.danzki.service.AccountRequestValidationService;
import com.danzki.service.ProductRegisterService;
import com.danzki.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

public class ProductRegisterControllerTest {

    CorporateSettlementAccountRequest requestValid;
    CorporateSettlementAccountRequest requestNotValid;
    private long instantId = 140L;
    private String registryTypeCode = "03.012.002_47533_ComSoLd";
    private String accountType = "Клиентский";
    private String currencyCode = "840";
    private String branchCode = "0022";
    private String priorityCode = "00";
    private String mdmCode = "15";

    @BeforeEach
    void initRequest() {
        requestValid = new CorporateSettlementAccountRequest(
                instantId,
                registryTypeCode,
                accountType,
                currencyCode,
                branchCode,
                priorityCode,
                mdmCode,
                "",
                "",
                "",
                ""
        );
        requestNotValid = new CorporateSettlementAccountRequest(
                null,
                registryTypeCode,
                accountType,
                currencyCode,
                branchCode,
                priorityCode,
                mdmCode,
                "",
                "",
                "",
                ""
        );
    }

    @Test
    void createSettlementAccount_step2_BAD_REQUEST() {
        var productRegisterService = Mockito.mock(ProductRegisterService.class);
        var productService = Mockito.mock(ProductService.class);
        var requestValidationService = Mockito.mock(AccountRequestValidationService.class);
        var accountPoolService = Mockito.mock(AccountPoolService.class);

        var errorMessage = "Not valid";

        Mockito.when(requestValidationService.isExistsByProductIdAndType(requestValid)).thenReturn(errorMessage);

        var controller = new ProductRegisterController(
                productRegisterService,
                productService,
                requestValidationService,
                accountPoolService
        );

        var expected = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ProductRegisterController.CreateProductRegistryBadRequest(new ProductRegisterController.ResponseDataErr(errorMessage)));
        var actual = controller.createSettlementAccount(requestValid);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void createSettlementAccount_step3_PRODUCT_NOT_FOUND() {
        var productRegisterService = Mockito.mock(ProductRegisterService.class);
        var productService = Mockito.mock(ProductService.class);
        var requestValidationService = Mockito.mock(AccountRequestValidationService.class);
        var accountPoolService = Mockito.mock(AccountPoolService.class);

        var errorMessage = "Product not found";

        Mockito.when(requestValidationService.isExistsByProductIdAndType(any())).thenReturn("");
        Mockito.when(productService.getProductNotFoundMessage(any())).thenReturn(errorMessage);

        var controller = new ProductRegisterController(
                productRegisterService,
                productService,
                requestValidationService,
                accountPoolService
        );

        var expected = ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ProductRegisterController.CreateProductRegistryNotFound(new ProductRegisterController.ResponseDataErr(errorMessage)));
        var actual = controller.createSettlementAccount(requestValid);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void createSettlementAccount_OK() {
        var productRegisterService = Mockito.mock(ProductRegisterService.class);
        var productService = Mockito.mock(ProductService.class);
        var requestValidationService = Mockito.mock(AccountRequestValidationService.class);
        var accountPoolService = Mockito.mock(AccountPoolService.class);

        Mockito.when(requestValidationService.isExistsByProductIdAndType(any())).thenReturn("");
        var product = new Product();
        product.setId(instantId);
        Mockito.when(productService.findById(instantId)).thenReturn(Optional.of(product));
        Mockito.when(requestValidationService.isExistsProductCode(requestValid, product)).thenReturn("");

        var account = new Account();
        account.setId(500L);
        account.setAccountNum("4564545");
        Mockito.when(accountPoolService.getAccountFromPool(
                branchCode,
                currencyCode,
                mdmCode,
                priorityCode,
                registryTypeCode)).thenReturn(account);

        var productRegister = new ProductRegister();
        productRegister.setProduct(product);
        productRegister.setAccount(account);

        Mockito.when(productRegisterService.saveFromRequest(requestValid, product, account))
                .thenReturn(productRegister);

        var controller = new ProductRegisterController(
                productRegisterService,
                productService,
                requestValidationService,
                accountPoolService
        );

        var expected = ResponseEntity.status(HttpStatus.OK)
                .body(new CreateProductRegistryOk(new ResponseDataOk(Long.toString(productRegister.getId()))));
        var actual = controller.createSettlementAccount(requestValid);
        Assertions.assertEquals(expected, actual);
    }
}
