package com.diogo.cookup.network;

import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.ApiResponseWithFilled;
import com.diogo.cookup.data.model.CategoryData;
import com.diogo.cookup.data.model.CommentData;
import com.diogo.cookup.data.model.CommentRequest;
import com.diogo.cookup.data.model.RatingRequest;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.data.model.SavedListData;
import com.diogo.cookup.data.model.SearchData;
import com.diogo.cookup.data.model.TrackRequest;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.data.model.HomeFeedData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    // Users
    @POST("api.php")
    Call<ApiResponse<UserData>> createOrUpdateUser(@Query("route") String route, @Body UserData user);

    @GET("api.php")
    Call<ApiResponse<UserData>> getUser(@Query("route") String route, @Query("firebase_uid") String firebaseUid);

    // Recipes
    @GET("api.php")
    Call<ApiResponse<List<RecipeData>>> getAllRecipes(@Query("route") String route);

    @GET("api.php")
    Call<ApiResponseWithFilled<List<RecipeData>>> getWeeklyRecipes(@Query("route") String route);

    @GET("api.php")
    Call<ApiResponse<List<RecipeData>>> getPopularRecipesWithPage(@Query("route") String route, @Query("page") int page);

    @GET("api.php")
    Call<ApiResponse<List<RecipeData>>> getRecommendedRecipes(@Query("route") String route);

    @GET("api.php")
    Call<ApiResponse<List<RecipeData>>> getUserRecommendedRecipes(@Query("route") String route, @Query("user_id") int userId);

    @GET("api.php")
    Call<ApiResponse<List<RecipeData>>> getRecommendedRecipesByUser(@Query("route") String route, @Query("user_id") int userId);

    @GET("api.php")
    Call<ApiResponseWithFilled<List<RecipeData>>> getRecipesByCategory(@Query("route") String route, @Query("category_id") int categoryId);

    @GET("api.php")
    Call<ApiResponse<HomeFeedData>> getFullHomeFeed(@Query("route") String route, @Query("user_id") int userId);

    @GET("api.php")
    Call<ApiResponse<RecipeData>> getRecipeDetail(@Query("route") String route, @Query("action") String action, @Query("recipe_id") int recipeId);

    // FallbackRecipes
    @POST("api.php")
    Call<ApiResponseWithFilled<List<RecipeData>>> getFallbackRecipes();

    //Search

    @GET("api.php")
    Call<ApiResponse<SearchData>> searchAll(@Query("route") String route, @Query("query") String query);

    // Categories
    @GET("api.php")
    Call<ApiResponse<List<CategoryData>>> getCategories(@Query("route") String route);

    @GET("api.php")
    Call<ApiResponse<List<CategoryData>>> getUserCategories(@Query("route") String route, @Query("user_id") int userId);

    @GET("api.php")
    Call<ApiResponse<List<CategoryData>>> getPopularCategories(@Query("route") String route);

    @GET("api.php")
    Call<ApiResponseWithFilled<List<Integer>>> getTopUserCategories(@Query("route") String route, @Query("user_id") int userId);

    // Saved Lists
    @FormUrlEncoded
    @POST("api.php")
    Call<ApiResponse<Void>> createList(@Query("route") String route, @Field("user_id") int userId, @Field("list_name") String listName, @Field("color") String color);

    @FormUrlEncoded
    @POST("api.php")
    Call<ApiResponse<Void>> updateList(@Query("route") String route, @Field("list_id") int listId, @Field("list_name") String name, @Field("color") String color);

    @FormUrlEncoded
    @POST("api.php")
    Call<ApiResponse<Void>> removeRecipeFromList(@Query("route") String route, @Field("list_id") int listId, @Field("recipe_id") int recipeId);

    @GET("api.php")
    Call<ApiResponse<List<SavedListData>>> getUserLists(@Query("route") String route, @Query("action") String action, @Query("user_id") int userId);

    @FormUrlEncoded
    @POST("api.php")
    Call<ApiResponse<Void>> addRecipeToList(@Query("route") String route, @Field("list_id") int listId, @Field("recipe_id") int recipeId);

    @GET("api.php")
    Call<ApiResponse<List<RecipeData>>> getRecipesFromList(@Query("route") String route, @Query("list_id") int listId);

    @GET("api.php")
    Call<ApiResponse<List<Integer>>> getRecipeListIds(@Query("route") String route, @Query("recipe_id") int recipeId);

    @GET("api.php")
    Call<ApiResponse<Void>> deleteList(@Query("route") String route, @Query("list_id") int listId);

    @GET("api.php")
    Call<ApiResponse<List<Integer>>> getSavedRecipeIds(@Query("route") String route, @Query("user_id") int userId);

    // Ratings & Comments
    @GET("api.php")
    Call<ApiResponse<Float>> getAverageRating(@Query("route") String route, @Query("recipe_id") int recipeId);

    @POST("api.php")
    Call<ApiResponse<Void>> submitRating(@Query("route") String route, @Body RatingRequest request);

    @GET("api.php")
    Call<ApiResponse<List<CommentData>>> getComments(@Query("route") String route, @Query("recipe_id") int recipeId);

    @POST("api.php")
    Call<ApiResponse<Void>> submitComment(@Query("route") String route, @Body CommentRequest request);

    // Tracking
    @POST("api.php")
    Call<ApiResponse<Void>> trackInteraction(@Query("route") String route, @Body TrackRequest trackRequest);

}
