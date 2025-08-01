package com.example.assignmentthree;

import com.google.android.gms.maps.model.LatLng;
import java.io.Serializable;

/**
 * Represents an EV Charger with details such as title, description,
 * address, points available, cost, and location.
 */
public class EVCharger implements Serializable {
    private final String title;
    private final String description;
    private final String address;
    private final int points;
    private final double cost;
    private final LatLng location;

    /**
     * Constructor.
     *
     * @param title       The title or name of the EV charger.
     * @param description The description of the EV charger.
     * @param address     The address of the EV charger.
     * @param points      The number of charging points available.
     * @param cost        The cost of using the charger (per kWh).
     * @param location    The geographical location (latitude and longitude) of the charger.
     */
    public EVCharger(String title, String description, String address, int points, double cost, LatLng location) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.points = points;
        this.cost = cost;
        this.location = location;
    }

    /**
     * Gets the title of the EV charger.
     *
     * @return The title of the EV charger.
     */
    public String getTitle() { return title; }

    /**
     * Gets the description of the EV charger.
     *
     * @return The description of the EV charger.
     */
    public String getDescription() { return description; }

    /**
     * Gets the address of the EV charger.
     *
     * @return The address of the EV charger.
     */
    public String getAddress() { return address; }

    /**
     * Gets the number of available charging points.
     *
     * @return The number of charging points.
     */
    public int getPoints() { return points; }

    /**
     * Gets the cost of using the charger per kWh.
     *
     * @return The cost per kWh.
     */
    public double getCost() { return cost; }

    /**
     * Gets the geographical location of the EV charger.
     *
     * @return The latitude and longitude of the EV charger.
     */
    public LatLng getLocation() { return location; }
}
