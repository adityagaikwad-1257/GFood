package com.adi.gfood.models;

import java.io.Serializable;

public class Offer implements Serializable {
    private Dish dish;

    private Integer discountedPrice
            , discountPercentage;

    private String offerName
            , offerUid;

    public Offer(){}

    public Offer(Dish dish, Integer discountedPrice, Integer discountPercentage, String offerName) {
        this.dish = dish;
        this.discountedPrice = discountedPrice;
        this.discountPercentage = discountPercentage;
        this.offerName = offerName;
    }

    public String getOfferUid() {
        return offerUid;
    }

    public void setOfferUid(String offerUid) {
        this.offerUid = offerUid;
    }

    public Dish getDish() {
        return dish;
    }

    public Integer getDiscountedPrice() {
        return discountedPrice;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public String getOfferName() {
        return offerName;
    }
}
