package com.example.assignmentthree;

import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * This class is responsible for handling Google Map interactions, including managing markers
 * for current location, searched locations, and EV sites.
 */
public class MyMap {
    public static final float CITY_ZOOM = 12f;

    private GoogleMap gMap;
    private MarkerOptions EVSiteMarkerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.img_marker_ev));
    private ArrayList<Marker> EVSiteMarkers = new ArrayList<>();
    private MarkerOptions currentMarkerOptions = new MarkerOptions().title("My Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.img_marker_location_current));
    private Marker currentMarker;
    private MarkerOptions searchedMarkerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.img_marker_location_searched));
    private Marker lastSearchedMarker;

    /**
     * Constructor to initialize the MyMap class with the provided GoogleMap instance.
     *
     * @param googleMap The GoogleMap object used for map interactions.
     */
    public MyMap(GoogleMap googleMap) {
        gMap = googleMap;
    }

    /**
     * Adds a marker for the current location and removes the previous one if it exists.
     * Animates the camera to the given location with a predefined zoom level.
     *
     * @param markerOptions The options for the marker.
     * @param latLng        The latitude and longitude where the marker will be placed.
     * @return The newly added marker.
     */
    public Marker addCurrentMarker(MarkerOptions markerOptions, LatLng latLng) {
        if (currentMarker != null) {
            currentMarker.remove();
        }

        currentMarker = gMap.addMarker(markerOptions.position(latLng));
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, CITY_ZOOM));

        return currentMarker;
    }

    /**
     * Adds a marker for the current location using default marker options.
     *
     * @param latLng The latitude and longitude where the marker will be placed.
     * @return The newly added marker.
     */
    public Marker addCurrentMarker(LatLng latLng) {
        return addCurrentMarker(currentMarkerOptions, latLng);
    }

    /**
     * Removes the current location marker from the map.
     */
    public void removeCurrentLocation() {
        if (currentMarker != null) {
            currentMarker.remove();
        }
    }

    /**
     * Adds a marker for a searched location and removes the previous searched marker if it exists.
     *
     * @param markerOptions The options for the marker.
     * @param latLng        The latitude and longitude where the marker will be placed.
     * @return The newly added searched location marker.
     */
    public Marker addSearchedMarker(MarkerOptions markerOptions, LatLng latLng) {
        if (lastSearchedMarker != null) {
            lastSearchedMarker.remove();
        }

        lastSearchedMarker = gMap.addMarker(markerOptions.position(latLng));

        return lastSearchedMarker;
    }

    /**
     * Adds a marker for a searched location with a title.
     *
     * @param title  The title of the searched location.
     * @param latLng The latitude and longitude where the marker will be placed.
     * @return The newly added searched location marker.
     */
    public Marker addSearchedMarker(String title, LatLng latLng) {
        return addSearchedMarker(searchedMarkerOptions.title(title), latLng);
    }

    /**
     * Removes the last searched location marker from the map.
     */
    public void removeSearchedLocation() {
        if (lastSearchedMarker != null) {
            lastSearchedMarker.remove();
        }
    }

    /**
     * Adds a marker for an EV site and stores the marker in a list.
     *
     * @param markerOptions The options for the marker.
     * @param latLng        The latitude and longitude where the marker will be placed.
     * @return The newly added EV site marker.
     */
    public Marker addEVSiteMarker(MarkerOptions markerOptions, LatLng latLng) {
        Marker siteMarker = gMap.addMarker(markerOptions.position(latLng));
        EVSiteMarkers.add(siteMarker);

        return siteMarker;
    }

    /**
     * Adds a marker for an EV site using default marker options.
     *
     * @param latLng The latitude and longitude where the marker will be placed.
     * @return The newly added EV site marker.
     */
    public Marker addEVSiteMarker(LatLng latLng) {
        return addEVSiteMarker(EVSiteMarkerOptions, latLng);
    }

    /**
     * Removes all EV site markers from the map.
     */
    public void removeAllEVSiteMarkers() {
        for (Marker marker : EVSiteMarkers) {
            marker.remove();
        }
        EVSiteMarkers.clear();
    }

    /**
     * Updates the marker options for the current location marker.
     *
     * @param markerOptions The new marker options for the current location.
     */
    public void setCurrentLocationMarkerOptions(MarkerOptions markerOptions) {
        currentMarkerOptions = markerOptions;
    }

    /**
     * Updates the marker options for the searched location marker.
     *
     * @param markerOptions The new marker options for the searched location.
     */
    public void setSearchedMarkerOptions(MarkerOptions markerOptions) {
        searchedMarkerOptions = markerOptions;
    }

    /**
     * Updates the marker options for the EV site markers.
     *
     * @param markerOptions The new marker options for the EV site markers.
     */
    public void setEVSiteMarkerOptions(MarkerOptions markerOptions) {
        EVSiteMarkerOptions = markerOptions;
    }

    /**
     * Animates the camera to a specific location with the specified zoom level.
     *
     * @param latLng The latitude and longitude to move the camera to.
     * @param zoom   The zoom level for the camera.
     */
    public void flyTo(LatLng latLng, float zoom) {
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    /**
     * Converts a Location object to a LatLng object.
     *
     * @param location The location to be converted.
     * @return The LatLng object representing the same location.
     */
    public static LatLng locationToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }
}
