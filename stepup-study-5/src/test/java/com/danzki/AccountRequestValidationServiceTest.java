package com.danzki;

import com.danzki.controller.ProductRegisterController.CorporateSettlementAccountRequest;
import com.danzki.model.Product;
import com.danzki.model.ProductClass;
import com.danzki.model.ProductRegister;
import com.danzki.model.ProductRegisterType;
import com.danzki.service.AccountRequestValidationService;
import com.danzki.service.ProductRegisterService;
import com.danzki.service.ProductRegisterTypeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class AccountRequestValidationServiceTest {
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
    void isValid_success() {
        var productRegisterService = Mockito.mock(ProductRegisterService.class);
        var productRegisterTypeService = Mockito.mock(ProductRegisterTypeService.class);

        var accountRequestValidationService = new AccountRequestValidationService(
                productRegisterService,
                productRegisterTypeService
        );

        var expected = "";
        Assertions.assertEquals(expected, accountRequestValidationService.isValidRequest(requestValid));
    }

    @Test
    void isValid_returnError() {
        var productRegisterService = Mockito.mock(ProductRegisterService.class);
        var productRegisterTypeService = Mockito.mock(ProductRegisterTypeService.class);

        var accountRequestValidationService = new AccountRequestValidationService(
                productRegisterService,
                productRegisterTypeService
        );

        var expected = "instanseId <> не заполнено.";
        Assertions.assertEquals(expected, accountRequestValidationService.isValidRequest(requestNotValid));
    }

    @Test
    void isExistsByProductIdAndType_returnError() {
        var productRegisterService = Mockito.mock(ProductRegisterService.class);
        var productRegisterTypeService = Mockito.mock(ProductRegisterTypeService.class);

        var product = new Product();
        product.setId(instantId);
        var productRegister = new ProductRegister();
        productRegister.setProduct(product);
        var productRegisterType = new ProductRegisterType();
        productRegisterType.setRegisterTypeName(registryTypeCode);
        productRegister.setProductRegisterType(productRegisterType);
        List<ProductRegister> productRegisters = new ArrayList<>();
        productRegisters.add(productRegister);
        Mockito.when(productRegisterService.findByProductId(instantId)).thenReturn(productRegisters);

        var accountRequestValidationService = new AccountRequestValidationService(
                productRegisterService,
                productRegisterTypeService
        );

        var expected = "Параметр registryTypeCode тип регистра <REGISTRY_TYPE_CODE_VALUE> уже существует для ЭП с ИД  <IDS>.".replaceAll("REGISTRY_TYPE_CODE_VALUE", registryTypeCode);
        expected = expected.replaceAll("IDS", instantId + ",");
        var actual = accountRequestValidationService.isExistsByProductIdAndType(requestValid);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void isExistsByProductIdAndType_success() {
        var productRegisterService = Mockito.mock(ProductRegisterService.class);
        var productRegisterTypeService = Mockito.mock(ProductRegisterTypeService.class);

        var product = new Product();
        product.setId(instantId);
        var productRegister = new ProductRegister();
        productRegister.setProduct(product);

        List<ProductRegister> productRegisters = new ArrayList<>();
        Mockito.when(productRegisterService.findByProductId(instantId)).thenReturn(productRegisters);

        var accountRequestValidationService = new AccountRequestValidationService(
                productRegisterService,
                productRegisterTypeService
        );

        var expected = "";
        var actual = accountRequestValidationService.isExistsByProductIdAndType(requestValid);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void isExistsProductCode_success() {
        var productRegisterService = Mockito.mock(ProductRegisterService.class);
        var productRegisterTypeService = Mockito.mock(ProductRegisterTypeService.class);

        var product = new Product();
        product.setId(instantId);
        product.setProductCodeId("1234");
        var productClass = new ProductClass();
        productClass.setGlobalCode(registryTypeCode);
        var productRegisterType = new ProductRegisterType();
        productRegisterType.setProductClass(productClass);
        productRegisterType.setValue(registryTypeCode);
        List<ProductRegisterType> productRegisterTypes = new ArrayList<>();
        productRegisterTypes.add(productRegisterType);

        Mockito.when(productRegisterTypeService.findAllByValue(any())).thenReturn(productRegisterTypes);

        var accountRequestValidationService = new AccountRequestValidationService(
                productRegisterService,
                productRegisterTypeService
        );

        var expected = "";
        var actual = accountRequestValidationService.isExistsProductCode(requestValid, product);
        Assertions.assertEquals(expected, actual);
    }
}
