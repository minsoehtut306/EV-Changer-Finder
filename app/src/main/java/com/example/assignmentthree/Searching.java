package com.example.assignmentthree;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Observer;

/**
 * Handles the search functionality for a location using the Google Places API.
 * It allows users to select a location via an autocomplete feature in a SearchView
 * and fetch nearby electric vehicle (EV) chargers.
 */
public class Searching {
    private final RequestQueue requestQueue;
    private final Context context;
    private final ActivityResultLauncher<Intent> startAutocomplete;
    private final ArrayList<SearchObserver> searchObservers = new ArrayList<>();
    private final ArrayList<LoadingObserver> loadingObservers = new ArrayList<>();

    /**
     * Callback interface to handle the result when a user selects a place from the autocomplete.
     */
    public interface SearchObserver {
        /**
         * Triggered when the user selects a place.
         *
         * @param place The place selected by the user.
         * @param chargerLocations A list of nearby EV charger locations.
         */
        void update(Place place, ArrayList<LatLng> chargerLocations);
    }

    /**
     * Callback interface for displaying a loading state during search operations.
     */
    public interface LoadingObserver {
        /**
         * Triggered when a loading state needs to be displayed.
         */
        void update();
    }

    private interface EVSitesObserver {
        void update(ArrayList<LatLng> chargerLocations);
    }

    /**
     * Constructs a new instance of the {@code Searching} class.
     *
     * @param context The current {@link FragmentActivity} context.
     */
    public Searching(FragmentActivity context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);

        // Initialize the ActivityResultLauncher to handle the autocomplete intent result.
        startAutocomplete = context.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();

                        if (intent != null) {
                            // Extract the selected Place from the intent.
                            Place place = Autocomplete.getPlaceFromIntent(intent);

                            // Notify loading observers.
                            for (LoadingObserver observer : loadingObservers) {
                                observer.update();
                            }

                            // Fetch nearby EV chargers for the selected place.
                            getNearbyEVChargers(Objects.requireNonNull(place.getLocation()), chargerLocations -> {
                                for (SearchObserver observer : searchObservers) {
                                    observer.update(place, chargerLocations);
                                }
                            });
                        }
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        // Log an error if the autocomplete operation is canceled.
                        Log.e("Autocomplete", "Error: " + Autocomplete.getStatusFromIntent(result.getData()).getStatusMessage());
                    }
                });
    }

    /**
     * Fetches nearby EV chargers based on the provided location (latitude and longitude).
     *
     * @param latLng The latitude and longitude of the searched location.
     * @param observer The observer to notify with the list of nearby charger locations.
     */
    private void getNearbyEVChargers(LatLng latLng, EVSitesObserver observer) {
        String apiKey = "788067d4-828a-4b8c-95ee-08f40139f180";
        String url = "https://api.openchargemap.io/v3/poi/?output=json&latitude=" +
                latLng.latitude + "&longitude=" + latLng.longitude + "&maxresults=10&key=" + apiKey;

        // Make a network request to fetch nearby EV chargers.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        ArrayList<LatLng> chargerLocations = new ArrayList<>();
                        Log.d("EV_API", "Chargers found: " + response.length());

                        // Show a toast with the number of chargers found.
                        Toast.makeText(context, response.length() + " EV chargers found", Toast.LENGTH_SHORT).show();

                        // Parse response and add charger locations.
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject charger = response.getJSONObject(i);
                            JSONObject addressInfo = charger.getJSONObject("AddressInfo");
                            double lat = addressInfo.getDouble("Latitude");
                            double lon = addressInfo.getDouble("Longitude");
                            chargerLocations.add(new LatLng(lat, lon));
                        }

                        observer.update(chargerLocations);
                    } catch (Exception e) {
                        Log.e("EV_API", "Error parsing response: " + e.getMessage());
                    }
                },
                error -> Log.e("EV_API", "Error fetching data: " + error.toString())
        );

        // Add the request to the request queue.
        requestQueue.add(jsonArrayRequest);
    }

    /**
     * Sets up the SearchView to trigger the autocomplete feature when it gains focus.
     *
     * @param id The ID of the SearchView.
     */
    public void setupSearchViewAutocomplete(int id) {
        SearchView searchView = ((Activity) context).findViewById(id);

        // Trigger the autocomplete process when the SearchView gains focus.
        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                startAutocompleteIntent();
                searchView.clearFocus(); // Clear focus to avoid reopening.
            }
        });
    }

    /**
     * Launches the autocomplete intent using the Google Places API.
     * This method starts an autocomplete activity that allows users to search for and select a place.
     */
    public void startAutocompleteIntent() {
        // Specify the types of place data to return after the user selects a place.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.LOCATION, Place.Field.ADDRESS_COMPONENTS);

        // Create and launch the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(context);
        startAutocomplete.launch(intent);
    }

    /**
     * Adds a {@link SearchObserver} to be notified when a place is selected from the autocomplete.
     *
     * @param observer The observer to be added.
     */
    public void addSearchObserver(SearchObserver observer) {
        searchObservers.add(observer);
    }

    /**
     * Removes a {@link SearchObserver}.
     *
     * @param observer The observer to be removed.
     */
    public void removeSearchObserver(SearchObserver observer) {
        searchObservers.remove(observer);
    }

    /**
     * Adds a {@link LoadingObserver} to be notified when the loading state is triggered.
     *
     * @param observer The observer to be added.
     */
    public void addLoadingObserver(LoadingObserver observer) {
        loadingObservers.add(observer);
    }

    /**
     * Removes a {@link LoadingObserver}.
     *
     * @param observer The observer to be removed.
     */
    public void removeLoadingObserver(LoadingObserver observer) {
        loadingObservers.remove(observer);
    }
}
