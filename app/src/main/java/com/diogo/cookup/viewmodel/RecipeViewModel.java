package com.diogo.cookup.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diogo.cookup.data.model.CommentData;
import com.diogo.cookup.data.model.CommentRequest;
import com.diogo.cookup.data.model.RatingRequest;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.data.repository.RecipeRepository;

import java.util.ArrayList;
import java.util.List;

public class RecipeViewModel extends ViewModel {

    private final RecipeRepository recipeRepository;
    private final MutableLiveData<List<RecipeData>> recipesLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<List<Integer>> savedRecipeIds = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<RecipeData> recipeDetail = new MutableLiveData<>();
    private final MutableLiveData<List<CommentData>> commentsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Float> averageRatingLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> ratingSuccess = new MutableLiveData<>();

    public RecipeViewModel() {
        recipeRepository = new RecipeRepository();
    }

    public LiveData<List<RecipeData>> getRecipesLiveData() {
        return recipesLiveData;
    }

    public LiveData<RecipeData> getRecipeDetailLiveData() {
        return recipeDetail;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<List<Integer>> getSavedRecipeIds() {
        return savedRecipeIds;
    }

    public LiveData<List<CommentData>> getCommentsLiveData() {
        return commentsLiveData;
    }

    public LiveData<Float> getAverageRatingLiveData() {
        return averageRatingLiveData;
    }

    public LiveData<Boolean> getRatingSuccessLiveData() {
        return ratingSuccess;
    }

    public void loadRecipes() {
        recipeRepository.getAllRecipes(recipesLiveData, errorMessage);
    }

    public void loadRecipeDetail(int recipeId) {
        recipeRepository.getRecipeDetail(recipeId, recipeDetail, errorMessage);
    }

    public void loadComments(int recipeId) {
        recipeRepository.getComments(recipeId, commentsLiveData, errorMessage);
    }

    public void loadAverageRating(int recipeId) {
        recipeRepository.getAverageRating(recipeId, averageRatingLiveData, errorMessage);
    }

    public void submitRating(RatingRequest request) {
        recipeRepository.submitRating(request, ratingSuccess, errorMessage);
    }

    public void submitComment(int userId, int recipeId, String comment) {
        CommentRequest request = new CommentRequest(userId, recipeId, comment);
        recipeRepository.submitComment(request, ratingSuccess, errorMessage);
    }

    public void submitRatingAndComment(int userId, int recipeId, float rating, String comment) {
        if (rating > 0f) {
            RatingRequest ratingRequest = new RatingRequest(userId, recipeId, (int) rating);
            recipeRepository.submitRating(ratingRequest, ratingSuccess, errorMessage);
        }

        if (!comment.isEmpty()) {
            CommentRequest commentRequest = new CommentRequest(userId, recipeId, comment);
            recipeRepository.submitComment(commentRequest, ratingSuccess, errorMessage);
        }
    }
}
