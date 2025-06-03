package com.diogo.cookup.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.diogo.cookup.R;
import com.diogo.cookup.network.ApiRetrofit;
import com.diogo.cookup.network.ApiService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthActivity extends BaseConnectivityActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_auth);

            View layoutNetworkError = findViewById(R.id.layout_network_error);
            View layoutServerError = findViewById(R.id.layout_server_error);
            View layoutContent = findViewById(R.id.auth_nav_host_fragment);

            ApiService apiService = ApiRetrofit.getApiService();

            initConnectivityLayouts(layoutNetworkError, layoutServerError, layoutContent, apiService);

            View tryAgainNetwork = layoutNetworkError.findViewById(R.id.button_try_again);
            if (tryAgainNetwork != null) {
                tryAgainNetwork.setOnClickListener(v -> recheckConnection());
            }

            View tryAgainServer = layoutServerError.findViewById(R.id.button_try_again);
            if (tryAgainServer != null) {
                tryAgainServer.setOnClickListener(v -> recheckConnection());
            }
        }
    }

}
