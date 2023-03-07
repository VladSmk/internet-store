package ua.internet.store.model;

public class UserPassword {
    private int userId;
    private String newPassword1;
    private String newPassword2;
    private String oldPassword;
    public UserPassword(){}

    public UserPassword(int userId, String newPassword1, String newPassword2, String oldPassword) {
        this.userId = userId;
        this.newPassword1 = newPassword1;
        this.newPassword2 = newPassword2;
        this.oldPassword = oldPassword;
    }

    public String getNewPassword1() {
        return newPassword1;
    }

    public int getUserId() {
        return userId;
    }

    public UserPassword(int userId) {
        this.userId = userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setNewPassword1(String newPassword1) {
        this.newPassword1 = newPassword1;
    }

    public String getNewPassword2() {
        return newPassword2;
    }

    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
