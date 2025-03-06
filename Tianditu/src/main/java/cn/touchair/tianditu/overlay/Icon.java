package cn.touchair.tianditu.overlay;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

import cn.touchair.tianditu.entity.Point;
import cn.touchair.tianditu.util.JsonObject;

public class Icon implements JsonObject {

    public static final Icon DEFAULT = null;
    public static final Icon CENTER_POINT = new Icon("assets/img/icon_center_point_new@2x.png", new Point(19, 19));
    public static final Icon START = new Icon("assets/img/icon_start@2x.png", new Point(28, 35));
    public static final Icon END = new Icon("assets/img/icon_end@2x.png", new Point(28, 35));
    public static final Icon PIN_RED = new Icon("assets/img/pin_red@2x.png", new Point(30, 30));

    public final String iconUrl;
    public final Point iconSize;
    public final Point iconAnchor;

    public Icon(String iconUrl) {
        this(iconUrl, new Point(25, 41), new Point(12, 41));
    }

    public Icon(String iconUrl, Point iconSize) {
        this(iconUrl, iconSize, new Point(iconSize.x / 2, iconSize.y / 2));
    }

    public Icon(String iconUrl, Point iconSize, Point iconAnchor) {
        this.iconUrl = iconUrl;
        this.iconSize = iconSize;
        this.iconAnchor = iconAnchor;
    }

    public static Icon fromDrawable(final Resources resources, @DrawableRes int drawableRes, @Nullable Point size) {
        Bitmap bitmap = BitmapFactory.decodeResource(resources, drawableRes);
        return fromBitmap(bitmap, size);
    }

    public static Icon fromDrawable(final Resources resources, @DrawableRes int drawableRes) {
        Bitmap bitmap = BitmapFactory.decodeResource(resources, drawableRes);
        return fromBitmap(bitmap, null);
    }

    public static Icon fromBitmap(final Bitmap bitmap, @Nullable Point size) {
        if (size == null) {
            size = new Point(bitmap.getWidth(), bitmap.getHeight());
        }
        Point anchor = new Point(size.x / 2, size.y / 2);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        bitmap.recycle();
        final String data = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        return new Icon("data:image/png;base64," + data, size, anchor);
    }
}
