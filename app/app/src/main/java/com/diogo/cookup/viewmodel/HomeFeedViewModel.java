package com.diogo.cookup.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diogo.cookup.data.model.HomeFeedData;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.data.repository.HomeFeedRepository;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class HomeFeedViewModel extends ViewModel {

    private final HomeFeedRepository repository;

    private final MutableLiveData<List<RecipeData>> recommendedRecipes = new MutableLiveData<>();
    private final MutableLiveData<List<RecipeData>> weeklyRecipes = new MutableLiveData<>();
    private final MutableLiveData<List<RecipeData>> popularRecipes = new MutableLiveData<>();
    private final MutableLiveData<List<RecipeData>> categoryRecipes1 = new MutableLiveData<>();
    private final MutableLiveData<List<RecipeData>> categoryRecipes2 = new MutableLiveData<>();
    private final MutableLiveData<List<RecipeData>> categoryRecipes3 = new MutableLiveData<>();

    private final MutableLiveData<String> categoryTitle1 = new MutableLiveData<>();
    private final MutableLiveData<String> categoryTitle2 = new MutableLiveData<>();
    private final MutableLiveData<String> categoryTitle3 = new MutableLiveData<>();

    private final MutableLiveData<String> categoryName1 = new MutableLiveData<>();
    private final MutableLiveData<String> categoryName2 = new MutableLiveData<>();
    private final MutableLiveData<String> categoryName3 = new MutableLiveData<>();

    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public HomeFeedViewModel() {
        repository = new HomeFeedRepository();
    }

    public LiveData<List<RecipeData>> getRecommendedRecipes() { return recommendedRecipes; }
    public LiveData<List<RecipeData>> getWeeklyRecipes() { return weeklyRecipes; }
    public LiveData<List<RecipeData>> getPopularRecipes() { return popularRecipes; }
    public LiveData<List<RecipeData>> getCategoryRecipes1() { return categoryRecipes1; }
    public LiveData<List<RecipeData>> getCategoryRecipes2() { return categoryRecipes2; }
    public LiveData<List<RecipeData>> getCategoryRecipes3() { return categoryRecipes3; }

    public LiveData<String> getCategoryTitle1() { return categoryTitle1; }
    public LiveData<String> getCategoryTitle2() { return categoryTitle2; }
    public LiveData<String> getCategoryTitle3() { return categoryTitle3; }

    public LiveData<String> getCategoryName1() { return categoryName1; }
    public LiveData<String> getCategoryName2() { return categoryName2; }
    public LiveData<String> getCategoryName3() { return categoryName3; }

    public LiveData<String> getErrorMessage() { return errorMessage; }

    public void loadHomeFeed(int userId) {
        repository.getFullHomeFeed(userId, new HomeFeedRepository.HomeFeedCallback() {
            @Override
            public void onSuccess(HomeFeedData data) {
                recommendedRecipes.postValue(data.getRecommended());
                weeklyRecipes.postValue(data.getWeekly());
                popularRecipes.postValue(data.getPopular());

                if (data.getCategories() != null) {
                    if (data.getCategories().containsKey("cat1")) {
                        categoryRecipes1.postValue(data.getCategories().get("cat1").getRecipes());

                        String name = data.getCategories().get("cat1").getCategoryName();
                        categoryName1.postValue(name); // Nome real
                        String[] options = {
                                "Favoritos em: %s",
                                "Top receitas de %s",
                                "Sabores de %s"
                        };
                        categoryTitle1.postValue(getRandomTitle(name, options));
                    }

                    if (data.getCategories().containsKey("cat2")) {
                        categoryRecipes2.postValue(data.getCategories().get("cat2").getRecipes());

                        String name = data.getCategories().get("cat2").getCategoryName();
                        categoryName2.postValue(name); // Nome real
                        String[] options = {
                                "Podes gostar de: %s",
                                "Recomendado: %s",
                                "Explora: %s"
                        };
                        categoryTitle2.postValue(getRandomTitle(name, options));
                    }

                    if (data.getCategories().containsKey("cat3")) {
                        categoryRecipes3.postValue(data.getCategories().get("cat3").getRecipes());

                        String name = data.getCategories().get("cat3").getCategoryName();
                        categoryName3.postValue(name); // Nome real
                        String[] options = {
                                "Mais de %s",
                                "Descobre %s",
                                "Inspiração: %s"
                        };
                        categoryTitle3.postValue(getRandomTitle(name, options));
                    }
                }
            }

            @Override
            public void onError(String error) {
                errorMessage.postValue(error);
            }
        });
    }

    private String getRandomTitle(String categoryName, String[] options) {
        if (categoryName == null || categoryName.isEmpty()) return "Sugestões para ti";
        int index = new Random().nextInt(options.length);
        return String.format(Locale.ROOT, options[index], categoryName.toLowerCase(Locale.ROOT));
    }
}
