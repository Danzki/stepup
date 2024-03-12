package com.danzki;

import com.danzki.controller.ProductAgreementController;
import com.danzki.controller.ProductAgreementController.CreateProductAgreementRequest;
import com.danzki.controller.ProductAgreementController.InstanceArrangement;
import com.danzki.model.*;
import com.danzki.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ProductAgreementControllerTest {
    CreateProductAgreementRequest requestNewProduct;
    CreateProductAgreementRequest requestNewArrangement;
    private final String productType = "НСО";
    private final String productCode = "02";
    private final String registerType = "12345678";
    private final String mdmCode = "15";
    private final String contractNumber = "77889";
    private final String contractDateString = "2024-02-01";
    private Date contractDate;
    private final String branchCode = "0022";
    private final String isoCurrencyCode = "RUB";
    private final String urgencyCode = "00";
    private Date openingDate;
    private final String openingDateString = "2024-02-28";
    private final String accountTypeName = "Клиентский";

    @BeforeEach
    void initRequest() {
        var formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            contractDate = formatter.parse(contractDateString);
            openingDate = formatter.parse(openingDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        requestNewProduct = new CreateProductAgreementRequest(
                null,
                productType,
                productCode,
                registerType,
                mdmCode,
                contractNumber,
                contractDate,
                1,
                18.5f,
                10000000.00f,
                null,
                "Some accounting data",
                "0",
                null,
                null,
                78563722,
                branchCode,
                isoCurrencyCode,
                urgencyCode,
                453627,
                null,
                null
        );

        var arrangement = new InstanceArrangement(
                "788989",
                "675656",
                productType,
                123,
                "1",
                openingDate,
                null,
                null,
                500,
                null,
                "Открыт",
                openingDate,
                5,
                2,
                "+",
                null,
                null,
                null,
                null,
                null,
                null
        );
        requestNewArrangement = new CreateProductAgreementRequest(
                25L,
                productType,
                productCode,
                registerType,
                mdmCode,
                contractNumber,
                contractDate,
                1,
                18.5f,
                10000000.00f,
                null,
                "Some accounting data",
                "0",
                null,
                null,
                78563722,
                branchCode,
                isoCurrencyCode,
                urgencyCode,
                453627,
                null,
                new InstanceArrangement[] {arrangement}
        );
    }


    @Test
    void createProductAgreement_NewProductSuccess() {
        var instanceRequestValidationService = Mockito.mock(InstanceRequestValidationService.class);
        var accountTypeService = Mockito.mock(AccountTypeService.class);
        var productService = Mockito.mock(ProductService.class);
        var productRegisterService = Mockito.mock(ProductRegisterService.class);
        var accountPoolService = Mockito.mock(AccountPoolService.class);
        var agreementService = Mockito.mock( AgreementService.class);
        var productRegisterTypeService = Mockito.mock(ProductRegisterTypeService.class);

        Mockito.when(instanceRequestValidationService.isExistsProductByContractNumber(requestNewProduct.contractNumber()))
                .thenReturn("");
        var accountType = new AccountType();
        accountType.setValue(accountTypeName);
        Mockito.when(accountTypeService.findByValue(accountTypeName)).thenReturn(Optional.of(accountType));

        var productClass = new ProductClass();
        productClass.setGlobalCode(requestNewProduct.productCode());
        var productRegisterType = new ProductRegisterType();
        productRegisterType.setProductClass(productClass);
        productRegisterType.setAccountType(accountType);

        List<ProductRegisterType> productRegisterTypeList = new ArrayList<>();
        productRegisterTypeList.add(productRegisterType);
        Mockito.when(productRegisterTypeService.getProductRegisterByCodeAndAccountType(requestNewProduct.productCode(), accountType))
                .thenReturn(productRegisterTypeList);

        var product = new Product();
        product.setId(25L);
        Mockito.when(productService.saveFromRequest(requestNewProduct))
                .thenReturn(product);

        var account = new Account();
        account.setId(500L);
        account.setAccountNum("4564545");
        Mockito.when(accountPoolService.getAccountFromPool(
                branchCode,
                isoCurrencyCode,
                mdmCode,
                urgencyCode,
                productRegisterType.getValue())).thenReturn(account);

        var productRegister = new ProductRegister();
        productRegister.setId(55L);
        productRegister.setProductRegisterType(productRegisterType);
        productRegister.setAccount(account);
        productRegister.setCurrencyCode(isoCurrencyCode);
        productRegister.setProduct(product);

        Mockito.when(productRegisterService.saveByParams(product, productRegisterType, account, requestNewProduct.isoCurrencyCode()))
                .thenReturn(productRegister);

        var controller = new ProductAgreementController(
                instanceRequestValidationService,
                accountTypeService,
                productService,
                productRegisterService,
                accountPoolService,
                agreementService,
                productRegisterTypeService
        );

        var resisterId = new Long[] {productRegister.getId()};
        var responseOk = new ProductAgreementController.ResponseDataOk(product.getId(), resisterId, null);
        var expected = ResponseEntity.status(HttpStatus.OK).body(new ProductAgreementController.CreateProductAgreementOk(responseOk));;
        var actual = controller.createProductAgreement(requestNewProduct);
        Assertions.assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }
}
