package com.diogo.cookup.utils;

import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.NavDestination;

/**
 * Navegação segura: só navega se a ação existir a partir do destino atual.
 * Evita o crash IllegalArgumentException ("destination is unknown") em
 * duplo-toque rápido ou navegações repetidas.
 */
public final class NavUtils {

    private NavUtils() {}

    public static void navigateSafe(NavController navController, int actionId) {
        navigateSafe(navController, actionId, null);
    }

    public static void navigateSafe(NavController navController, int actionId, Bundle args) {
        if (navController == null) return;
        NavDestination current = navController.getCurrentDestination();
        if (current == null || current.getAction(actionId) == null) return;
        if (args != null) {
            navController.navigate(actionId, args);
        } else {
            navController.navigate(actionId);
        }
    }
}
