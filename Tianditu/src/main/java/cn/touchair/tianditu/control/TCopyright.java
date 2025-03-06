package cn.touchair.tianditu.control;

import androidx.annotation.NonNull;

import cn.touchair.tianditu.entity.TLngLatBounds;

public class TCopyright implements TControl {
    public final String id;
    public final String content;
    public TLngLatBounds bounds = null;

    public TCopyright(@NonNull String id, @NonNull String content) {
        this.id = id;
        this.content = content;
    }
}
