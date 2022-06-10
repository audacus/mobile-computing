package ch.hftm.mobilecomputing.map;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.openmobilemaps.mapscore.shared.map.coordinates.Coord;
import io.openmobilemaps.mapscore.shared.map.coordinates.CoordinateSystemIdentifiers;
import io.openmobilemaps.mapscore.shared.map.coordinates.RectCoord;
import io.openmobilemaps.mapscore.shared.map.layers.tiled.Tiled2dMapLayerConfig;
import io.openmobilemaps.mapscore.shared.map.layers.tiled.Tiled2dMapZoomInfo;
import io.openmobilemaps.mapscore.shared.map.layers.tiled.Tiled2dMapZoomLevelInfo;

public class MapLayerConfig extends Tiled2dMapLayerConfig {

    private static final String TAG = "MapLayerConfig";

    private final RectCoord epsg3857Bounds = new RectCoord(
            new Coord(CoordinateSystemIdentifiers.EPSG3857(), -20037508.34, 20037508.34, 0.0),
            new Coord(CoordinateSystemIdentifiers.EPSG3857(), 20037508.34, -20037508.34, 0.0)
    );

    @NonNull
    @Override
    public String getCoordinateSystemIdentifier() {
        return CoordinateSystemIdentifiers.EPSG3857();
    }

    @NonNull
    @Override
    public String getTileUrl(int x, int y, int zoom) {
        String url = String.format("https://a.tile.openstreetmap.org/%s/%s/%s.png", zoom, x, y);
        Log.i(TAG, url);
        return url;
    }

    @NonNull
    @Override
    public String getTileIdentifier(int x, int y, int zoom) {
        return "OSM_" + zoom + "_" + x + "_" + y;
    }

    @NonNull
    @Override
    public ArrayList<Tiled2dMapZoomLevelInfo> getZoomLevelInfos() {
        List<Tiled2dMapZoomLevelInfo> zoomLevelInfos = Arrays.asList(
                new Tiled2dMapZoomLevelInfo(500000000.0, 40075016f, 1, 1, 0, epsg3857Bounds),
                new Tiled2dMapZoomLevelInfo(250000000.0, 20037508f, 2, 2, 1, epsg3857Bounds),
                new Tiled2dMapZoomLevelInfo(150000000.0, 10018754f, 4, 4, 2, epsg3857Bounds),
                new Tiled2dMapZoomLevelInfo(70000000.0, 5009377.1f, 8, 8, 3, epsg3857Bounds),
                new Tiled2dMapZoomLevelInfo(35000000.0, 2504688.5f, 16, 16, 4, epsg3857Bounds),
                new Tiled2dMapZoomLevelInfo(15000000.0, 1252344.3f, 32, 32, 5, epsg3857Bounds),
                new Tiled2dMapZoomLevelInfo(10000000.0, 626172.1f, 64, 64, 6, epsg3857Bounds),
                new Tiled2dMapZoomLevelInfo(4000000.0, 313086.1f, 128, 128, 7, epsg3857Bounds),
                new Tiled2dMapZoomLevelInfo(2000000.0, 156543f, 256, 256, 8, epsg3857Bounds),
                new Tiled2dMapZoomLevelInfo(1000000.0, 78271.5f, 512, 512, 9, epsg3857Bounds),
                new Tiled2dMapZoomLevelInfo(500000.0, 39135.8f, 1024, 1024, 10, epsg3857Bounds),
                new Tiled2dMapZoomLevelInfo(250000.0, 19567.9f, 2048, 2048, 11, epsg3857Bounds),
                new Tiled2dMapZoomLevelInfo(150000.0, 9783.94f, 4096, 4096, 12, epsg3857Bounds),
                new Tiled2dMapZoomLevelInfo(70000.0, 4891.97f, 8192, 8192, 13, epsg3857Bounds),
                new Tiled2dMapZoomLevelInfo(35000.0, 2445.98f, 16384, 16384, 14, epsg3857Bounds),
                new Tiled2dMapZoomLevelInfo(15000.0, 1222.99f, 32768, 32768, 15, epsg3857Bounds),
                new Tiled2dMapZoomLevelInfo(8000.0, 611.496f, 65536, 65536, 16, epsg3857Bounds),
                new Tiled2dMapZoomLevelInfo(4000.0, 305.748f, 131072, 131072, 17, epsg3857Bounds),
                new Tiled2dMapZoomLevelInfo(2000.0, 152.874f, 262144, 262144, 18, epsg3857Bounds),
                new Tiled2dMapZoomLevelInfo(1000.0, 76.437f, 524288, 524288, 19, epsg3857Bounds));

        return new ArrayList<>(zoomLevelInfos);
    }

    @NonNull
    @Override
    public Tiled2dMapZoomInfo getZoomInfo() {
        return new Tiled2dMapZoomInfo(1.2f, 2);
    }
}
