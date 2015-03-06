package com.appkernel.http;

import android.os.Build;

/**
 */
public class HttpApiUtils {

    public static String buildUserAgent(String packageName, String versionName, int versionCode) {

        return Build.FINGERPRINT.replace("/release-keys", "").replace(",release-keys", "") +
            packageName + "/" + versionName + "." + versionCode ;
    }
}
