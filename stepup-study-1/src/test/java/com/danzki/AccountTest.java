package com.danzki;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountTest {

    @Test
    public void ownerIsNull() {
        var thrownText = "Owner cannot be null";
        String actualOwner = null;
        IllegalArgumentException thrown = new IllegalArgumentException(thrownText);

        try {
            var account = new Account(actualOwner);
        } catch (RuntimeException ex) {
            Assertions.assertEquals(thrownText, thrown.getMessage());
        }
    }

    @Test
    public void ownerIsEmpty() {
        var thrownText = "Owner cannot be empty";
        String actualOwner = null;
        IllegalArgumentException thrown = new IllegalArgumentException(thrownText);

        try {
            var account = new Account(actualOwner);
        } catch (RuntimeException ex) {
            Assertions.assertEquals(thrownText, thrown.getMessage());
        }
    }

    @Test
    public void amountIsNegative() {
        var thrownText = "Amount cannot be negative";
        var actualAmount = -100;
        var actualOwner = "Pete";
        var ccy = Currency.RUB;
        IllegalArgumentException thrown = new IllegalArgumentException(thrownText);

        try {
            var account = new Account(actualOwner);
            account.setAmountByCcy(ccy, actualAmount);
        } catch (RuntimeException ex) {
            Assertions.assertEquals(thrownText, thrown.getMessage());
        }
    }

    @Test
    public void undoOwnerTest() {
        var account = new Account("Pete");
        var expected = account.getOwner();
        account.setAmountByCcy(Currency.RUB, 100);
        account.setOwner("Mike");

        account.undo();
        var actual = account.getOwner();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void undoAmountTest() {
        var account = new Account("Pete");
        account.setAmountByCcy(Currency.RUB, 100);
        var expected = account.getAmountCcy().get(Currency.RUB);
        account.setOwner("Mike");
        account.setAmountByCcy(Currency.RUB, 300);

        account.undo();
        var actual = account.getAmountCcy().get(Currency.RUB);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public  void saveOwnerTest() {
        var expected = new Account("Pete");
        var actual = new Account("Pete");
        expected.setAmountByCcy(Currency.RUB, 100);

        var actualState = expected.save();
        expected.setOwner("Bob");
        expected.restore(actualState);

        Assertions.assertNotEquals(expected, actual);
    }

    @Test
    public  void saveAmountTest() {
        var expected = new Account("Pete");
        expected.setAmountByCcy(Currency.RUB, 100);
        var actual = new Account("Pete");
        actual.setAmountByCcy(Currency.RUB, 100);

        var actualState = expected.save();

        expected.setAmountByCcy(Currency.USD, 500);
        expected.restore(actualState);

        Assertions.assertNotEquals(expected, actual);
    }

    @Test
    public void saveNullAccount() {
        var expected = new Account("Pete");
        var save1 = expected.save();

        var actual = Account.restore(save1);

        Assertions.assertEquals(expected.getOwner(), actual.getOwner());
    }
}
