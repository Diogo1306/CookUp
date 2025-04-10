package com.diogo.cookup.network;

import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.data.model.SavedListData;
import com.diogo.cookup.data.model.UserData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {


    @POST("api.php")
    Call<ApiResponse<UserData>> createOrUpdateUser(
            @Query("route") String route,
            @Body UserData user
    );

    @GET("api.php")
    Call<ApiResponse<UserData>> getUser(
            @Query("route") String route,
            @Query("firebase_uid") String firebaseUid
    );

    @GET("api.php")
    Call<ApiResponse<List<RecipeData>>> getAllRecipes(
            @Query("route") String route
    );

    @FormUrlEncoded
    @POST("api.php")
    Call<ApiResponse<Void>> createList(
            @Query("route") String route,
            @Field("user_id") int userId,
            @Field("list_name") String listName,
             @Field("color") String color
    );

    @FormUrlEncoded
    @POST("api.php")
    Call<ApiResponse<Void>> updateList(
            @Query("route") String route,
            @Field("list_id") int listId,
            @Field("list_name") String name,
            @Field("color") String color
    );

    @FormUrlEncoded
    @POST("api.php")
    Call<ApiResponse<Void>> removeRecipeFromList(
            @Query("route") String route,
            @Field("list_id") int listId,
            @Field("recipe_id") int recipeId
    );

    @GET("api.php")
    Call<ApiResponse<List<SavedListData>>> getUserLists(
            @Query("route") String route,
            @Query("action") String action,
            @Query("user_id") int userId
    );

    @FormUrlEncoded
    @POST("api.php")
    Call<ApiResponse<Void>> addRecipeToList(
            @Query("route") String route,
            @Field("list_id") int listId,
            @Field("recipe_id") int recipeId
    );

    @GET("api.php")
    Call<ApiResponse<List<RecipeData>>> getRecipesFromList(
            @Query("route") String route,
            @Query("list_id") int listId
    );

    @GET("api.php")
    Call<ApiResponse<List<Integer>>> getRecipeListIds(
            @Query("route") String route,
            @Query("recipe_id") int recipeId
    );

    @GET("api.php")
    Call<ApiResponse<Void>> deleteList(
            @Query("route") String route,
            @Query("list_id") int listId
    );

    @GET("api.php")
    Call<ApiResponse<List<Integer>>> getSavedRecipeIds(
            @Query("route") String route,
            @Query("user_id") int userId
    );

}