package com.eugene.example.location.uii.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.eugene.example.location.data.gps.GPSManager;
import com.eugene.example.location.R;

/**
 *
 * Created 10/28/2017 by @author eugene
 */
public class MainActivity extends AppCompatActivity {
    private GPSManager gpsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gpsManager = new GPSManager(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gpsManager.stopUsingGPS();
    }

    public void onGetCurrentLocation(View v) {
        if (gpsManager.isGPSEnabled()) {
            gpsManager.defineLocation();
            final double latitude = gpsManager.getLatitude();
            final double longitude = gpsManager.getLongitude();
            Toast.makeText(this,
                    "Your current location" +
                            "\nLat: " + latitude +
                            "\nLong: " + longitude,
                    Toast.LENGTH_LONG
            ).show();
        } else {
            gpsManager.showSettingsDialog();
        }
    }
}
