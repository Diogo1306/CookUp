package com.diogo.cookup.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.diogo.cookup.data.model.UserData;
import com.google.gson.Gson;

public class SharedPrefHelper {
    private static SharedPrefHelper instance;

    private static final String PREF_NAME = "cookup_prefs";
    private static final String KEY_USER = "user";

    private final SharedPreferences sharedPref;
    private final SharedPreferences.Editor editor;
    private final Gson gson;

    private SharedPrefHelper(Context context) {
        sharedPref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        gson = new Gson();
    }

    public static synchronized SharedPrefHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefHelper(context);
        }
        return instance;
    }

    public void saveUser(UserData user) {
        clearUser();
        String json = gson.toJson(user);
        editor.putString(KEY_USER, json);
        editor.commit();
    }


    public UserData getUser() {
        String json = sharedPref.getString(KEY_USER, null);
        if (json != null) {
            return gson.fromJson(json, UserData.class);
        }
        return null;
    }

    public void clearUser() {
        editor.remove(KEY_USER);
        editor.commit();
    }
}

