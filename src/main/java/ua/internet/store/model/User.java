package ua.internet.store.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class User {

    private int accountId;
    @Size(min = 3,  max = 40, message = "Name should be between 3 and 40 characters")
    private String accountName;
    @Size(min = 6,  max = 20, message = "Password should be between 6 and 20 characters")
    private String accountPassword;
//    @Size(min = 1, max = 100, message = "min age = 0")
    private int accountAge;
    @NotEmpty(message = "not empty")
    private String accountPhoto;
    @NotEmpty(message = "not empty")
    private String accountBio;

    public User(){}
    public User(int accountId, String accountName, String accountPassword, int accountAge, String accountPhoto, String accountBio) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.accountPassword = accountPassword;
        this.accountAge = accountAge;
        this.accountPhoto = accountPhoto;
        this.accountBio = accountBio;
    }

    public User(int accountId) {
        this.accountId = accountId;
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

    public int getAccountAge() {
        return accountAge;
    }

    public void setAccountAge(int accountAge) {
        this.accountAge = accountAge;
    }

    public String getAccountPhoto() {
        return accountPhoto;
    }

    public void setAccountPhoto(String accountPhoto) {
        this.accountPhoto = accountPhoto;
    }

    public String getAccountBio() {
        return accountBio;
    }

    public void setAccountBio(String accountBio) {
        this.accountBio = accountBio;
    }
}
