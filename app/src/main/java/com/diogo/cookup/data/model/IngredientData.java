package com.diogo.cookup.data.model;

public class IngredientData {
    private String name;
    private String quantity;
    private String image;

    public IngredientData(String name, String quantity, String image) {
        this.name = name;
        this.quantity = quantity;
        this.image = image;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getQuantity() {
        return quantity;
    }
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}