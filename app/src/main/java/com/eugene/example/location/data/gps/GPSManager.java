package com.eugene.example.location.data.gps;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.eugene.example.location.R;

/**
 * Class for defining current user's location with help of LocationManager.
 *
 * Created 10/28/2017 by @author eugene
 */
public class GPSManager extends Service {
    private static final String LOCATION_PERMISSION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String[] PERMISSIONS = new String[]{LOCATION_PERMISSION};
    private static final int LOCATION_ERROR_CODE = -1;
    // The minimum distance to change updates in meters
    private static final long MIN_UPDATE_DISTANCE = 10;     // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_UPDATE_TIME = 1000 * 60 * 2;      // 2 minute

    private final Context context;
    private final LocationListener locationListener;
    private Location location;
    private LocationManager locationManager;

    public GPSManager(@NonNull final Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new GPSLocationListener();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public boolean isGPSEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public Location defineLocation() {
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_UPDATE_TIME,
                MIN_UPDATE_DISTANCE,
                locationListener);
        if (ContextCompat.checkSelfPermission(context, LOCATION_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, 0);
        }
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        return location;
    }

    public double getLatitude() {
        return location != null ? location.getLatitude() : LOCATION_ERROR_CODE;
    }

    public double getLongitude() {
        return location != null ? location.getLongitude() : LOCATION_ERROR_CODE;
    }

    /**
     * Shows settings dialog to redirect to settings if cannot get gps access
     */
    public void showSettingsDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(R.string.no_gps);
        alertDialog.setMessage(R.string.to_settings);
        setDialogPositiveButtonAction(alertDialog);
        setDialogNegativeButton(alertDialog);
        alertDialog.show();
    }

    /**
     * Stops using GPS listener
     */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    private void setDialogPositiveButtonAction(@NonNull final AlertDialog.Builder alertDialog) {
        alertDialog.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
    }

    private void setDialogNegativeButton(@NonNull final AlertDialog.Builder alertDialog) {
        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
    }
}
