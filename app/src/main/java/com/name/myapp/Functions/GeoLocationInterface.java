package com.eroinnovations.qelmedistore;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class GeoLocationInterface {

    private Context context;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    public GeoLocationInterface(Context context) {
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @JavascriptInterface
    public void requestLocation() {
        // Check if location permission is granted
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            
            // Request the missing permissions from the user
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            
            Toast.makeText(context, "Requesting location permissions", Toast.LENGTH_SHORT).show();
            return;
        }

        startLocationUpdates();
    }

    private void startLocationUpdates() {
        try {
            // Get last known location as a fallback (optional)
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                latitude = lastKnownLocation.getLatitude();
                longitude = lastKnownLocation.getLongitude();
            }

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}

                @Override
                public void onProviderEnabled(String provider) {}

                @Override
                public void onProviderDisabled(String provider) {}
            };

            // Check permissions again before requesting location updates
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
            } else {
                // Permission is not granted
                Toast.makeText(context, "Location permission not granted", Toast.LENGTH_SHORT).show();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public double getLatitude() {
        // Return the last known or updated latitude
        return latitude != 0.0 ? latitude : -1; // Return -1 if still waiting for update
    }

    @JavascriptInterface
    public double getLongitude() {
        // Return the last known or updated longitude
        return longitude != 0.0 ? longitude : -1; // Return -1 if still waiting for update
    }

    @JavascriptInterface
    public void stopLocationUpdates() {
        if (locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    // Handling the result of the permission request
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start location updates
                startLocationUpdates();
            } else {
                // Permission denied
                Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
