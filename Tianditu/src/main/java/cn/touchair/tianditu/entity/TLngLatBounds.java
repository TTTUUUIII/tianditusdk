package cn.touchair.tianditu.entity;

import cn.touchair.tianditu.util.JsonObject;

public class TLngLatBounds implements JsonObject {
    public final TLngLat sw;
    public final TLngLat ne;

    public TLngLatBounds(TLngLat sw, TLngLat ne) {
        this.sw = sw;
        this.ne = ne;
    }
}
