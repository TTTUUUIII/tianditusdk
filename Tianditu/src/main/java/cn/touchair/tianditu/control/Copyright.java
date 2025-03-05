package cn.touchair.tianditu.control;

import androidx.annotation.NonNull;

import cn.touchair.tianditu.entity.LngLatBounds;

public class Copyright implements Control {
    public final String id;
    public final String content;
    public LngLatBounds bounds = null;

    public Copyright(@NonNull String id, @NonNull String content) {
        this.id = id;
        this.content = content;
    }
}
