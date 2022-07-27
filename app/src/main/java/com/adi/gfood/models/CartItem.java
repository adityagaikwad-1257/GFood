package com.adi.gfood.models;

import java.io.Serializable;

public class CartItem implements Serializable {
    private Integer quantity, price;
    private String uid;

    private Dish dish;

    private Offer offer;

    public CartItem() {
    }

    public CartItem(Integer quantity, Integer price, String uid) {
        this.quantity = quantity;
        this.price = price;
        this.uid = uid;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
