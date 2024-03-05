package com.danzki;

public interface ErrorMessage {
   String INSTANCE_ID = "INSTANCE_ID";
   String ERROR_NECESSARY_FIELDS = "instanseId <INSTANCE_ID> не заполнено.";
   String REGISTRY_TYPE_CODE_VALUE = "REGISTRY_TYPE_CODE_VALUE";
   String IDS = "IDS";
   String ERROR_PR_EXISTS = "Параметр registryTypeCode тип регистра <REGISTRY_TYPE_CODE_VALUE> уже существует для ЭП с ИД  <IDS>.";
   String PRODUCT_CODE = "PRODUCT_CODE";
   String BD_TABLE_NAME = "BD_TABLE_NAME";
   String ERROR_PRODUCT_NOT_FOUND = "КодПродукта <PRODUCT_CODE> не найдено в Каталоге продуктов <BD_TABLE_NAME> для данного типа Регистра";
   String POOL_PARAMS = "POOL_PARAMS";
   String PRODUCT_NOT_FOUND = "Продукт <INSTANCE_ID> не найден.";
   String ACCOUNT_NOT_FOUND = "Счет не найден в пуле счетов <POOL_PARAMS> не найден.";
   String PRODUCT_NUM = "PRODUCT_NUM";
   String VALUE = "VALUE";
   String PRODUCT_EXISTS = "Параметр ContractNumber № договора <PRODUCT_NUM> уже существует для ЭП с ИД  <IDS>.";
   String ROLL_EXISTS = "Параметр № Дополнительного соглашения (сделки) Number <VALUE> уже существует для ЭП с ИД  <IDS>.";
   String ACCOUNT_TYPE_NOT_FOUND = "AccountType со значение <VALUE> не найден.";
   String PRODUCT_REGISTRY_NOT_FOUND = "КодПродукта <VALUE> не найдено в Каталоге продуктов <BD_TABLE_NAME>";
}
