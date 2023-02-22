package ua.internet.store.model;

import javax.validation.constraints.NotNull;

public class Users {
    @NotNull
    private int accountId;
    @NotNull
    private String accountName;
    @NotNull
    private String accountPassword;
    public Users(){

    }

    public Users(int accountId) {
        this.accountId = accountId;
    }

    public Users(int accountId, String accountName, String accountPassword) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.accountPassword = accountPassword;
    }

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
