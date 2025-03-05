package cn.touchair.tianditu;

import android.app.Application;

import cn.touchair.tianditu.util.TMapInitializer;
import cn.touchair.tianditu.map.TMapOptions;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TMapOptions options = new TMapOptions();
        options.zoom = 16;
        TMapInitializer.initialize("your_api_key", options);
    }
}
