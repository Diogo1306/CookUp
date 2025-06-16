package com.diogo.cookup.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NoShowBottomNavigationBehavior extends CoordinatorLayout.Behavior<BottomNavigationView> {

    public NoShowBottomNavigationBehavior() {}

    public NoShowBottomNavigationBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private static boolean forceHide = false;

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, BottomNavigationView child, int layoutDirection) {
        Log.d("BottomNavBehavior", "onLayoutChild chamado - forceHide=" + forceHide);

        if (forceHide) {
            child.setVisibility(View.GONE);
        } else {
            child.setVisibility(View.VISIBLE);
        }
        return super.onLayoutChild(parent, child, layoutDirection);
    }


    public static void forceLayout(BottomNavigationView bottomNav) {
        if (bottomNav.getParent() instanceof CoordinatorLayout) {
            ((CoordinatorLayout) bottomNav.getParent()).requestLayout();
        }
    }

    public static void hideForever() {
        forceHide = true;
    }

    public static void showAgain() {
        forceHide = false;
    }
}
