package pro.rudloff.openvegemap;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.maps.GoogleMap.*;
import com.google.maps.android.data.geojson.*;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.net.Uri;
import android.content.Intent;

import java.io.IOException;
import org.json.JSONException;

public class MainActivity extends WearableActivity implements OnInfoWindowClickListener, OnMapReadyCallback {

    private GoogleMap mMap;

    private MapFragment mMapFragment;

    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        setContentView(R.layout.activity_main);

        setAmbientEnabled();

        mMapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        mMapFragment.onEnterAmbient(ambientDetails);
    }

    @Override
    public void onExitAmbient() {
        super.onExitAmbient();
        mMapFragment.onExitAmbient();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException error) {

        }
        mMap.setOnInfoWindowClickListener(this);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(48.583692, 7.748794), 16));

        try {
            GeoJsonLayer layer = new GeoJsonLayer(mMap, R.raw.poi, getApplicationContext());
            for (GeoJsonFeature feature : layer.getFeatures()) {
                GeoJsonPointStyle pointStyle = new GeoJsonPointStyle();
                String content = "";
                boolean hasVegan = feature.hasProperty("diet:vegan");
                boolean hasVegetarian = feature.hasProperty("diet:vegetarian");

                if (hasVegan || hasVegetarian) {
                    if (feature.hasProperty("name")) {
                        pointStyle.setTitle(feature.getProperty("name"));
                    }

                    if (hasVegan) {
                        content += "Vegan: " + feature.getProperty("diet:vegan");
                    }
                    if (hasVegetarian) {
                        if (hasVegan) {
                            content += "; ";
                        }
                        content += "Vegetarian: " + feature.getProperty("diet:vegetarian");
                    }

                    pointStyle.setSnippet(content);
                } else {
                    pointStyle.setVisible(false);
                }

                feature.setPointStyle(pointStyle);
            }
            layer.addLayerToMap();
        } catch (IOException error) {

        } catch (JSONException error) {

        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        LatLng position = marker.getPosition();
        Intent mapIntent = new Intent(
            Intent.ACTION_VIEW,
            Uri.parse("google.navigation:q" + position.latitude + "," + position.longitude + "?mode=w")
        );
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

}
