package com.diogo.cookup.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import com.google.android.material.snackbar.Snackbar;
import java.util.LinkedList;
import java.util.Queue;

public class MessageUtils {

    private static final Queue<Runnable> snackbarQueue = new LinkedList<>();
    private static boolean isShowingSnackbar = false;

    public static void showSnackbar(View view, String message, int color) {
        snackbarQueue.add(() -> showSnackbarInternal(view, message, color, null, null, 0));
        if (!isShowingSnackbar) {
            showNextSnackbar();
        }
    }

    public static void showSnackbarWithDelay(View view, String message, int color, Context context, Class<?> nextActivity, int delay) {
        snackbarQueue.add(() -> showSnackbarInternal(view, message, color, context, nextActivity, delay));
        if (!isShowingSnackbar) {
            showNextSnackbar();
        }
    }

    private static void showSnackbarInternal(View view, String message, int color, Context context, Class<?> nextActivity, int delay) {
        isShowingSnackbar = true;

        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(color);
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                isShowingSnackbar = false;
                showNextSnackbar();
            }
        });
        snackbar.show();

        if (context != null && nextActivity != null) {
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(context, nextActivity);
                context.startActivity(intent);
            }, delay);
        }
    }

    private static void showNextSnackbar() {
        if (!snackbarQueue.isEmpty()) {
            snackbarQueue.poll().run();
        }
    }
}
