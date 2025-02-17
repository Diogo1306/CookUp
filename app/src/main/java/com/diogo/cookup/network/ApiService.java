package com.diogo.cookup.network;

import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.UserData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("api.php")
    Call<ApiResponse> createOrUpdateUser(@Body UserData user);

    @GET("api.php")
    Call<ApiResponse> getUser(@Query("firebase_uid") String firebase_uid);
}
