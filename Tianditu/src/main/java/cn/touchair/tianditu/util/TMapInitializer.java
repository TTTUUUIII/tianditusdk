package cn.touchair.tianditu.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import java.util.Objects;

import cn.touchair.tianditu.map.TMapOptions;

public final class TMapInitializer {

    public static final String METADATA_API_KEY = "cn.gov.tianditu.API_KEY";

    private TMapInitializer() {
        throw new RuntimeException();
    }
    private static String key;
    private static TMapOptions options;
    public static void initialize(@NonNull String key, TMapOptions options) {
        Objects.requireNonNull(key);
        TMapInitializer.key = key;
        TMapInitializer.options = options;
    }

    public static void initialize(@NonNull String key) {
        initialize(key, new TMapOptions());
    }

    public static void initialize(@NonNull Context applicationContext, TMapOptions options) {
        try {
            ApplicationInfo applicationInfo = applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), PackageManager.GET_META_DATA).applicationInfo;
            assert applicationInfo != null;
            String key = applicationInfo.metaData.getString(METADATA_API_KEY);
            assert key != null;
            initialize(key, options);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void initialize(@NonNull Context applicationContext) {
        initialize(applicationContext, new TMapOptions());
    }

    public static String getKey() {
        return key;
    }

    public static String getOptions() {
        return options.toJson();
    }
}
