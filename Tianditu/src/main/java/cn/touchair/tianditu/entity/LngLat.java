package cn.touchair.tianditu.entity;

import android.location.Location;

import java.util.Objects;

import cn.touchair.tianditu.util.JsonObject;

public class LngLat implements JsonObject {
    public final Number lng;
    public final Number lat;

    public LngLat(Number lng, Number lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public LngLat(Location location) {
        this(location.getLongitude(), location.getLatitude());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LngLat lngLat = (LngLat) o;
        return Objects.equals(lng, lngLat.lng) && Objects.equals(lat, lngLat.lat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lng, lat);
    }
}
