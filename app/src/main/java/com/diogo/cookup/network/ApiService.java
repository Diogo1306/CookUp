package com.diogo.cookup.network;

import com.diogo.cookup.data.model.*;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    // === USERS ===
    @POST("api.php")
     Call<ApiResponse<UserData>> saveUser(@Query("route") String route, @Body UserData user);

    @GET("api.php")
    Call<ApiResponse<UserData>> getUser(@Query("route") String route, @Query("firebase_uid") String firebaseUid);

    @Multipart
    @POST("api.php?route=update_profile")
    Call<ApiResponse<UserData>> updateProfile(@Part("user_id") RequestBody userId, @Part("username") RequestBody username, @Part MultipartBody.Part profile_picture);

    @POST("api.php")
    Call<ApiResponse<Void>> deleteUser(@Query("route") String route, @Query("user_id") int userId);

    // === RECIPES ===
    @Multipart
    @POST("api.php?route=recipes")
    Call<ApiResponse<RecipeData>> saveOrUpdateRecipe(
            @Part("recipe_id") RequestBody recipeId,
            @Part("author_id") RequestBody authorId,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part("instructions") RequestBody instructions,
            @Part("difficulty") RequestBody difficulty,
            @Part("preparation_time") RequestBody preparationTime,
            @Part("servings") RequestBody servings,
            @Part("categories") RequestBody categoriesJson,
            @Part("ingredients") RequestBody ingredientsJson,
            @Part List<MultipartBody.Part> gallery,
            @Part("old_gallery") RequestBody oldGallery
    );

    @FormUrlEncoded
    @POST("api.php")
    Call<ApiResponse<Void>> deleteRecipe(@Query("route") String route, @Field("recipe_id") int recipeId);

    @GET("api.php")
    Call<ApiResponse<List<RecipeData>>> getAllRecipes(@Query("route") String route);

    @GET("api.php")
    Call<ApiResponse<RecipeData>> getRecipeById(@Query("route") String route, @Query("recipe_id") int recipeId);

    @GET("api.php")
    Call<ApiResponse<List<RecipeData>>> getPopularRecipesWithPage(@Query("route") String route, @Query("page") int page);

    @GET("api.php")
    Call<ApiResponse<List<RecipeData>>> getMostFavoritedRecipes(@Query("route") String route);

    @GET("api.php")
    Call<ApiResponse<List<RecipeData>>> getByCategory(@Query("route") String route, @Query("category_id") int categoryId);

    @GET("api.php")
    Call<ApiResponse<List<RecipeData>>> getRecommendedRecipes(@Query("route") String route);

    // === PROFILE ===
    @GET("api.php")
    Call<ApiResponse<ProfileData>> getProfileSummary(@Query("route") String route, @Query("user_id") int userId);

    @GET("api.php")
    Call<ApiResponse<List<RecipeData>>> getProfileRecipes(@Query("route") String route, @Query("user_id") int userId);

    // === HOME FEED ===
    @GET("api.php")
    Call<ApiResponse<HomeFeedData>> getFullHomeFeed(@Query("route") String route, @Query("user_id") int userId);

    @GET("api.php")
    Call<ApiResponseWithFilled<List<RecipeData>>> getWeeklyRecipes(@Query("route") String route);

    @GET("api.php")
    Call<ApiResponse<List<RecipeData>>> getPopularHomeRecipes(@Query("route") String route);

    @GET("api.php")
    Call<ApiResponseWithFilled<List<RecipeData>>> getRecipesByCategory(@Query("route") String route, @Query("category_id") int categoryId);

    // === CATEGORIES ===
    @GET("api.php")
    Call<ApiResponse<List<CategoryData>>> getCategories(@Query("route") String route);

    @GET("api.php")
    Call<ApiResponse<List<CategoryData>>> getPopularCategories(@Query("route") String route);

    @GET("api.php")
    Call<ApiResponse<List<CategoryData>>> getUserCategories(@Query("route") String route, @Query("user_id") int userId);

    // === COMMENTS ===
    @POST("api.php")
    Call<ApiResponse<Void>> submitComment(@Query("route") String route, @Body CommentRequest request);

    @GET("api.php")
    Call<ApiResponse<List<CommentData>>> getComments(@Query("route") String route, @Query("recipe_id") int recipeId);

    // === INGREDIENTS ===
    @GET("api.php")
    Call<List<IngredientData>> autocompleteIngredients(@Query("route") String route, @Query("q") String query);

    // === RATINGS ===
    @POST("api.php")
    Call<ApiResponse<Void>> submitRating(@Query("route") String route, @Body RatingRequest request);

    @GET("api.php")
    Call<ApiResponse<Float>> getAverageRating(@Query("route") String route, @Query("recipe_id") int recipeId);

    // === SAVED LISTS ===
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
    Call<ApiResponse<List<SavedListData>>> getUserLists(@Query("route") String route, @Query("user_id") int userId);

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

    @GET("api.php")
    Call<ApiResponse<List<SavedListData>>> getListsWithRecipes(@Query("route") String route, @Query("user_id") int userId);

    // === TRACKING ===
    @POST("api.php")
    Call<ApiResponse<Void>> trackInteraction(@Query("route") String route, @Body TrackRequest trackRequest);

    // === SEARCH ===
    @GET("api.php")
    Call<ApiResponse<SearchData>> searchAll(@Query("route") String route, @Query("query") String query);

    @GET("api.php")
    Call<ApiResponse<List<RecipeData>>> searchFilteredRecipes(
            @Query("route") String route,
            @Query("query") String query,
            @Query("filter") String filter,
            @Query("userId") Integer userId,
            @Query("difficulty") String difficulty,
            @Query("min_time") Integer minTime,
            @Query("max_time") Integer maxTime,
            @Query("max_ingredients") Integer maxIngredients,
            @Query("limit") int limit,
            @Query("offset") int offset
    );

    // === TEST CONNECTION ===
    @GET("test_connection.php")
    Call<ResponseBody> testConnection();
}
