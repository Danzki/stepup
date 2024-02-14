package com.danzki;

import java.util.HashMap;
import java.util.Map;

public class AccountState {
    final private String owner;
    final private Map<Currency, Integer> amountCcy = new HashMap<>();

    public AccountState(String owner, Map<Currency, Integer> amountCcy) {
        this.owner = owner;
        for(Map.Entry<Currency, Integer> entry : amountCcy.entrySet()) {
            this.amountCcy.put(entry.getKey(), entry.getValue());
        }
    }

    public String getOwner() {
        return owner;
    }

    public Map<Currency, Integer> getAmountCcy() {
        return amountCcy;
    }


}
