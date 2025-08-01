package com.example.assignmentthree;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static org.hamcrest.Matchers.not;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * This class contains instrumentation tests for the DetailActivity.
 * It tests the proper functionality of passing data via intents and displaying EV charger details.
 */
@RunWith(AndroidJUnit4.class)
public class DetailActivityInstrumentationTest {

    /**
     * Rule to launch the DetailActivity before running each test.
     */
    @Rule
    public ActivityScenarioRule<DetailActivity> activityRule = new ActivityScenarioRule<>(DetailActivity.class);

    /**
     * This test verifies that data passed through an Intent is properly handled and displayed in DetailActivity.
     *
     * @throws InterruptedException if the waiting time for the API exceeds the time limit.
     */
    @Test
    public void testPutDataIntoActivityAndDisplay() throws InterruptedException {
        // CountDownLatch to simulate waiting for the API response
        final CountDownLatch latch = new CountDownLatch(1);

        // Create an intent with extra data (latitude and longitude)
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("latitude", 37.7749);  // Example latitude for San Francisco
        intent.putExtra("longitude", -122.4194); // Example longitude for San Francisco

        // Launch the DetailActivity with the provided intent
        ActivityScenario.launch(intent);

        // Wait for the API response or a timeout of 6 seconds
        latch.await(6, TimeUnit.SECONDS);

        // Validate that the TextViews display non-empty values for charger details
        onView(withId(R.id.chargerTitle)).check(matches(withText(not(""))));
        onView(withId(R.id.chargerDescription)).check(matches(withText(not(""))));
        onView(withId(R.id.chargerLocation)).check(matches(withText(not(""))));
        onView(withId(R.id.chargerAddress)).check(matches(withText(not(""))));
        onView(withId(R.id.chargerPoints)).check(matches(withText(not(""))));
    }

    /**
     * This test verifies that the EVCharger object details are properly displayed in DetailActivity.
     *
     * @throws InterruptedException if the waiting time for the UI update exceeds the time limit.
     */
    @Test
    public void testDisplayChargerDetails() throws InterruptedException {
        // CountDownLatch to simulate waiting for UI update
        final CountDownLatch latch = new CountDownLatch(1);

        // Example data for an EVCharger
        String EVTitle = "ev-title";
        String EVDesc = "ev-desc";
        String EVAddress = "ev-address";
        int EVPoints = 123;
        double EVCost = 123.0;
        LatLng EVLocation = new LatLng(39.9, 39.9);
        EVCharger evCharger = new EVCharger(EVTitle, EVDesc, EVAddress, EVPoints, EVCost, EVLocation);

        // Access the activity and display the EV charger details
        activityRule.getScenario().onActivity(activity -> {
            activity.displayChargerDetails(evCharger);
        });

        // Wait for the UI to update or a timeout of 1 second
        latch.await(1, TimeUnit.SECONDS);

        // Validate that the TextViews display the correct details of the EVCharger
        onView(withId(R.id.chargerTitle)).check(matches(withText(evCharger.getTitle())));
        onView(withId(R.id.chargerDescription)).check(matches(withText(evCharger.getDescription())));
        onView(withId(R.id.chargerLocation)).check(matches(withText("Location: " + evCharger.getLocation().latitude + ", " + evCharger.getLocation().longitude)));
        onView(withId(R.id.chargerAddress)).check(matches(withText("Address: " + evCharger.getAddress())));
        onView(withId(R.id.chargerPoints)).check(matches(withText("Points: " + evCharger.getPoints() + " and Cost: $" + evCharger.getCost() + "/kWh")));
    }
}
