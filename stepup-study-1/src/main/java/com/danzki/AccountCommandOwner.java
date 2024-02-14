package com.danzki;

public class AccountCommandOwner implements AccountCommand {
    final private Account account;
    private String backup;
    public AccountCommandOwner(Account account) {
        this.account = account;
    }

    @Override
    public void saveBackup() {
        backup = account.getOwner();
    }

    public void undo() {
        account.setOwner(backup);
    }
}
