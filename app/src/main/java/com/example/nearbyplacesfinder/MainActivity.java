package com.example.nearbyplacesfinder;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MainActivity extends AppCompatActivity {

    private MapView map;
    private IMapController mapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // OSM setup
        Configuration.getInstance().setUserAgentValue(getPackageName());
        setContentView(R.layout.activity_main);

        map = findViewById(R.id.map);
        map.setMultiTouchControls(true);

        mapController = map.getController();
        mapController.setZoom(14.0);

        // Default center (Tirupati for example)
        GeoPoint startPoint = new GeoPoint(13.6288, 79.4192);
        mapController.setCenter(startPoint);

        // Add marker for "You are here"
        Marker myLocationMarker = new Marker(map);
        myLocationMarker.setPosition(startPoint);
        myLocationMarker.setTitle("You are here");
        map.getOverlays().add(myLocationMarker);

        // Button: Fetch restaurants nearby
        Button btnRecenter = findViewById(R.id.btnRecenter);
        btnRecenter.setOnClickListener(v -> {
            Toast.makeText(this, "Searching for restaurants...", Toast.LENGTH_SHORT).show();
            fetchNearbyPlaces("restaurant", startPoint);
        });
    }

    private void fetchNearbyPlaces(String type, GeoPoint location) {
        // Build query: e.g., "restaurant near Tirupati"
        String query = type + "+near+" + location.getLatitude() + "," + location.getLongitude();
        String url = "https://nominatim.openstreetmap.org/search.php?q=" + query + "&format=json";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Remove old markers (except "You are here")
                        map.getOverlays().clear();

                        // Re-add "You are here" marker
                        Marker myMarker = new Marker(map);
                        myMarker.setPosition(location);
                        myMarker.setTitle("You are here");
                        map.getOverlays().add(myMarker);

                        // Add new places
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject place = response.getJSONObject(i);
                            double lat = place.getDouble("lat");
                            double lon = place.getDouble("lon");
                            String name = place.optString("display_name", "Unknown");

                            Marker marker = new Marker(map);
                            marker.setPosition(new GeoPoint(lat, lon));
                            marker.setTitle(name);
                            map.getOverlays().add(marker);
                        }

                        map.invalidate(); // Refresh map
                        Toast.makeText(this, "Found " + response.length() + " places", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing results", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Failed to load places", Toast.LENGTH_SHORT).show());

        queue.add(request);
    }
}
