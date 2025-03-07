package cn.touchair.tianditu.entity;

import android.location.Location;

import java.util.Objects;

import cn.touchair.tianditu.util.JsonObject;

public class TLngLat implements JsonObject {
    public final double lng;
    public final double lat;

    public TLngLat(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public TLngLat(Location location) {
        this(location.getLongitude(), location.getLatitude());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TLngLat lngLat = (TLngLat) o;
        return Objects.equals(lng, lngLat.lng) && Objects.equals(lat, lngLat.lat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lng, lat);
    }
}
