package cn.touchair.tianditu.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

public class TMapLocationManager implements LocationListener {
    private static final String TAG = "TMapLocationManager";

    public static final int SINGLE            = 1;
    public static final int CYCLE             = 2;

    private final LocationOptions options;
    private final LocationManager lm;

    public TMapLocationManager(@NonNull Context context) {
        this(context, new LocationOptions());
    }

    public TMapLocationManager(@NonNull Context context, LocationOptions options) {
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

    @Override
    public void onLocationChanged(@NonNull Location location) {
    }

    public static class LocationOptions {
        public String provider = LocationManager.GPS_PROVIDER;
        private final long minTime = 10000L;
        private final float minDistance = 10.0f;
    }
}
