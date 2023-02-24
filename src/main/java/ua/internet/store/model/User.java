package ua.internet.store.model;

import javax.validation.constraints.NotNull;

public class User {
    @NotNull
    private int accountId;
    @NotNull
    private String accountName;
    @NotNull
    private String accountPassword;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }
}
