package com.diogo.cookup.network;

import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.UserData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("public/script.php")
    Call<ApiResponse> sendUserData(@Body UserData userData);
}
