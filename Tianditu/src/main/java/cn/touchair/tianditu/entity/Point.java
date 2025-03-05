package cn.touchair.tianditu.entity;

import cn.touchair.tianditu.util.JsonObject;

public class Point implements JsonObject {
    public final Number x;
    public final Number y;

    public Point(Number x, Number y) {
        this.x = x;
        this.y = y;
    }
}
