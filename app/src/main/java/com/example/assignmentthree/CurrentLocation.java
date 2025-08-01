package com.example.assignmentthree;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

/**
 * This class is responsible for retrieving the current location of the device using the Fused Location Provider.
 */
public class CurrentLocation {
    private static final int FINE_PERMISSION_CODE = 1;
    private Location lastLocation;
    private final FusedLocationProviderClient fusedLocationProviderClient;
    private final Context context;

    /**
     * Constructor to initialize the CurrentLocation class.
     *
     * @param context The context of the activity or application, used to access system services.
     */
    public CurrentLocation(Context context) {
        this.context = context;
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    /**
     * Retrieves the last known location of the device.
     *
     * This method checks for location permissions and requests them if not granted.
     * If permissions are granted, it fetches the last known location and triggers the success listener.
     *
     * @param listener The listener that will be triggered when the location is successfully retrieved.
     */
    public void getLastLocation(OnSuccessListener<Location> listener) {
        // Check for location permissions before retrieving the location.
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Request location permissions if not granted.
            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }

        // Get the last known location from the FusedLocationProviderClient.
        Task<Location> task = fusedLocationProviderClient.getLastLocation();

        // If the task is successful and a location is found, trigger the success listener.
        task.addOnSuccessListener((Activity) context, location -> {
            if (location == null) {
                return;
            }

            lastLocation = location;

            if (listener != null) {
                listener.onSuccess(lastLocation);
            }
        });
    }

    /**
     * Returns the most recently retrieved location of the device.
     *
     * @return The last known location, or null if no location is available yet.
     */
    public Location getCurrentLocation() {
        return lastLocation;
    }
}
