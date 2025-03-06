package cn.touchair.tianditu.overlay;

import cn.touchair.tianditu.entity.TLngLat;
import cn.touchair.tianditu.util.JsonObject;

public class TMarker implements JsonObject {
    public final TLngLat lngLat;
    public TIcon icon = TIcon.DEFAULT;
    public boolean draggable = false;
    public String title = "";
    public int zIndexOffset = 0;
    public float opacity = 1.0f;
    public TMarker(TLngLat lngLat) {
        this.lngLat = lngLat;
    }

    public TMarker(TLngLat lngLat, TIcon icon) {
        this.lngLat = lngLat;
        this.icon = icon;
    }
}
