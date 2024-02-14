package com.danzki;

public interface AccountCommand {

    default boolean execute() {
        saveBackup();
        return true;
    }
    void undo();

    void saveBackup();
}
