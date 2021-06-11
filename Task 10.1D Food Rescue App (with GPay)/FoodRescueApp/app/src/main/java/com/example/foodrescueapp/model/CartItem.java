package com.example.foodrescueapp.model;

public class CartItem {
    private int cart_id;
    private String buyer_email;
    private int food_bought_id;

    public CartItem(String buyer_email, int food_bought_id) {
        this.buyer_email = buyer_email;
        this.food_bought_id = food_bought_id;
    }

    public CartItem(int cart_id, String buyer_email, int food_bought_id) {
        this.cart_id = cart_id;
        this.buyer_email = buyer_email;
        this.food_bought_id = food_bought_id;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public String getBuyer_email() {
        return buyer_email;
    }

    public void setBuyer_email(String buyer_email) {
        this.buyer_email = buyer_email;
    }

    public int getFood_bought_id() {
        return food_bought_id;
    }

    public void setFood_bought_id(int food_bought_id) {
        this.food_bought_id = food_bought_id;
    }
}
