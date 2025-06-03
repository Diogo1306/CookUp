package com.diogo.cookup.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetworkChangeReceiver extends BroadcastReceiver {
    public interface NetworkChangeListener {
        void onNetworkChanged(boolean isConnected);
    }
    private NetworkChangeListener listener;
    public NetworkChangeReceiver(NetworkChangeListener listener) {
        this.listener = listener;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isConnected = ConnectivityHelper.isNetworkAvailable(context);
        listener.onNetworkChanged(isConnected);
    }
}

