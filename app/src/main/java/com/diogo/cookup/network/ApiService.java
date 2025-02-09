package com.diogo.cookup.network;

import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.UserData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @POST("public/api.php")
    Call<ApiResponse> createOrUpdateUser(@Body UserData user);

    @GET("public/api.php")
    Call<UserData> getUser(@Query("firebase_uid") String firebase_uid);
}
