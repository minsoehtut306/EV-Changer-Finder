package com.example.assignmentthree;

import com.google.android.gms.maps.model.LatLng;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the EVCharger class.
 * This test verifies the correct functionality of the EVCharger constructor and its getters.
 */
public class EVChargerUnitTest {

    // An instance of EVCharger that will be used in the tests
    private EVCharger evCharger;

    /**
     * Sets up the test environment by initializing an EVCharger object with sample data.
     * This method is executed before each test to ensure a fresh object.
     */
    @Before
    public void setUp() {
        // Initialize the EVCharger object with a sample location and data
        LatLng location = new LatLng(37.7749, -122.4194); // Example location (San Francisco)
        evCharger = new EVCharger("ChargePoint 1", "Fast charging station", "123 Main St", 4, 0.25, location);
    }

    /**
     * Tests the constructor and getter methods of the EVCharger class.
     * Verifies that all fields are correctly initialized and that the getters return expected values.
     */
    @Test
    public void testConstructorAndGetters() {
        // Verify that the title field is correctly initialized and retrieved
        assertEquals("ChargePoint 1", evCharger.getTitle());

        // Verify that the description field is correctly initialized and retrieved
        assertEquals("Fast charging station", evCharger.getDescription());

        // Verify that the address field is correctly initialized and retrieved
        assertEquals("123 Main St", evCharger.getAddress());

        // Verify that the points field is correctly initialized and retrieved
        assertEquals(4, evCharger.getPoints());

        // Verify that the cost field is correctly initialized and retrieved, allowing a delta for floating point comparison
        assertEquals(0.25, evCharger.getCost(), 0.01);

        // Verify that the location field is correctly initialized and retrieved
        assertEquals(new LatLng(37.7749, -122.4194), evCharger.getLocation());
    }
}
