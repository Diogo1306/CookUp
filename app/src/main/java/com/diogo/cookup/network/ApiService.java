package com.diogo.cookup.network;

import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.data.model.UserData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import java.util.List;

public interface ApiService {

    @POST("api.php")
    Call<ApiResponse<UserData>> createOrUpdateUser(@Body UserData user);

    @GET("api.php")
    Call<ApiResponse<UserData>> getUser(@Query("firebase_uid") String firebase_uid);

    @GET("api.php")
    Call<ApiResponse<List<RecipeData>>> getAllRecipes(@Query("all_recipes") boolean allRecipes);
}
