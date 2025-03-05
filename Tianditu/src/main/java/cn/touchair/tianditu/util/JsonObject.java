package cn.touchair.tianditu.util;

import com.google.gson.Gson;

public interface JsonObject {
    Gson gson = new Gson();

    default String toJson() {
        return gson.toJson(this);
    }

    static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
