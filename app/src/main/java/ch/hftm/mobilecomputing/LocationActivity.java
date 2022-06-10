package ch.hftm.mobilecomputing;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import ch.hftm.mobilecomputing.map.MapManager;
import io.openmobilemaps.mapscore.map.view.MapView;

public class LocationActivity extends AppCompatActivity implements LocationListener {

    private MapManager mapManager;

    private LocationManager locationManager;

    private final ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                        Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                        Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);

                        if (fineLocationGranted != null && fineLocationGranted) {
                            runOnUiThread(() -> Toast.makeText(this, getString(R.string.fine_location), Toast.LENGTH_LONG));
                        } else if (coarseLocationGranted != null && coarseLocationGranted) {
                            runOnUiThread(() -> Toast.makeText(this, getString(R.string.only_coarse), Toast.LENGTH_LONG));
                        } else {
                            runOnUiThread(() -> Toast.makeText(this, getString(R.string.no_location), Toast.LENGTH_LONG));
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        MapView mapView = findViewById(R.id.mapView);
        this.mapManager = new MapManager(this, mapView, this.getCacheDir());
        this.mapManager.setupMap(this.getLifecycle());
    }

    public void zoomIn(View view) {
        this.mapManager.zoomIn();
    }

    public void zoomOut(View view) {
        this.mapManager.zoomOut();
    }

    public void onRequestLocation(View view) {
        this.registerLocationListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        });

        this.registerLocationListener();
    }

    private void registerLocationListener() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return;
        }
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.mapManager.setUserPosition(location.getLatitude(), location.getLongitude(), true);
    }
}