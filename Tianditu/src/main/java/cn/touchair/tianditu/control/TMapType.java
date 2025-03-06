package cn.touchair.tianditu.control;

public class TMapType implements TControl {

    private static final String TMAP_NORMAL_MAP = "TMAP_NORMAL_MAP"; /*传统*/
    private static final String TMAP_NORMAL_MAP_ICON = "https://api.tianditu.gov.cn/v4.0/image/map/maptype/vector.png"; /*传统*/
    private static final String TMAP_SATELLITE_MAP = "TMAP_SATELLITE_MAP"; /*卫星*/
    private static final String TMAP_SATELLITE_MAP_ICON = "https://api.tianditu.gov.cn/v4.0/image/map/maptype/satellite.png"; /*卫星*/
    private static final String TMAP_HYBRID_MAP = "TMAP_HYBRID_MAP"; /*卫星混合*/
    private static final String TMAP_HYBRID_MAP_ICON = "https://api.tianditu.gov.cn/v4.0/image/map/maptype/satellitepoi.png"; /*卫星混合*/
    private static final String TMAP_TERRAIN_MAP = "TMAP_TERRAIN_MAP"; /*地形*/
    private static final String TMAP_TERRAIN_MAP_ICON = "https://api.tianditu.gov.cn/v4.0/image/map/maptype/terrain.png"; /*地形*/
    private static final String TMAP_TERRAIN_HYBRID_MAP = "TMAP_TERRAIN_HYBRID_MAP"; /*地形混合*/
    private static final String TMAP_TERRAIN_HYBRID_MAP_ICON = "https://api.tianditu.gov.cn/v4.0/image/map/maptype/terrainpoi.png"; /*地形混合*/


    private String title;
    private final String icon;
    private final String layer;

    public TMapType(String title, String icon, String layer) {
        this.title = title;
        this.icon = icon;
        this.layer = layer;
    }

    public static final TMapType NORMAL = new TMapType(
            "传统",
            TMAP_NORMAL_MAP_ICON,
            "TMAP_NORMAL_MAP"
    );

    public static final TMapType SATELLITE = new TMapType(
            "卫星",
            TMAP_SATELLITE_MAP_ICON,
            "TMAP_SATELLITE_MAP"
    );

    public static final TMapType HYBRID = new TMapType(
            "卫星混合",
            TMAP_HYBRID_MAP_ICON,
            "TMAP_HYBRID_MAP"
    );

    public static final TMapType TERRAIN = new TMapType(
            "地形",
            TMAP_TERRAIN_MAP_ICON,
            "TMAP_TERRAIN_MAP"
    );

    public static final TMapType TERRAIN_HYBRID = new TMapType(
            "地形混合",
            TMAP_TERRAIN_HYBRID_MAP_ICON,
            "TMAP_TERRAIN_HYBRID_MAP"
    );

}
