package com.danzki;

import static com.danzki.Currency.*;

public class Main {
    public static void main(String[] args) {
        var account = new Account("Pete");
        account.setAmountByCcy(RUB, 100);
        var save1 = account.save();
        System.out.println(account);
        account.setOwner("Василий Иванов");
        System.out.println(account);
        account.setAmountByCcy(Currency.RUB, 300);
        var save2 = account.save();
        System.out.println(account);
        System.out.println("---------------------------");
        System.out.println("Undo 1 (RUB -> 100):");
        account.undo();
        System.out.println(account);
        System.out.println("Undo 2 (Owner -> Pete):");
        account.undo();
        System.out.println(account);
        System.out.println("Undo 3 (Delete RUB):");
        account.undo();
        System.out.println(account);
        System.out.println("---------------------------");
        System.out.println("Change save1 -> add USD -> 100");
        System.out.println("save1: " + save1);
        System.out.println("account: " +  account);

        System.out.println("Change save2 -> owner Igor");
        System.out.println("save2: " + save2);
        System.out.println("account: " +  account);
    }
}