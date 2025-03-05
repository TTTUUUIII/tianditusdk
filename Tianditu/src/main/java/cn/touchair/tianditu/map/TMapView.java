package cn.touchair.tianditu.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.touchair.tianditu.R;
import cn.touchair.tianditu.control.Control;
import cn.touchair.tianditu.control.MapType;
import cn.touchair.tianditu.control.ScaleControl;
import cn.touchair.tianditu.control.ZoomControl;
import cn.touchair.tianditu.entity.LngLat;
import cn.touchair.tianditu.entity.LocationAddress;
import cn.touchair.tianditu.overlay.Icon;
import cn.touchair.tianditu.overlay.Marker;
import cn.touchair.tianditu.util.JsonObject;
import cn.touchair.tianditu.util.TMapInitializer;

public class TMapView extends FrameLayout {
    private static final int NO_ERROR                   = 0;

    public static final int STYLE_BLACK                 = 1;
    public static final int STYLE_INDIGO                = 2;

    public static final int LEFT_OF_TOP                   = 0;
    public static final int RIGHT_OF_TOP                  = 1;
    public static final int LEFT_OF_BOTTOM                = 2;
    public static final int RIGHT_OF_BOTTOM               = 3;

    private static final String TAG = "TMapView";
    private final List<LocationAddressListener> mListeners = new ArrayList<>();
    private final Deque<Runnable> mWaitList = new ArrayDeque<>();
    private boolean mApiLoaded = false;
    private final WebView mEngine = new WebView(getContext());
    private final View mProgressView = LayoutInflater.from(getContext()).inflate(R.layout.layout_loading, this, false);
    private boolean mShowScaleControl = false;
    private int mScaleControlGravity = LEFT_OF_BOTTOM;
    private boolean mShowZoomControl = false;
    private int mZoomControlGravity = LEFT_OF_TOP;
    private boolean mShowMapTypeControl = false;

    public TMapView(@NonNull Context context) {
        this(context, null);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public TMapView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mEngine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressView.setVisibility(VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressView.setVisibility(GONE);
                callJs("initTMap(\"" + TMapInitializer.getKey() +"\", " + TMapInitializer.getOptions() + ")", true);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.d(TAG, error.getDescription().toString());
            }
        };
        mEngine.setWebViewClient(webViewClient);
        mEngine.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                switch (consoleMessage.messageLevel()) {
                    case LOG:
                        Log.i(TAG, consoleMessage.message());
                        break;
                    case WARNING:
                        Log.w(TAG, consoleMessage.message());
                        break;
                    case ERROR:
                        Log.e(TAG, consoleMessage.message());
                }
                return super.onConsoleMessage(consoleMessage);
            }
        });
        mEngine.getSettings().setJavaScriptEnabled(true);
        mEngine.getSettings().setDomStorageEnabled(true);
        mEngine.getSettings().setAllowFileAccess(true);
        mEngine.addJavascriptInterface(this, "AndroidInterface");
        addView(mEngine);
        addView(mProgressView);
        mEngine.loadUrl("file:///android_asset/h5/index.html");

        try (TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.TMapView)){
            mShowScaleControl = attributes.getBoolean(R.styleable.TMapView_showScaleControl, mShowScaleControl);
            mScaleControlGravity = attributes.getInt(R.styleable.TMapView_scaleControlGravity, mScaleControlGravity);
            mShowZoomControl = attributes.getBoolean(R.styleable.TMapView_showZoomControl, mShowZoomControl);
            mZoomControlGravity = attributes.getInt(R.styleable.TMapView_zoomControlGravity, mZoomControlGravity);
            mShowMapTypeControl = attributes.getBoolean(R.styleable.TMapView_showMapTypeControl, mShowMapTypeControl);
        }
    }

    public void panTo(LngLat lngLat) {
        callJs("TMap.panTo("+ lngLat.toJson() +");");
    }

    public void addMarker(String ident, LngLat lngLat) {
        addMarker(ident, new Marker(lngLat));
    }

    public void setMyLocation(LngLat lngLat) {
        panTo(lngLat);
        removeMarker(IDENT_MY_LOCATION);
        addMarker(IDENT_MY_LOCATION, lngLat, Icon.CENTER_POINT);
        if (!mLocHistory.containsKey(lngLat)) {
            getLocationAddress(lngLat);
        }
    }

    public void addMarker(@NonNull String ident, LngLat lngLat, Icon icon) {
        addMarker(ident, new Marker(lngLat, icon));
    }

    public void addMarker(@NonNull String ident, Marker marker) {
        callJs(String.format(Locale.US, "TOverLay.addMarker(\"%s\", %s)", ident, marker.toJson()));
    }

    public void removeMarker(String ident) {
        callJs("TOverLay.removeMarker(\"" + ident + "\");");
    }

    public @Nullable LocationAddress getLastLocationAddress() {
        return mLastLocationAddress;
    }

    private void setZoomControlEnable(boolean enable) {
        if (enable) {
            ZoomControl zoomControl = new ZoomControl();
            zoomControl.position = getControlPosition(mZoomControlGravity);
            zoomControl.zoomInTitle = getContext().getString(R.string.tmap_zoom_in_title);
            zoomControl.zoomOutTitle = getContext().getString(R.string.tmap_zoom_out_title);
            callJs(String.format(Locale.US, "TControl.addZoomControl(%s);", zoomControl.toJson()));
        } else {
            callJs("TControl.removeZoomControl();");
        }
    }

    private void setScaleControlEnable(boolean enable) {
        if (enable){
            ScaleControl scaleControl = new ScaleControl();
            scaleControl.position = getControlPosition(mScaleControlGravity);
            callJs(String.format(Locale.US, "TControl.addScaleControl(%s);", scaleControl.toJson()));
        } else {
            callJs("TControl.removeScaleControl();");
        }
    }

    private void setMapTypeControlEnable(boolean enable) {
        if (enable) {
            ArrayList<MapType> mapTypes = new ArrayList<>();
            mapTypes.add(MapType.NORMAL);
            mapTypes.add(MapType.SATELLITE);
            mapTypes.add(MapType.TERRAIN);
            callJs(String.format(Locale.US, "TControl.addMapTypeControl(%s);", JsonObject.gson.toJson(mapTypes)));
        } else {
            callJs("TControl.removeMapTypeControl();");
        }
    }

    private void getLocationAddress(LngLat lngLat) {
        callJs("TGeocoder.getLocation(" + lngLat.toJson() + ");");
    }

    private void callJs(String js) {
        callJs(js, false);
    }

    private void callJs(String js, boolean force) {
        post(() -> {
            try {
                if (mApiLoaded || force) {
                    mEngine.evaluateJavascript(js, null);
                } else {
                    mWaitList.add(() -> mEngine.evaluateJavascript(js, null));
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        });
    }

    public void setStyle(int style) {
        switch (style) {
            case STYLE_BLACK:
                callJs("TMap.setStyle(\"black\")");
                break;
            case STYLE_INDIGO:
                callJs("TMap.setStyle(\"indigo\")");
                break;
            default:
                Log.e(TAG, "Unknown style: " + style);
        }
    }

    public void addLocationAddressUpdatedListener(LocationAddressListener listener) {
        if (mListeners.contains(listener)) return;
        mListeners.add(listener);
    }

    public void removeLocationAddressUpdatedListener(LocationAddressListener listener) {
        mListeners.remove(listener);
    }

    @JavascriptInterface
    public void onTMapJavaScriptLoaded(final String version) {
        Log.d(TAG, "onTMapJavaScriptLoaded: " + version);
        mApiLoaded = true;
        setScaleControlEnable(mShowScaleControl);
        setZoomControlEnable(mShowZoomControl);
        setMapTypeControlEnable(mShowMapTypeControl);
        while (!mWaitList.isEmpty()) {
            mWaitList.remove().run();
        }
    }

    @JavascriptInterface
    public void onLocationAddress(int code, String lnglatJson, String result) {
        if (code == NO_ERROR) {
            LngLat lnglat = JsonObject.fromJson(lnglatJson, LngLat.class);
            mLastLocationAddress = JsonObject.fromJson(result, LocationAddress.class);
            mLocHistory.put(lnglat, mLastLocationAddress);
            mListeners.forEach(listener -> listener.onLocationAddressUpdated(lnglat, mLastLocationAddress));
        } else {
            Log.e(TAG, String.format(Locale.US,"{lnglat=%s, code=%d, msg=%s}", lnglatJson, code, result));
        }
    }

    private String getControlPosition(int gravity) {
        switch (gravity) {
            case LEFT_OF_TOP:
                return Control.T_ANCHOR_TOP_LEFT;
            case RIGHT_OF_TOP:
                return Control.T_ANCHOR_TOP_RIGHT;
            case LEFT_OF_BOTTOM:
                return Control.T_ANCHOR_BOTTOM_LEFT;
            case RIGHT_OF_BOTTOM:
                return Control.T_ANCHOR_BOTTOM_RIGHT;
            default:
                throw new RuntimeException("Unsupported gravity: " + gravity);
        }
    }

    public interface LocationAddressListener {
        void onLocationAddressUpdated(LngLat lngLat, LocationAddress address);
    }

    private LocationAddress mLastLocationAddress = null;
    private final Map<LngLat, LocationAddress> mLocHistory = new HashMap<>();

    private static final String IDENT_MY_LOCATION = "my_location";
}
