package com.diogo.cookup.utils;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class AuthUtils {
    public static boolean isGoogleLogin(FirebaseUser user) {
        if (user == null) return false;
        for (UserInfo userInfo : user.getProviderData()) {
            if ("google.com".equals(userInfo.getProviderId())) {
                return true;
            }
        }
        return false;
    }

    public static String getProvider(FirebaseUser user) {
        if (user == null) return "";
        for (UserInfo userInfo : user.getProviderData()) {
            String providerId = userInfo.getProviderId();
            if ("google.com".equals(providerId) || "password".equals(providerId)) {
                return providerId;
            }
        }
        return "";
    }
}