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

/**
 * Activity base para monitorar internet e servidor,
 * mostrando layouts de erro e controlando o BottomNavigationView automaticamente.
 */
public abstract class BaseConnectivityActivity extends AppCompatActivity {
    private NetworkChangeReceiver receiver;
    private ApiService apiService;
    private View layoutNetworkError, layoutServerError, layoutContent;

    // Handler e runnable para checagem periódica automática
    private final int SERVER_CHECK_INTERVAL = 5000; // 5 segundos (ajuste se quiser)
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

    /**
     * Inicializa os layouts e começa a monitorar rede e servidor.
     * Chame este método no onCreate das suas Activities filhas
     */
    protected void initConnectivityLayouts(View layoutNetworkError, View layoutServerError, View layoutContent, ApiService apiService) {
        this.layoutNetworkError = layoutNetworkError;
        this.layoutServerError = layoutServerError;
        this.layoutContent = layoutContent;
        this.apiService = apiService;

        // Cria e registra receiver para monitorar mudanças na rede
        receiver = new NetworkChangeReceiver(isConnected -> {
            if (!isConnected) showNetworkError();
            else checkServer();
        });
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        // Checagem inicial ao abrir tela
        if (!ConnectivityHelper.isNetworkAvailable(this)) showNetworkError();
        else checkServer();
    }

    /**
     * Testa o servidor remoto via Retrofit e mostra o layout adequado
     */
    private void checkServer() {
        // Mostra o loading apenas no erro de servidor
        showCheckingServer(layoutServerError);

        ServerStatusChecker.checkServer(apiService, isOnline -> runOnUiThread(() -> {
            hideCheckingServer(layoutServerError);
            if (isOnline) showContent();
            else showServerError();
        }));
    }

    /**
     * Mostra o layout de erro de rede e esconde o resto (incluindo o menu)
     */
    protected void showNetworkError() {
        setViewVisible(layoutNetworkError, true);
        setViewVisible(layoutServerError, false);
        setViewVisible(layoutContent, false);
        setViewVisible(findViewById(R.id.bottom_navigation), false);
    }

    /**
     * Mostra o layout de erro de servidor (XAMPP/Banco) e esconde o resto
     */
    protected void showServerError() {
        setViewVisible(layoutNetworkError, false);
        setViewVisible(layoutServerError, true);
        setViewVisible(layoutContent, false);
        setViewVisible(findViewById(R.id.bottom_navigation), false);
    }

    /**
     * Mostra o conteúdo normal da tela e o menu
     */
    protected void showContent() {
        setViewVisible(layoutNetworkError, false);
        setViewVisible(layoutServerError, false);
        setViewVisible(layoutContent, true);
        setViewVisible(findViewById(R.id.bottom_navigation), true);
    }

    // Progress Bar
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

    /**
     * Utilitário para facilitar show/hide de Views
     */
    private void setViewVisible(View view, boolean visible) {
        if (view != null) view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void recheckConnection() {
        if (!ConnectivityHelper.isNetworkAvailable(this)) {
            showNetworkError();
        } else {
            // Mostra loading enquanto testa o servidor
            showCheckingServer(layoutServerError);
            checkServer();
        }
    }

    // Checagem periódica automática do servidor
    @Override
    protected void onResume() {
        super.onResume();
        // Inicia a checagem automática ao entrar na Activity
        serverCheckHandler.postDelayed(serverCheckRunnable, SERVER_CHECK_INTERVAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Para a checagem automática ao sair da Activity
        serverCheckHandler.removeCallbacks(serverCheckRunnable);
    }

    /**
     * Sempre desregistra o receiver ao destruir a Activity, evitando memory leaks/crashes
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            try {
                unregisterReceiver(receiver);
            } catch (IllegalArgumentException ignored) {}
            receiver = null;
        }
        serverCheckHandler.removeCallbacks(serverCheckRunnable);
    }
}
