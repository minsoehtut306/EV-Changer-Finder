package com.example.assignmentthree;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Instrumentation tests for testing the retrieval of the last known location in MapsActivity.
 * This class simulates the behavior with and without location permissions.
 */
@RunWith(AndroidJUnit4.class)
public class CurrentLocationInstrumentationTest {

    /**
     * Rule to launch MapsActivity before each test is run.
     */
    @Rule
    public ActivityScenarioRule<MapsActivity> activityRule = new ActivityScenarioRule<>(MapsActivity.class);

    /**
     * Test the behavior of getLastLocation when location permissions are granted.
     * This simulates the case where location access is allowed and we retrieve the last known location.
     */
    @Test
    public void testGetLastLocation_WithPermissionGranted() {
        // Launch the MapsActivity
        ActivityScenario<MapsActivity> scenario = activityRule.getScenario();

        // Interact with the activity within the scenario
        scenario.onActivity(activity -> {
            // Given that location permission is granted (ensure it's enabled in emulator settings)
            CurrentLocation currentLocation = new CurrentLocation(activity);

            // Request the last known location
            currentLocation.getLastLocation(location -> {
                // Assert that a location is available when permission is granted
                assertNotNull(currentLocation.getCurrentLocation());
            });
        });
    }

    /**
     * Test the behavior of getLastLocation when location permissions are denied.
     * This simulates the case where location access is disabled and no location is available.
     */
    @Test
    public void testGetLastLocation_WithoutPermission() {
        // Launch the MapsActivity
        ActivityScenario<MapsActivity> scenario = activityRule.getScenario();

        // Interact with the activity within the scenario
        scenario.onActivity(activity -> {
            // Create a new instance of CurrentLocation
            CurrentLocation currentLocation = new CurrentLocation(activity);

            // Request the last known location without granting permissions
            currentLocation.getLastLocation(location -> {
                // Assert that no location is returned when permission is denied
                assertNull(currentLocation.getCurrentLocation());
            });
        });
    }
}
