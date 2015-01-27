package com.appkernel.http;

import android.content.pm.PackageInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.appkernel.commons.BuildConfig;

/**
 */
public class HttpApiUtils {

    public static String buildUserAgent(String packageName, String versionName, int versionCode) {
        return Build.FINGERPRINT.replace("/release-keys", "").replace(",release-keys", "") +
            packageName + "/" + versionName + "." + versionCode ;
    }

    public static @NonNull String buildUserAgent(@Nullable PackageInfo info) {
        if (info == null)
            return buildUserAgent("unknown", "0.0", 0);

        return  buildUserAgent(info.packageName, info.versionName, info.versionCode);
    }
}
