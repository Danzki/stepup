package com.danzki;

import java.util.HashMap;
import java.util.Map;

public class AccountCommandCcyAmount implements AccountCommand {
    final private Account account;
    private Map<Currency, Integer> backup = new HashMap<>();

    public AccountCommandCcyAmount(Account account) {
        this.account = account;
    }

    @Override
    public void undo() {
        for(Map.Entry<Currency, Integer> entry : account.getAmountCcy().entrySet()) {
            Boolean isExists = false;
            if (backup.containsKey(entry.getKey())) {
                isExists = true;
            }
            if (!isExists) {
                account.getAmountCcy().remove(entry.getKey());
            }
        }

        for(Map.Entry<Currency, Integer> entry : backup.entrySet()) {
            account.setAmountByCcy(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void saveBackup() {
        for(Map.Entry<Currency, Integer> entry : account.getAmountCcy().entrySet()) {
            this.backup.put(entry.getKey(), entry.getValue());
        }
    }
}
