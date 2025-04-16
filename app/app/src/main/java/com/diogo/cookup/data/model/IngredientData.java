package com.diogo.cookup.data.model;

public class IngredientData {
    private String name;
    private String full_entry;

    public IngredientData(String name, String full_entry) {
        this.name = name;
        this.full_entry = full_entry;
    }

    public String getName() {
        return name;
    }

    public String getFull_entry() {
        return full_entry;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFull_entry(String full_entry) {
        this.full_entry = full_entry;
    }
}
