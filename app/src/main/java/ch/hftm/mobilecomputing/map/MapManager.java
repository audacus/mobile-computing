package ch.hftm.mobilecomputing.map;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

import androidx.lifecycle.Lifecycle;

import java.io.File;

import ch.hftm.mobilecomputing.R;
import io.openmobilemaps.mapscore.MapsCore;
import io.openmobilemaps.mapscore.graphics.BitmapTextureHolder;
import io.openmobilemaps.mapscore.map.loader.TextureLoader;
import io.openmobilemaps.mapscore.map.view.MapView;
import io.openmobilemaps.mapscore.shared.graphics.common.Vec2F;
import io.openmobilemaps.mapscore.shared.map.MapConfig;
import io.openmobilemaps.mapscore.shared.map.coordinates.Coord;
import io.openmobilemaps.mapscore.shared.map.coordinates.CoordinateSystemFactory;
import io.openmobilemaps.mapscore.shared.map.coordinates.CoordinateSystemIdentifiers;
import io.openmobilemaps.mapscore.shared.map.layers.icon.IconFactory;
import io.openmobilemaps.mapscore.shared.map.layers.icon.IconInfoInterface;
import io.openmobilemaps.mapscore.shared.map.layers.icon.IconLayerInterface;
import io.openmobilemaps.mapscore.shared.map.layers.icon.IconType;
import io.openmobilemaps.mapscore.shared.map.layers.tiled.Tiled2dMapLayerConfig;
import io.openmobilemaps.mapscore.shared.map.layers.tiled.raster.Tiled2dMapRasterLayerInterface;

public class MapManager {
    private static final String TAG = "MapManger";
    private final int zoomLevel = 4;

    private final Context context;
    private final MapView mainMap;
    private final File cacheDir;
    private IconLayerInterface iconLayer;

    public MapManager(Context context, MapView mapView, File cacheDirectory) {
        this.context = context;
        this.mainMap = mapView;
        this.cacheDir = cacheDirectory;
        MapsCore.INSTANCE.initialize();
    }

    public void setupMap(Lifecycle lifecycle) {
        if (this.setupCacheDir()) {
            Tiled2dMapLayerConfig layerConfig = new MapLayerConfig();
            TextureLoader textureLoader = new TextureLoader(this.context, this.cacheDir, 100000000, "android-maps", "android");
            Tiled2dMapRasterLayerInterface tiledLayer = Tiled2dMapRasterLayerInterface.create(layerConfig, textureLoader);

            this.mainMap.setupMap(new MapConfig(CoordinateSystemFactory.getEpsg3857System()));
            this.mainMap.addLayer(tiledLayer.asLayerInterface());
            this.mainMap.registerLifecycle(lifecycle);

            this.mainMap.getCamera().moveToCenterPositionZoom(
                    new Coord(CoordinateSystemIdentifiers.EPSG4326(), 8.378232525377973, 46.962592372639634, 0.0),
                    10000000.0, false);

            this.iconLayer = IconLayerInterface.create();
        }
    }

    public void setUserPosition(double lat, double lon, boolean flyToPosition) {
        this.removeCurrentIcons();
        Coord coord = new Coord(CoordinateSystemIdentifiers.EPSG4326(), lon, lat, 0);
        this.iconLayer.add(this.getIcon(coord));
        this.mainMap.addLayer(this.iconLayer.asLayerInterface());

        if (flyToPosition) {
            this.flyToPosition(lat, lon);
        }
    }

    public void flyToPosition(double lat, double lon) {
        this.mainMap.getCamera().moveToCenterPositionZoom(
                new Coord(CoordinateSystemIdentifiers.EPSG4326(), lon, lat, 0.0),
                25000.0, false);
    }

    public void zoomIn() {
        double inZoomLevel = this.mainMap.getCamera().getZoom() - this.mainMap.getCamera().getZoom() / this.zoomLevel;
        if (inZoomLevel > 0) {
            this.mainMap.getCamera().setZoom(inZoomLevel, false);
        }
    }

    public void zoomOut() {
        double outZoomLevel = this.mainMap.getCamera().getZoom() + this.mainMap.getCamera().getZoom() / this.zoomLevel;
        if (outZoomLevel > 0) {
            this.mainMap.getCamera().setZoom(outZoomLevel, false);
        }
    }

    public void removeCurrentIcons() {
        for (var i : this.iconLayer.getIcons()) {
            this.iconLayer.remove(i);
        }
    }

    private IconInfoInterface getIcon(Coord coord) {
        ImageView image = new ImageView(this.context);
        image.setImageResource(R.drawable.marker);
        BitmapTextureHolder texture = new BitmapTextureHolder(image.getDrawable());

        DisplayMetrics metrics = new DisplayMetrics();
        metrics.density = 0.8f;
        float iconSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100f, metrics);

        return IconFactory.createIcon(
                "userLocation",
                coord,
                texture,
                new Vec2F(iconSize, iconSize),
                IconType.INVARIANT);
    }

    private boolean setupCacheDir() {
        boolean dirCreated = false;
        try {
            File cacheDir = new File(this.cacheDir, "cacheMap");
            dirCreated = cacheDir.mkdir() || cacheDir.exists();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Log.i(TAG, String.format("App has directory:%s, created:%s, and can write:%s ", this.cacheDir.getAbsolutePath(), dirCreated, this.cacheDir.canWrite()));
        return dirCreated;
    }
}