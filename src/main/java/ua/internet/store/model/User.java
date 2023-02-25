package ua.internet.store.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class User {

    private int accountId;
    @Size(min = 3,  max = 40, message = "Name should be between 3 and 40 characters")
    private String accountName;
    @Size(min = 6,  max = 20, message = "Password should be between 6 and 20 characters")
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
