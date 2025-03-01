package com.diogo.cookup.network;

import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.data.model.UserData;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
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
}
