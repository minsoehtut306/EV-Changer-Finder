package com.example.assignmentthree;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
/**
 * initial user interface and navigating to the MapsActivity.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Called when the activity is first created.
     * This method sets up the layout, enables edge-to-edge UI, and adjusts insets for the view.
     *
     * @param savedInstanceState The saved instance state, used to restore the activity state if it was previously stopped.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Handles the "Start" button click event, navigating from MainActivity to MapsActivity.
     *
     * @param view The view that was clicked.
     */
    public void onclickStart(View view) {
        // Create an explicit Intent to navigate from MainActivity to GameActivity
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        // Start the GameActivity
        startActivity(intent);
    }
}