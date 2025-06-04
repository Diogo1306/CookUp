package com.diogo.cookup.ui.activity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.diogo.cookup.R;
import com.diogo.cookup.network.ApiService;
import com.diogo.cookup.utils.ConnectivityHelper;
import com.diogo.cookup.utils.NetworkChangeReceiver;
import com.diogo.cookup.utils.ServerStatusChecker;

public abstract class BaseConnectivityActivity extends AppCompatActivity {
    private NetworkChangeReceiver receiver;
    private ApiService apiService;
    private View layoutNetworkError, layoutServerError, layoutContent;

    private final int SERVER_CHECK_INTERVAL = 5000;
    private final Handler serverCheckHandler = new Handler();
    private final Runnable serverCheckRunnable = new Runnable() {
        @Override
        public void run() {
            if (ConnectivityHelper.isNetworkAvailable(BaseConnectivityActivity.this)) {
                checkServer();
            }
            serverCheckHandler.postDelayed(this, SERVER_CHECK_INTERVAL);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initConnectivityLayouts(View layoutNetworkError, View layoutServerError, View layoutContent, ApiService apiService) {
        this.layoutNetworkError = layoutNetworkError;
        this.layoutServerError = layoutServerError;
        this.layoutContent = layoutContent;
        this.apiService = apiService;

        receiver = new NetworkChangeReceiver(isConnected -> {
            if (!isConnected) showNetworkError();
            else checkServer();
        });
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        if (!ConnectivityHelper.isNetworkAvailable(this)) showNetworkError();
        else checkServer();
    }

    private void checkServer() {
        showCheckingServer(layoutServerError);

        ServerStatusChecker.checkServer(apiService, isOnline -> runOnUiThread(() -> {
            hideCheckingServer(layoutServerError);
            if (isOnline) showContent();
            else showServerError();
        }));
    }

    protected void showNetworkError() {
        setViewVisible(layoutNetworkError, true);
        setViewVisible(layoutServerError, false);
        setViewVisible(layoutContent, false);
        setViewVisible(findViewById(R.id.bottom_navigation), false);
        hideCheckingNetwork(layoutNetworkError);
    }
    protected void showServerError() {
        setViewVisible(layoutNetworkError, false);
        setViewVisible(layoutServerError, true);
        setViewVisible(layoutContent, false);
        setViewVisible(findViewById(R.id.bottom_navigation), false);
    }

    protected void showContent() {
        setViewVisible(layoutNetworkError, false);
        setViewVisible(layoutServerError, false);
        setViewVisible(layoutContent, true);
        setViewVisible(findViewById(R.id.bottom_navigation), true);
    }

    protected void showCheckingServer(View errorLayout) {
        if (errorLayout != null) {
            View progress = errorLayout.findViewById(R.id.progress_checking);
            View btn = errorLayout.findViewById(R.id.button_try_again);
            if (progress != null) progress.setVisibility(View.VISIBLE);
            if (btn != null) btn.setVisibility(View.GONE);
        }
    }

    protected void hideCheckingServer(View errorLayout) {
        if (errorLayout != null) {
            View progress = errorLayout.findViewById(R.id.progress_checking);
            View btn = errorLayout.findViewById(R.id.button_try_again);
            if (progress != null) progress.setVisibility(View.GONE);
            if (btn != null) btn.setVisibility(View.VISIBLE);
        }
    }

    private void setViewVisible(View view, boolean visible) {
        if (view != null) view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void recheckConnection() {
        if (!ConnectivityHelper.isNetworkAvailable(this)) {
            showNetworkError(); // Mostra erro de rede e esconde o loading do botão
        } else {
            // Mostra erro de servidor, esconde conteúdo, mostra loading
            setViewVisible(layoutNetworkError, false);
            setViewVisible(layoutServerError, true);
            setViewVisible(layoutContent, false);
            setViewVisible(findViewById(R.id.bottom_navigation), false);

            showCheckingServer(layoutServerError); // <-- Mostra o progress e esconde botão
            checkServer(); // <-- Checa o servidor e depois mostra o resultado
        }
    }

    protected void showCheckingNetwork(View errorLayout) {
        if (errorLayout != null) {
            View progress = errorLayout.findViewById(R.id.progress_checking);
            View btn = errorLayout.findViewById(R.id.button_try_again);
            if (progress != null) progress.setVisibility(View.VISIBLE);
            if (btn != null) btn.setVisibility(View.GONE);
        }
    }

    protected void hideCheckingNetwork(View errorLayout) {
        if (errorLayout != null) {
            View progress = errorLayout.findViewById(R.id.progress_checking);
            View btn = errorLayout.findViewById(R.id.button_try_again);
            if (progress != null) progress.setVisibility(View.GONE);
            if (btn != null) btn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        serverCheckHandler.postDelayed(serverCheckRunnable, SERVER_CHECK_INTERVAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        serverCheckHandler.removeCallbacks(serverCheckRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            try {
                unregisterReceiver(receiver);
            } catch (IllegalArgumentException ignored) {
            }
            receiver = null;
        }
        serverCheckHandler.removeCallbacks(serverCheckRunnable);
    }
}
