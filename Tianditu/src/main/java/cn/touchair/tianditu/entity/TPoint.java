package cn.touchair.tianditu.entity;

import cn.touchair.tianditu.util.JsonObject;

public class TPoint implements JsonObject {
    public final int x;
    public final int y;

    public TPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
