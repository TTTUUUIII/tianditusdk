package cn.touchair.tianditu.map;

import cn.touchair.tianditu.entity.TLngLat;
import cn.touchair.tianditu.entity.TLngLatBounds;
import cn.touchair.tianditu.util.JsonObject;

public class TMapOptions implements JsonObject {

    public static final String EPSG_900913 = "EPSG:900913";
    public static final String EPSG_4326 = "EPSG:4326";

    public String projection = EPSG_900913;
    public Number minZoom = null;
    public Number maxZoom = null;
    public TLngLatBounds maxBounds = null;
    public TLngLat center = new TLngLat(116.40769, 39.89945);
    public int zoom = 12;
    public boolean enableCopyrightControl = false;
}
