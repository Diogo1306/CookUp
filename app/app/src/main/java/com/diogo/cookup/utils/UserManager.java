package com.diogo.cookup.utils;

import android.content.Context;
import android.util.Log;

import com.diogo.cookup.data.model.UserData;

public class UserManager {
    private static final String TAG = "USER_MANAGER";

    public static UserData getCurrentUser(Context context) {
        UserData user = SharedPrefHelper.getInstance(context).getUser();
        if (user != null && user.getUserId() > 0) {
            return user;
        } else {
            Log.e(TAG, "Utilizador não encontrado ou inválido.");
            return null;
        }
    }

    public static boolean isUserLoggedIn(Context context) {
        return getCurrentUser(context) != null;
    }

    public static void saveUser(Context context, UserData user) {
        SharedPrefHelper.getInstance(context).saveUser(user);
        Log.d(TAG, "Utilizador salvo localmente: " + user.getUsername());
    }

    public static void clearUser(Context context) {
        SharedPrefHelper.getInstance(context).clearUser();
        Log.d(TAG, "Utilizador removido localmente.");
    }
}
