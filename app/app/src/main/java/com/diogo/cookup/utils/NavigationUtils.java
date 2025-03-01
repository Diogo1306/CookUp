package com.diogo.cookup.utils;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class NavigationUtils {

    public static void setupBackButton(Activity activity, int buttonId) {
        if (activity == null) return;
        ImageButton backButton = activity.findViewById(buttonId);
        if (backButton != null) {
            backButton.setOnClickListener(v -> activity.onBackPressed());
        }
    }

    public static void setupBackButton(Fragment fragment, View rootView, int buttonId) {
        if (fragment == null || rootView == null) return;
        ImageButton backButton = rootView.findViewById(buttonId);
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                FragmentActivity activity = fragment.getActivity();
                if (activity != null) {
                    activity.onBackPressed();
                }
            });
        }
    }
}
