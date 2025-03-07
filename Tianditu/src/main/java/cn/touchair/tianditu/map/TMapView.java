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
import cn.touchair.tianditu.control.TControl;
import cn.touchair.tianditu.control.TCopyright;
import cn.touchair.tianditu.control.TMapType;
import cn.touchair.tianditu.control.TOverviewMap;
import cn.touchair.tianditu.control.TScaleControl;
import cn.touchair.tianditu.control.TZoomControl;
import cn.touchair.tianditu.entity.TLngLat;
import cn.touchair.tianditu.entity.TLocationAddress;
import cn.touchair.tianditu.entity.TPoint;
import cn.touchair.tianditu.overlay.TInfoWindow;
import cn.touchair.tianditu.overlay.TIcon;
import cn.touchair.tianditu.overlay.TLabel;
import cn.touchair.tianditu.overlay.TMarker;
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
    private boolean mShowOverviewMapControl = false;
    private int mOverviewMapControlGravity = RIGHT_OF_TOP;
    private boolean mOverviewMapControlDefaultOpen = false;
    private float mOverviewMapControlWidth = 120;
    private float mOverviewMapControlHeight = 120;
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
            mShowOverviewMapControl = attributes.getBoolean(R.styleable.TMapView_showOverviewMapControl, mShowOverviewMapControl);
            mOverviewMapControlGravity = attributes.getInt(R.styleable.TMapView_overviewMapControlGravity, mOverviewMapControlGravity);
            mOverviewMapControlDefaultOpen = attributes.getBoolean(R.styleable.TMapView_overviewMapControlDefaultOpen, mOverviewMapControlDefaultOpen);
            mOverviewMapControlWidth = attributes.getDimension(R.styleable.TMapView_overviewMapControlWidth, mOverviewMapControlWidth);
            mOverviewMapControlHeight = attributes.getDimension(R.styleable.TMapView_overviewMapControlHeight, mOverviewMapControlHeight);
        }
    }

    public void panTo(TLngLat lngLat) {
        callJs("TMap.panTo("+ lngLat.toJson() +");");
    }

    public void addMarker(String ident, TLngLat lngLat) {
        addMarker(ident, new TMarker(lngLat));
    }

    public void setMyLocation(TLngLat lngLat) {
        setMyLocation(lngLat, true);
    }

    public void setMyLocation(TLngLat lngLat, boolean autoGetLocationAddress) {
        panTo(lngLat);
        removeOverlay(IDENT_MY_LOCATION);
        addMarker(IDENT_MY_LOCATION, lngLat, TIcon.CENTER_POINT);
        if (autoGetLocationAddress) {
            getLocationAddress(lngLat);
        }
    }

    public void addMarker(@NonNull String ident, TLngLat lngLat, TIcon icon) {
        addMarker(ident, new TMarker(lngLat, icon));
    }

    public void addMarker(@NonNull String ident, TMarker marker) {
        callJs(String.format(Locale.US, "TOverLay.addMarker(\"%s\", %s)", ident, marker.toJson()));
    }

    public void addLabel(@NonNull String ident, TLabel label) {
        callJs(String.format(Locale.US, "TOverLay.addLabel(\"%s\", %s)", ident, label.toJson()));
    }

    public void removeOverlay(@NonNull String ident) {
        callJs("TOverLay.removeOverLay(\"" + ident + "\");");
    }

    public void addCopyright(TCopyright copyright) {
        callJs(String.format(Locale.US, "TCopyright.addCopyright(%s);", copyright.toJson()));
    }

    public void removeCopyright(@NonNull String ident) {
        callJs(String.format(Locale.US, "TCopyright.removeCopyright(%s);", ident));
    }

    public void addInfoWindow(@NonNull String ident, TInfoWindow window) {
        callJs(String.format(Locale.US, "TOverLay.addInfoWindow(\"%s\", %s);", ident, window.toJson()));
    }

//    public void closeInfoWindow(@NonNull String ident) {
//        callJs(String.format(Locale.US, "TOverLay.closeInfoWindow(\"%s\");", ident));
//    }

    private void setOverviewMapControlEnable(boolean enable) {
        if (enable) {
            TOverviewMap overviewMap = new TOverviewMap();
            overviewMap.position = getControlPosition(mOverviewMapControlGravity);
            overviewMap.isOpen = mOverviewMapControlDefaultOpen;
            overviewMap.size = new TPoint((int) mOverviewMapControlWidth, (int) mOverviewMapControlHeight);
            callJs(String.format(Locale.US, "TControl.addOverviewMapControl(%s);", overviewMap.toJson()));
        } else {
            callJs("TControl.removeOverviewMapControl();");
        }
    }

    private void setZoomControlEnable(boolean enable) {
        if (enable) {
            TZoomControl zoomControl = new TZoomControl();
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
            TScaleControl scaleControl = new TScaleControl();
            scaleControl.position = getControlPosition(mScaleControlGravity);
            callJs(String.format(Locale.US, "TControl.addScaleControl(%s);", scaleControl.toJson()));
        } else {
            callJs("TControl.removeScaleControl();");
        }
    }

    private void setMapTypeControlEnable(boolean enable) {
        if (enable) {
            ArrayList<TMapType> mapTypes = new ArrayList<>();
            mapTypes.add(TMapType.NORMAL);
            mapTypes.add(TMapType.SATELLITE);
            mapTypes.add(TMapType.TERRAIN);
            callJs(String.format(Locale.US, "TControl.addMapTypeControl(%s);", JsonObject.gson.toJson(mapTypes)));
        } else {
            callJs("TControl.removeMapTypeControl();");
        }
    }

    public void getLocationAddress(TLngLat lngLat) {
        TLocationAddress address = mLocHistory.get(lngLat);
        if (address != null) {
            notifyLocationAddress(lngLat, address);
        } else {
            callJs("TGeocoder.getLocation(" + lngLat.toJson() + ");");
        }
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
                    mWaitList.add(() -> callJs(js));
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
        setOverviewMapControlEnable(mShowOverviewMapControl);
        while (!mWaitList.isEmpty()) {
            mWaitList.remove().run();
        }
    }

    @JavascriptInterface
    public void onLocationAddress(int code, String lnglatJson, String result) {
        if (code == NO_ERROR) {
            TLngLat loc = JsonObject.fromJson(lnglatJson, TLngLat.class);
            TLocationAddress address = JsonObject.fromJson(result, TLocationAddress.class);
            mLocHistory.put(loc, address);
            notifyLocationAddress(loc, address);
        } else {
            Log.e(TAG, String.format(Locale.US,"{lnglat=%s, code=%d, msg=%s}", lnglatJson, code, result));
        }
    }

    private void notifyLocationAddress(TLngLat loc, TLocationAddress address) {
        mListeners.forEach(listener -> listener.onLocationAddressUpdated(loc, address));
    }

    private String getControlPosition(int gravity) {
        switch (gravity) {
            case LEFT_OF_TOP:
                return TControl.T_ANCHOR_TOP_LEFT;
            case RIGHT_OF_TOP:
                return TControl.T_ANCHOR_TOP_RIGHT;
            case LEFT_OF_BOTTOM:
                return TControl.T_ANCHOR_BOTTOM_LEFT;
            case RIGHT_OF_BOTTOM:
                return TControl.T_ANCHOR_BOTTOM_RIGHT;
            default:
                throw new RuntimeException("Unsupported gravity: " + gravity);
        }
    }

    public interface LocationAddressListener {
        void onLocationAddressUpdated(TLngLat lngLat, TLocationAddress address);
    }

    private final Map<TLngLat, TLocationAddress> mLocHistory = new HashMap<>();

    private static final String IDENT_MY_LOCATION = "my_location";
}
