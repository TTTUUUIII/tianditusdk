package cn.touchair.tianditu.entity;

import cn.touchair.tianditu.util.JsonObject;

public class LngLatBounds implements JsonObject {
    public final LngLat sw;
    public final LngLat ne;

    public LngLatBounds(LngLat sw, LngLat ne) {
        this.sw = sw;
        this.ne = ne;
    }
}
