package cn.touchair.tianditu.overlay;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

import cn.touchair.tianditu.entity.TPoint;
import cn.touchair.tianditu.util.JsonObject;

public class TIcon implements JsonObject {

    public static final TIcon DEFAULT = null;
    public static final TIcon CENTER_POINT = new TIcon("assets/img/icon_center_point_new@2x.png", new TPoint(19, 19));
    public static final TIcon START = new TIcon("assets/img/icon_start@2x.png", new TPoint(28, 35));
    public static final TIcon END = new TIcon("assets/img/icon_end@2x.png", new TPoint(28, 35));
    public static final TIcon PIN_RED = new TIcon("assets/img/pin_red@2x.png", new TPoint(30, 30));

    public final String iconUrl;
    public final TPoint iconSize;
    public final TPoint iconAnchor;

    public TIcon(String iconUrl) {
        this(iconUrl, new TPoint(25, 41), new TPoint(12, 41));
    }

    public TIcon(String iconUrl, TPoint iconSize) {
        this(iconUrl, iconSize, new TPoint(iconSize.x / 2, iconSize.y / 2));
    }

    public TIcon(String iconUrl, TPoint iconSize, TPoint iconAnchor) {
        this.iconUrl = iconUrl;
        this.iconSize = iconSize;
        this.iconAnchor = iconAnchor;
    }

    public static TIcon fromDrawable(final Resources resources, @DrawableRes int drawableRes, @Nullable TPoint size) {
        Bitmap bitmap = BitmapFactory.decodeResource(resources, drawableRes);
        return fromBitmap(bitmap, size);
    }

    public static TIcon fromDrawable(final Resources resources, @DrawableRes int drawableRes) {
        Bitmap bitmap = BitmapFactory.decodeResource(resources, drawableRes);
        return fromBitmap(bitmap, null);
    }

    public static TIcon fromBitmap(final Bitmap bitmap, @Nullable TPoint size) {
        if (size == null) {
            size = new TPoint(bitmap.getWidth(), bitmap.getHeight());
        }
        TPoint anchor = new TPoint(size.x / 2, size.y / 2);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        bitmap.recycle();
        final String data = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        return new TIcon("data:image/png;base64," + data, size, anchor);
    }
}
