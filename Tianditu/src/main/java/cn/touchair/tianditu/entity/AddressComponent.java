package cn.touchair.tianditu.entity;

import com.google.gson.annotations.SerializedName;

import cn.touchair.tianditu.util.JsonObject;

public class AddressComponent implements JsonObject {
    public String address;
    @SerializedName("address_distance")
    public Number addressDistance;
    @SerializedName("address_position")
    public Number addressPosition;
    public String city;
    public String poi;
    @SerializedName("poi_distance")
    public Number poiDistance;
    @SerializedName("poi_position")
    public String poiPosition;
    public String road;
    @SerializedName("road_distance")
    public Number roadDistance;

    @Override
    public String toString() {
        return "AddressComponent{" +
                "address='" + address + '\'' +
                ", addressDistance=" + addressDistance +
                ", addressPosition=" + addressPosition +
                ", city='" + city + '\'' +
                ", poi='" + poi + '\'' +
                ", poiDistance=" + poiDistance +
                ", poiPosition='" + poiPosition + '\'' +
                ", road='" + road + '\'' +
                ", roadDistance=" + roadDistance +
                '}';
    }
}
