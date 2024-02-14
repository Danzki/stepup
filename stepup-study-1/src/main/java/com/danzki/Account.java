package com.danzki;

import java.util.HashMap;
import java.util.Map;

public class Account {
    private String owner;
    private Boolean isUndo;
    private Map<Currency, Integer> amountCcy = new HashMap<>();

    private AccountCommandHistory commandHistory;

    public Account(String owner) {
        validateOwner(owner);
        this.owner = owner;
        this.commandHistory = new AccountCommandHistory();
        this.isUndo = false;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        validateOwner(owner);
        executeCommand(new AccountCommandOwner(this));
        this.owner = owner;
    }

    private void validateOwner(String owner) {
        if (owner == null) {
            throw new IllegalArgumentException("Owner cannot be null");
        }
        if (owner.equals("")) {
            throw new IllegalArgumentException("Owner cannot be empty");
        }
    }

    public Map<Currency, Integer> getAmountCcy() {
        return amountCcy;
    }

    public void setAmountByCcy(Currency ccy, Integer amount) {
        validateAmount(amount);
        executeCommand(new AccountCommandCcyAmount(this));
        amountCcy.put(ccy, amount);
    }

    private void validateAmount(Integer amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
    }

    public void undo() {
        if (commandHistory.isEmpty()) return;
        this.isUndo = true;
        var ac = commandHistory.pop();
        if (ac != null) {
            ac.undo();
        }
    }

    @Override
    public String toString() {
        var str = "Account{" +
                        "owner='" + owner + "\'\n " +
                        "Currencies: " + "\n";
        for(Map.Entry<Currency, Integer> entry : this.getAmountCcy().entrySet()) {
            str += "\t" + entry.getKey().toString() + " : " + entry.getValue();
        }
        str += "\n" + "}";

        return str;
    }

    private void executeCommand(AccountCommand ac) {
        if (!this.isUndo) {
            if (ac.execute()) {
                commandHistory.push(ac);
            }
        }
        this.isUndo = false;
    }

    public AccountState save() {
        var state = new AccountState(this.getOwner(), this.getAmountCcy());
        return state;
    }

    public static Account restore(AccountState state) {
        var restoredAccount = new Account(state.getOwner());
        for(Map.Entry<Currency, Integer> entry : state.getAmountCcy().entrySet()) {
            restoredAccount.amountCcy.put(entry.getKey(), entry.getValue());
        }
        return restoredAccount;
    }
}
