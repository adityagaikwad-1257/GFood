package com.adi.gfood.models;

import java.io.Serializable;

public class Dish implements Serializable {
    private String dishName
            , dishDescription
            , dishContent
            , dishImage
            , dishCategory;

    private Integer actualDishPrice
            , ourDishPrice;

    private String dishUid;

    private Integer bestDeal;

    public Dish() {
    }

    public Dish(String dishName, String dishDescription, String dishContent, String dishImage, String dishCategory, Integer actualDishPrice, Integer ourDishPrice) {
        this.dishName = dishName;
        this.dishDescription = dishDescription;
        this.dishContent = dishContent;
        this.dishImage = dishImage;
        this.dishCategory = dishCategory;
        this.actualDishPrice = actualDishPrice;
        this.ourDishPrice = ourDishPrice;
    }

    public Integer getBestDeal() {
        return bestDeal;
    }

    public void setBestDeal(Integer bestDeal) {
        this.bestDeal = bestDeal;
    }

    public Integer getActualDishPrice() {
        return actualDishPrice;
    }

    public Integer getOurDishPrice() {
        return ourDishPrice;
    }

    public void setDishUid(String dishUid) {
        this.dishUid = dishUid;
    }

    public String getDishName() {
        return dishName;
    }

    public String getDishDescription() {
        return dishDescription;
    }

    public String getDishContent() {
        return dishContent;
    }

    public String getDishImage() {
        return dishImage;
    }

    public String getDishCategory() {
        return dishCategory;
    }

    public String getDishUid() {
        return dishUid;
    }
}
