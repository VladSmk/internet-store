package ua.internet.store.model;

import javax.validation.constraints.Size;

public class BankCard {
    @Size(min = 3, max = 3, message = "should be 3 digits")
    private String code;
    @Size(min = 16, max = 16, message = "should be 16 digits")
    private String cvc;
    public BankCard(){}
    public BankCard(String code, String cvc) {
        this.code = code;
        this.cvc = cvc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }
}
