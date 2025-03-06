package cn.touchair.tianditu;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Arrays;

import cn.touchair.tianditu.databinding.ActivityMainBinding;
import cn.touchair.tianditu.entity.TLngLat;
import cn.touchair.tianditu.entity.TPoint;
import cn.touchair.tianditu.overlay.TIcon;
import cn.touchair.tianditu.overlay.TMarker;
import cn.touchair.tianditu.util.TMapLocationManager;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && Arrays.stream(grantResults).sum() == PackageManager.PERMISSION_GRANTED) {
            onAllowAccess();
        } else {
            Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            onAllowAccess();
        }
    }

    private void onAllowAccess() {
        TMapLocationManager manager = new TMapLocationManager(this) {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                binding.mapView.setMyLocation(new TLngLat(location));
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.startLocation();
    }
}