package ua.internet.store.model;

import javax.validation.constraints.NotNull;

public class Basket {
    @NotNull
    private int user_id;
    @NotNull
    private int product_id;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }
}
