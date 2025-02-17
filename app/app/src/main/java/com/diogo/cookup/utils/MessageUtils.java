package com.diogo.cookup.utils;

import android.graphics.Color;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class MessageUtils {

    public static void showSnackbar(View view, String message, int color) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(color);
        snackbar.show();
    }
}
