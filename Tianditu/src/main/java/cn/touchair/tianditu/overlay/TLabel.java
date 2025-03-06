package cn.touchair.tianditu.overlay;

import cn.touchair.tianditu.entity.TLngLat;
import cn.touchair.tianditu.entity.TPoint;
import cn.touchair.tianditu.util.JsonObject;

public class TLabel implements JsonObject {
    public final String text;
    public final TLngLat position;
    public TPoint offset = new TPoint(0, 0);

    public TLabel(String text, TLngLat position) {
        this.text = text;
        this.position = position;
    }
}
