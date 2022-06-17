package ch.hftm.mobilecomputing;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Map;

import ch.hftm.mobilecomputing.map.MapManager;
import io.openmobilemaps.mapscore.map.view.MapView;

public class LocationActivity extends AppCompatActivity implements LocationListener {

    private MapManager mapManager;
    private LocationManager locationManager;

    private Boolean isListening = false;
    private Location lastLocation;

    private final ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), this::onRequestMultiplePermissions);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        MapView mapView = findViewById(R.id.mapView);
        this.mapManager = new MapManager(this, mapView, this.getCacheDir());
        this.mapManager.setupMap(this.getLifecycle());

        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    public void zoomIn(View view) {
        this.mapManager.zoomIn();
    }

    public void zoomOut(View view) {
        this.mapManager.zoomOut();
    }

    public void onRequestLocation(View view) {
        this.launchLocationPermissionRequest();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.locationManager.removeUpdates(this);
        this.isListening = false;
    }

    private void launchLocationPermissionRequest() {
        this.locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        });
    }

    private void onRequestMultiplePermissions(Map<String, Boolean> result) {
        Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
        Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);

        if (Boolean.TRUE.equals(fineLocationGranted)) {
            Toast.makeText(this, getString(R.string.fine_location), Toast.LENGTH_LONG).show();
            this.registerLocationListener();
        } else if (Boolean.TRUE.equals(coarseLocationGranted)) {
            Toast.makeText(this, getString(R.string.only_coarse), Toast.LENGTH_LONG).show();
            this.registerLocationListener();
        } else {
            Toast.makeText(this, getString(R.string.no_location), Toast.LENGTH_LONG).show();
        }
    }

    private void registerLocationListener() {
        this.registerLocationListener(false);
    }

    private void registerLocationListener(boolean forceReRegister) {
        if (this.lastLocation != null) this.mapManager.flyToPosition(this.lastLocation.getLatitude(), this.lastLocation.getLongitude());

        if (this.isListening && !forceReRegister) return;

        if (forceReRegister) this.locationManager.removeUpdates(this);

        // generated permission check
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.launchLocationPermissionRequest();
            return;
        }

        // register all possible location providers
        for (var provider : this.locationManager.getAllProviders()) {
            this.locationManager.requestLocationUpdates(provider, 5000, 5, this);
            Log.i(MainActivity.TAG, String.format("Location updates started. Provider: %s", provider));
        }

        this.isListening = true;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.lastLocation = location;
        this.mapManager.setUserPosition(location.getLatitude(), location.getLongitude(), true);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        this.locationManager.removeUpdates(this);
        this.mapManager.removeCurrentIcons();
        this.registerLocationListener(true);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        // generated permission check
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.launchLocationPermissionRequest();
            return;
        }
        this.registerLocationListener(true);
    }
}