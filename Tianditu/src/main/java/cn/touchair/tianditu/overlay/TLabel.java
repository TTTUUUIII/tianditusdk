package cn.touchair.tianditu.overlay;

import cn.touchair.tianditu.entity.TLngLat;
import cn.touchair.tianditu.entity.TPoint;
import cn.touchair.tianditu.util.JsonObject;

public class TLabel implements JsonObject {
    public final String text;
    public final TLngLat position;
    public String title = null;
    public int fontSize = 13;
    public String fontColor = "#000";
    public String backgroundColor = "#fff";
    public int borderLine = 0;
    public String borderColor = "#fff";
    public float opacity = 1f;
    public TPoint offset = new TPoint(0, 0);

    public TLabel(String text, TLngLat position) {
        this.text = text;
        this.position = position;
    }
}
