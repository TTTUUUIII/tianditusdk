package cn.touchair.tianditu;

import android.app.Application;

import cn.touchair.tianditu.control.Copyright;
import cn.touchair.tianditu.util.TMapInitializer;
import cn.touchair.tianditu.map.TMapOptions;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TMapOptions options = new TMapOptions();
        options.zoom = 16;
        TMapInitializer.initialize("cd6a40c10b97d054b435a60eb67d23b2", options);
    }
}
