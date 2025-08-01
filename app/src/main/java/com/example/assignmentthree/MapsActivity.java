package com.example.assignmentthree;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.example.assignmentthree.databinding.ActivityMapsBinding;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;
import java.util.Observer;

/**
 * MapsActivity handles the display and interaction with Google Maps, including
 * showing the current location, searching for a location, and fetching nearby EV sites.
 * This activity also implements the {@link OnMapReadyCallback} to initialize the map.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    /** Constant for the location permission request code. */
    private final int FINE_PERMISSION_CODE = 1;

    /** A reference to the custom map handler {@link MyMap}. */
    private MyMap myMap;

    /** A reference to the current location handler {@link CurrentLocation}. */
    private CurrentLocation currentLocation;

    /** View binding for the activity's layout. */
    private ActivityMapsBinding binding;

    /**
     * Called when the activity is first created. This method initializes the map,
     * sets up current location fetching, and configures search functionality with autocomplete.
     *
     * @param savedInstanceState The saved instance state, used to restore the activity state if it was previously stopped.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Bind the layout to the activity
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Google Places if it hasn't been initialized yet
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyDm_dnWj5gYBV7eEAib1aC9_two6Bo2hNo");
        }

        // Set up the map fragment and initialize the map asynchronously
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        assert mapFragment != null;
        mapFragment.getMapAsync(MapsActivity.this);

        // Set up the current location handler
        currentLocation = new CurrentLocation(this);
        setupGettingLastLocation();

        // Set up searching functionality and EV site fetching
        Searching searching = new Searching(this);
        Toast loading = Toast.makeText(MapsActivity.this, "loading", Toast.LENGTH_LONG);

        // Configure the search view with autocomplete
        searching.setupSearchViewAutocomplete(R.id.sv_searchPlace);

        // Observer for handling location selection and EV site display
        searching.addSearchObserver((place, evSites) -> {
            loading.cancel();
            myMap.removeAllEVSiteMarkers();

            AddressComponent addressComponent = Objects.requireNonNull(place.getAddressComponents()).asList().get(0);
            LatLng searchedLocation = place.getLocation();
            Marker marker = myMap.addSearchedMarker(addressComponent.getName(), searchedLocation);
            myMap.flyTo(marker.getPosition(), MyMap.CITY_ZOOM);

            SearchView searchPlace = findViewById(R.id.sv_searchPlace);
            searchPlace.setQuery(addressComponent.getName(), false);

            // Add EV site markers
            for (LatLng siteLatLng : evSites) {
                myMap.addEVSiteMarker(siteLatLng);
            }
        });

        // Observer for displaying the loading message
        searching.addLoadingObserver(loading::show);
    }

    /**
     * Called when the Google Map is ready to be used.
     * This method sets up the map and adds a marker click listener
     * to navigate to {@link DetailActivity} when an EV site marker is clicked.
     *
     * @param googleMap The GoogleMap object to interact with.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = new MyMap(googleMap);

        // Set up a marker click listener to navigate to the detail page
        googleMap.setOnMarkerClickListener(marker -> {
            LatLng position = marker.getPosition();
            Intent intent = new Intent(MapsActivity.this, DetailActivity.class);
            intent.putExtra("latitude", position.latitude);
            intent.putExtra("longitude", position.longitude);
            startActivity(intent);
            return false;
        });
    }

    /**
     * Handles the result of the permission request for accessing the device's location.
     * If permission is granted, it sets up the location fetching process.
     *
     * @param requestCode The request code passed during the permission request.
     * @param permissions The requested permissions.
     * @param grantResults The result of the permission requests.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupGettingLastLocation();
            } else {
                Toast.makeText(this, "Location permission is denied, please allow the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Sets up the process for fetching the user's last known location and adds a marker
     * on the map for the current location.
     */
    private void setupGettingLastLocation() {
        currentLocation.getLastLocation(location -> {
            myMap.addCurrentMarker(MyMap.locationToLatLng(location));
        });
    }
}
