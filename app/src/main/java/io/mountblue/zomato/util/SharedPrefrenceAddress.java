package io.mountblue.zomato.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.util.List;

public class SharedPrefrenceAddress {

    private Context context;

    public SharedPrefrenceAddress(Context context) {
        this.context = context;
    }

    public void setDefaultAddress(String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getDefaultAddress(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    public String getAddress() {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context);
        double latitude = Double.parseDouble(getDefaultAddress("addressLatitude"));
        double longitude = Double.parseDouble(getDefaultAddress("addressLongitude"));

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 5);
            String address = addresses.get(0).getAddressLine(0);
            return address;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return " ";
    }
}
