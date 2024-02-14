package com.danzki;

import java.util.ArrayDeque;

public class AccountCommandHistory {
    private ArrayDeque<AccountCommand> history = new ArrayDeque<>();

    public void push(AccountCommand c) {
        history.push(c);
    }

    public AccountCommand pop() {
        return history.pop();
    }

    public boolean isEmpty() { return history.isEmpty(); }

}
