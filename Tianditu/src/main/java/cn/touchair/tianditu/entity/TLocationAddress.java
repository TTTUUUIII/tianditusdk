package cn.touchair.tianditu.entity;

import com.google.gson.annotations.SerializedName;

import cn.touchair.tianditu.util.JsonObject;

public class TLocationAddress implements JsonObject {
    public TAddressComponent addressComponent;
    @SerializedName("formatted_address")
    public String formattedAddress;

    @Override
    public String toString() {
        return "LocationAddress{" +
                "addressComponent=" + addressComponent +
                ", formattedAddress='" + formattedAddress + '\'' +
                '}';
    }
}
