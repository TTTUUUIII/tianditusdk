package cn.touchair.tianditu.entity;

import cn.touchair.tianditu.util.JsonObject;

public class Point implements JsonObject {
    public final int x;
    public final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
