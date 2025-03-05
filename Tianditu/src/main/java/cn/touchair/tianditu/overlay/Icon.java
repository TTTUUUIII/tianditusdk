package cn.touchair.tianditu.overlay;

import cn.touchair.tianditu.entity.Point;
import cn.touchair.tianditu.util.JsonObject;

public class Icon implements JsonObject {

    public static final Icon DEFAULT = null;
    public static final Icon CENTER_POINT = new Icon("assets/img/icon_center_point_new@2x.png", new Point(19, 19));
    public static final Icon START = new Icon("assets/img/icon_start@2x.png", new Point(28, 35));
    public static final Icon END = new Icon("assets/img/icon_end@2x.png", new Point(28, 35));
    public static final Icon PIN_RED = new Icon("assets/img/pin_red@2x.png", new Point(30, 30));

    public final String iconUrl;
    public final Point iconSize;
    public final Point iconAnchor;

    public Icon(String iconUrl) {
        this(iconUrl, new Point(25, 41), new Point(12, 41));
    }

    public Icon(String iconUrl, Point iconSize) {
        this(iconUrl, iconSize, new Point(12, 41));
    }

    public Icon(String iconUrl, Point iconSize, Point iconAnchor) {
        this.iconUrl = iconUrl;
        this.iconSize = iconSize;
        this.iconAnchor = iconAnchor;
    }
}
