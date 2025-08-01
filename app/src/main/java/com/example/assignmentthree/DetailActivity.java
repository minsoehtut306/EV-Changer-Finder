package com.example.assignmentthree;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {

    // Declare UI components
    private TextView chargerTitle, chargerDescription, chargerLocation, chargerAddress, chargerPoints;
    private ImageView chargerImage;
    private RequestQueue requestQueue;

    /**
     * Initialises the activity, sets up the layout, and get charger details based on latitude and longitude.
     *
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Initialise UI components
        chargerTitle = findViewById(R.id.chargerTitle);
        chargerDescription = findViewById(R.id.chargerDescription);
        chargerLocation = findViewById(R.id.chargerLocation);
        chargerAddress = findViewById(R.id.chargerAddress);
        chargerPoints = findViewById(R.id.chargerPoints);
        chargerImage = findViewById(R.id.chargerImage);

        Intent intent = getIntent();


        // Initialise the request queue for API requests
        requestQueue = Volley.newRequestQueue(this);

        if (intent.hasExtra("latitude") && intent.hasExtra("longitude")) {
            // Get the latitude and longitude from the Intent
            double latitude = intent.getDoubleExtra("latitude", 0);
            double longitude = intent.getDoubleExtra("longitude", 0);

            // Get the charger details using the latitude and longitude
            getChargerDetails(latitude, longitude);

            // Get StreetView image using Google StreetView API
            getStreetViewImage(latitude, longitude);
        }
    }

    /**
     * Gets the details of the charger using the latitude and longitude from the Open Charge Map API.
     *
     * @param latitude  Latitude of the selected charger location.
     * @param longitude Longitude of the selected charger location.
     */
    private void getChargerDetails(double latitude, double longitude) {
        String apiKey = "788067d4-828a-4b8c-95ee-08f40139f180";  // Please replace this with your own Open Charge Map API
        String url = "https://api.openchargemap.io/v3/poi/?output=json&latitude=" +
                latitude + "&longitude=" + longitude + "&maxresults=1&key=" + apiKey;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Show a toast with the number of chargers found
                        int chargerCount = response.length();

                        // Log the entire API response for debugging
                        Log.d("API Output", response.toString());

                        if (chargerCount > 0) {
                            // Get the first element of the array (the closest EV charger)
                            JSONObject chargerInfo = response.getJSONObject(0);
                            JSONObject addressInfo = chargerInfo.getJSONObject("AddressInfo");

                            // Get the name of the EV Charger Site
                            String title = addressInfo.getString("Title");

                            // Get description from "GeneralComments" or "Description", fallback to "Description not available"
                            String description = null;

                            if (!chargerInfo.isNull("GeneralComments")) {
                                description = chargerInfo.getString("GeneralComments");
                            } else if (!chargerInfo.isNull("Description")) {
                                description = chargerInfo.getString("Description");
                            }

                            if (description == null || description.isEmpty()) {
                                description = "Description not available";
                            }

                            String address = addressInfo.getString("AddressLine1") + ", " + addressInfo.getString("Town");
                            int points = chargerInfo.optInt("NumberOfPoints", 1);
                            String costString = chargerInfo.optString("UsageCost", "N/A");
                            double cost = parseCost(costString);
                            LatLng location = new LatLng(addressInfo.getDouble("Latitude"), addressInfo.getDouble("Longitude"));

                            // Create an EVCharger object
                            EVCharger evCharger = new EVCharger(title, description, address, points, cost, location);

                            // Update UI with EVCharger object
                            displayChargerDetails(evCharger);
                        } else {
                            // Handle the case where no charger data is returned
                            Log.e("EV_API", "No charger found for the given location.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("EV_API", "Error parsing response: " + e.getMessage());
                    }
                },
                error -> {
                    Log.e("EV_API", "Error fetching data: " + error.toString());
                }
        );

        // Add the request to the queue
        requestQueue.add(jsonArrayRequest);
    }

    /**
     * This is just a helper method to parse the cost value from the API response.
     *
     * @param costString The cost value as a string.
     * @return Parsed cost value as a double.
     */
    private double parseCost(String costString) {
        try {
            return Double.parseDouble(costString.replaceAll("[^\\d.]", "0"));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**
     * Displays the details of the EV charger on the screen by updating the UI components.
     *
     * @param evCharger The EVCharger object containing the details of the charger.
     */
    public void displayChargerDetails(EVCharger evCharger) {
        chargerTitle.setText(evCharger.getTitle());
        chargerDescription.setText(evCharger.getDescription());

        // Display location
        String location = "Location: " + evCharger.getLocation().latitude + ", " + evCharger.getLocation().longitude;
        chargerLocation.setText(location);

        // Display address
        String address = "Address: " + evCharger.getAddress();
        chargerAddress.setText(address);

        // Display points and cost
        String pointsAndCost = "Points: " + evCharger.getPoints() + " and Cost: $" + evCharger.getCost() + "/kWh";
        chargerPoints.setText(pointsAndCost);
    }

    /**
     * Get the Google StreetView image for the given location and displays it in the chargerImage ImageView.
     *
     * @param latitude  Latitude of the charger.
     * @param longitude Longitude of the charger.
     */
    private void getStreetViewImage(double latitude, double longitude) {
        String streetViewApiKey = "AIzaSyDm_dnWj5gYBV7eEAib1aC9_two6Bo2hNo"; // Please replace this with your own API
        String imageUrl = "https://maps.googleapis.com/maps/api/streetview?size=600x400&location=" +
                latitude + "," + longitude + "&key=" + streetViewApiKey;

        // This loads the StreetView image into the ImageView
        Picasso.get().load(imageUrl).into(chargerImage);
    }
}
