package com.diogo.cookup.data.model;

import java.util.List;
import java.util.Map;

public class HomeFeedData {

    private List<RecipeData> recommended;
    private List<RecipeData> weekly;
    private List<RecipeData> popular;
    private Map<String, CategoryData> categories;

    public List<RecipeData> getRecommended() {
        return recommended;
    }

    public List<RecipeData> getWeekly() {
        return weekly;
    }

    public List<RecipeData> getPopular() {
        return popular;
    }

    public Map<String, CategoryData> getCategories() {
        return categories;
    }
}
