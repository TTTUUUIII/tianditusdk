package cn.touchair.tianditu.map;

import cn.touchair.tianditu.entity.LngLat;
import cn.touchair.tianditu.entity.LngLatBounds;
import cn.touchair.tianditu.util.JsonObject;

public class TMapOptions implements JsonObject {

    public static final String EPSG_900913 = "EPSG:900913";
    public static final String EPSG_4326 = "EPSG:4326";

    public String projection = EPSG_900913;
    public Number minZoom = null;
    public Number maxZoom = null;
    public LngLatBounds maxBounds = null;
    public LngLat center = new LngLat(116.40769, 39.89945);
    public int zoom = 12;
    public boolean enableCopyrightControl = false;
}
