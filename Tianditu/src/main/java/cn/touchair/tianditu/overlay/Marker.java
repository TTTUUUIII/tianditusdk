package cn.touchair.tianditu.overlay;

import cn.touchair.tianditu.entity.LngLat;
import cn.touchair.tianditu.util.JsonObject;

public class Marker implements JsonObject {
    public final LngLat lngLat;
    public Icon icon = Icon.DEFAULT;
    public boolean draggable = false;
    public String title = "";
    public int zIndexOffset = 0;
    public float opacity = 1.0f;
    public Marker(LngLat lngLat) {
        this.lngLat = lngLat;
    }

    public Marker(LngLat lngLat, Icon icon) {
        this.lngLat = lngLat;
        this.icon = icon;
    }
}
