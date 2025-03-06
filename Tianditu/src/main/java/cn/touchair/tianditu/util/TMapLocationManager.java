package cn.touchair.tianditu.util;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

public abstract class TMapLocationManager implements LocationListener {
    private static final String TAG = "TMapLocationManager";

    public static final int SINGLE            = 1;
    public static final int CYCLE             = 2;

    private final TLocationOptions options;
    private final LocationManager lm;

    public TMapLocationManager(@NonNull Context context) {
        this(context, new TLocationOptions());
    }

    public TMapLocationManager(@NonNull Context context, TLocationOptions options) {
        this.options = options;
        this.lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);;
    }

    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    public void startLocation() {
        startLocation(SINGLE);
    }

    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    public void startLocation(int mode) {
        if (mode == CYCLE) {
            lm.requestLocationUpdates(
                    options.provider,
                    options.minTime,
                    options.minDistance,
                    this
            );
        } else {
            lm.requestSingleUpdate(
                    options.provider,
                    this,
                    Looper.getMainLooper()
            );
        }
    }

    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    public void stopLocation() {
        lm.removeUpdates(this);
    }

    public static class TLocationOptions {
        public String provider = LocationManager.GPS_PROVIDER;
        private final long minTime = 10000L;
        private final float minDistance = 10.0f;

        public TLocationOptions() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                provider = LocationManager.FUSED_PROVIDER;
            }
        }
    }
}
