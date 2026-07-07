package com.diogo.cookup.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diogo.cookup.data.model.CategoryData;
import com.diogo.cookup.data.model.IngredientData;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.data.repository.CategoryRepository;
import com.diogo.cookup.data.repository.IngredientRepository;
import com.diogo.cookup.data.repository.RecipeRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecipeCreateEditViewModel extends ViewModel {
    private final CategoryRepository categoryRepository = new CategoryRepository();
    private final RecipeRepository recipeRepository = new RecipeRepository();
    private final IngredientRepository ingredientRepository = new IngredientRepository();

    private final MutableLiveData<List<CategoryData>> categoriesLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<IngredientData>> ingredientsLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Object>> imagesLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<IngredientData>> ingredientSuggestionsLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<RecipeData> recipeToEditLiveData = new MutableLiveData<>();

    // GETTERS
    public LiveData<List<CategoryData>> getCategories() { return categoriesLiveData; }
    public LiveData<String> getError() { return errorLiveData; }
    public LiveData<List<IngredientData>> getIngredients() { return ingredientsLiveData; }
    public LiveData<List<IngredientData>> getIngredientSuggestions() { return ingredientSuggestionsLiveData; }
    public LiveData<RecipeData> getRecipeToEdit() { return recipeToEditLiveData; }
    public LiveData<List<Object>> getImages() { return imagesLiveData; }

    public void fetchCategories() {
        categoryRepository.getCategories(categoriesLiveData, errorLiveData);
    }

    public void fetchRecipeById(int recipeId) {
        recipeRepository.getRecipeDetail(recipeId, recipeToEditLiveData, errorLiveData);
        recipeToEditLiveData.observeForever(recipe -> {
            if (recipe != null) {
                setIngredients(recipe.getIngredients() != null ? recipe.getIngredients() : new ArrayList<>());
                List<Object> galeria = new ArrayList<>();
                if (recipe.getGallery() != null) {
                    galeria.addAll(recipe.getGallery());
                }
                imagesLiveData.setValue(galeria);
            }
        });
    }

    public void addIngredient(IngredientData ingredient) {
        List<IngredientData> list = new ArrayList<>(ingredientsLiveData.getValue());
        list.add(ingredient);
        ingredientsLiveData.setValue(list);
    }

    public void removeIngredient(int index) {
        List<IngredientData> list = new ArrayList<>(ingredientsLiveData.getValue());
        if (index >= 0 && index < list.size()) {
            list.remove(index);
            ingredientsLiveData.setValue(list);
        }
    }

    public void updateIngredient(int index, IngredientData ingredient) {
        List<IngredientData> list = new ArrayList<>(ingredientsLiveData.getValue());
        if (index >= 0 && index < list.size()) {
            list.set(index, ingredient);
            ingredientsLiveData.setValue(list);
        }
    }

    public void setIngredients(List<IngredientData> ingredients) {
        ingredientsLiveData.setValue(ingredients);
    }

    public void addImage(Object image) {
        List<Object> list = new ArrayList<>(imagesLiveData.getValue());
        list.add(image);
        imagesLiveData.setValue(list);
    }

    public void removeImage(int index) {
        List<Object> list = new ArrayList<>(imagesLiveData.getValue());
        if (index >= 0 && index < list.size()) {
            list.remove(index);
            imagesLiveData.setValue(list);
        }
    }

    public void setImages(List<Object> images) {
        imagesLiveData.setValue(images);
    }

    public void fetchIngredientSuggestions(String query) {
        if (query == null || query.trim().length() < 2) {
            ingredientSuggestionsLiveData.setValue(new ArrayList<>());
            return;
        }
        ingredientRepository.fetchSuggestions(query, ingredientSuggestionsLiveData, errorLiveData);
    }

    public LiveData<RecipeData> saveOrUpdateRecipe(
            Integer recipeId,
            int authorId,
            String title,
            String description,
            String instructions,
            String difficulty,
            int preparationTime,
            int servings,
            List<Integer> selectedCategoryIds,
            List<File> imagensNovas,
            List<String> imagensAntigas
    ) {
        MutableLiveData<RecipeData> resultLiveData = new MutableLiveData<>();

        recipeRepository.saveOrUpdateRecipe(
                recipeId,
                authorId,
                title,
                description,
                instructions,
                difficulty,
                preparationTime,
                servings,
                selectedCategoryIds,
                ingredientsLiveData.getValue(),
                imagensNovas,
                imagensAntigas
        ).enqueue(new retrofit2.Callback<com.diogo.cookup.data.model.ApiResponse<RecipeData>>() {
            @Override
            public void onResponse(retrofit2.Call<com.diogo.cookup.data.model.ApiResponse<RecipeData>> call, retrofit2.Response<com.diogo.cookup.data.model.ApiResponse<RecipeData>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    resultLiveData.postValue(response.body().getData());
                } else {
                    errorLiveData.postValue("Erro ao salvar receita.");
                }
            }
            @Override
            public void onFailure(retrofit2.Call<com.diogo.cookup.data.model.ApiResponse<RecipeData>> call, Throwable t) {
                errorLiveData.postValue("Erro de rede: " + t.getMessage());
            }
        });
        return resultLiveData;
    }

    public void clearForm() {
        ingredientsLiveData.setValue(new ArrayList<>());
        imagesLiveData.setValue(new ArrayList<>());
        ingredientSuggestionsLiveData.setValue(new ArrayList<>());
        recipeToEditLiveData.setValue(null);
    }
}
