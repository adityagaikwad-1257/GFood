package com.adi.gfood.models;

public class Category {
    private String categoryName, categoryDisplayImage;

    public Category() {
    }

    public Category(String categoryName, String categoryDisplayImage) {
        this.categoryName = categoryName;
        this.categoryDisplayImage = categoryDisplayImage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryDisplayImage() {
        return categoryDisplayImage;
    }
}
