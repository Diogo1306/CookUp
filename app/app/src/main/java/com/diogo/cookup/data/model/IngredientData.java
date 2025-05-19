package com.diogo.cookup.data.model;

import com.google.gson.annotations.SerializedName;

public class IngredientData {


    @SerializedName("ingredient_id")
    private int id;
    @SerializedName("ingredient_name")
    private String name;
    @SerializedName("ingredient_image")
    private String image;
    @SerializedName("ingredient_quantity")
    private String quantity;

    public IngredientData(String name, String quantity, String image) {
        this.name = name;
        this.quantity = quantity;
        this.image = image;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public String getQuantity() { return quantity; }

    public void setQuantity(String quantity) { this.quantity = quantity; }
}
