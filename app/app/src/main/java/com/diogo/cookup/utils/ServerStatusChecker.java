package com.diogo.cookup.utils;

import com.diogo.cookup.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.ResponseBody;

public class ServerStatusChecker {

    public static void checkServer(ApiService apiService, final ServerStatusCallback callback) {
        apiService.testConnection().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                callback.onResult(response.isSuccessful());
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onResult(false);
            }
        });
    }

    public interface ServerStatusCallback {
        void onResult(boolean isOnline);
    }
}
