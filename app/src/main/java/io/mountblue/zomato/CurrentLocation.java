package io.mountblue.zomato;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;

import io.mountblue.zomato.util.SharedPrefrenceAddress;

import static android.content.Context.LOCATION_SERVICE;

public class CurrentLocation {
    private static final String TAG = "Location";
    public static final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 100;
    private Context context;

    public CurrentLocation(Context context) {
        this.context = context;
    }

    public Location getLastBestLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            int accessCoarsePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
            int accessFinePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

            if (accessCoarsePermission != PackageManager.PERMISSION_GRANTED || accessFinePermission != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
                ActivityCompat.requestPermissions((Activity) context, permissions, REQUEST_ID_ACCESS_COURSE_FINE_LOCATION);
            }
        }
        LocationManager mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        Location locationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNet = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        long GPSLocationTime = 0;
        if (null != locationGPS) {
            GPSLocationTime = locationGPS.getTime();
        }

        long NetLocationTime = 0;

        if (null != locationNet) {
            NetLocationTime = locationNet.getTime();
        }

        if (0 < GPSLocationTime - NetLocationTime) {
            return locationGPS;
        } else {
            return locationNet;
        }
    }

    public double getCurrentLatitude() {
        try {
            return getLastBestLocation().getLatitude();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getCurrentLongitude() {
        try {
            return getLastBestLocation().getLongitude();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getCurrentAddress() {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context);

        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        /*if (!provider.contains("gps")) { //if gps is disabled
            Intent gpsOptionsIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(gpsOptionsIntent);
        }*/

        try {
            addresses = geocoder.getFromLocation(getCurrentLatitude(), getCurrentLongitude(), 5);
            String address = addresses.get(0).getAddressLine(0);
            return address;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return " ";
    }

    public void setSharedPrefrenceAddress() {
        String[] addresses = getCurrentAddress().split(",");
        String address = addresses[2] + ", " + addresses[3];
        SharedPrefrenceAddress sharedPrefrenceAddress = new SharedPrefrenceAddress(context);
        sharedPrefrenceAddress.setDefaultAddress("addressTitle", address.trim());
        sharedPrefrenceAddress.setDefaultAddress("addressLatitude", String.valueOf(getCurrentLatitude()));
        sharedPrefrenceAddress.setDefaultAddress("addressLongitude", String.valueOf(getCurrentLongitude()));
    }
}
