package cn.touchair.tianditu.overlay;

import cn.touchair.tianditu.entity.TLngLat;
import cn.touchair.tianditu.entity.TPoint;
import cn.touchair.tianditu.util.JsonObject;

public class TInfoWindow implements JsonObject {
    public int minWidth = 50;
    public int maxWidth = 300;
    public int maxHeight = 400;
    public boolean autoPan = false;
    public  boolean closeButton = true;
    public TPoint offset = new TPoint(0, 7);
    public  TPoint autoPanPadding = new TPoint(5, 5);
    public boolean closeOnClick = true;
    public final String content;
    public final TLngLat position;

    public TInfoWindow(String content, TLngLat position) {
        this.content = content;
        this.position = position;
    }
}
