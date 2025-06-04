package com.diogo.cookup.data.model;

import java.io.Serializable;

public class ProfileData implements Serializable {
    private int total_recipes;
    private float average_rating;
    private int total_views;
    private int finished_count;

    public int getTotalRecipes() { return total_recipes; }
    public float getAverageRating() { return average_rating; }
    public int getTotalViews() { return total_views; }
    public int getFinishedCount() { return finished_count; }
}
