package io.mountblue.zomato;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mountblue.zomato.adapter.RestaurantAdapter;
import io.mountblue.zomato.module.Restaurant;
import io.mountblue.zomato.data.remote.RestaurantViewModel;
import io.mountblue.zomato.view.gooutfragment.GoOutFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @BindView(R.id.restaurantList)
    RecyclerView restaurantRecyclerView;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.delivery_address)
    TextView deliveryAddress;
    @BindView(R.id.address_heading)
    TextView addressHeading;

    private PagedList<Restaurant> restaurantPagedList;

    private RestaurantViewModel restaurantViewModel;
    public static final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 100;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    restaurantRecyclerView.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_dashboard:
                    restaurantRecyclerView.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_notifications:
                    restaurantRecyclerView.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_search:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        restaurantRecyclerView.setLayoutManager(layoutManager);
        restaurantRecyclerView.smoothScrollToPosition(1);
        restaurantViewModel = new RestaurantViewModel();

        restaurantViewModel.getPagedList().observe(this, new Observer<PagedList<Restaurant>>() {
            @Override
            public void onChanged(PagedList<Restaurant> restaurants) {
                restaurantPagedList = restaurants;
                setRecyclerView();
            }
        });

        frameLayout.setVisibility(View.GONE);
        showFragment();

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getCurrentLocation();
    }

    private void showFragment() {
        Fragment fragment = new GoOutFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment)
                .commit();
    }

    private void setRecyclerView() {
        RestaurantAdapter restaurantAdapter = new RestaurantAdapter(MainActivity.this);
        restaurantAdapter.submitList(restaurantPagedList);
        restaurantRecyclerView.setAdapter(restaurantAdapter);
        restaurantAdapter.notifyDataSetChanged();
    }

    private Location getLastBestLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            int accessCoarsePermission
                    = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            int accessFinePermission
                    = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);


            if (accessCoarsePermission != PackageManager.PERMISSION_GRANTED
                    || accessFinePermission != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION};
                ActivityCompat.requestPermissions(this, permissions,
                        REQUEST_ID_ACCESS_COURSE_FINE_LOCATION);

            }
        }
        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location locationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNet = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        long GPSLocationTime = 0;
        if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }

        long NetLocationTime = 0;

        if (null != locationNet) {
            NetLocationTime = locationNet.getTime();
        }

        if ( 0 < GPSLocationTime - NetLocationTime ) {
            return locationGPS;
        }
        else {
            return locationNet;
        }
    }

    private void getCurrentLocation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    double longitude = getLastBestLocation().getLongitude();
                    double latitude = getLastBestLocation().getLatitude();
                    setAddress(latitude,longitude);
                    Log.e("myLocation",latitude+","+longitude);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this);

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 5);
            String address = addresses.get(0).getAddressLine(0);
            deliveryAddress.setText(address);
            String[] locality = address.split(",");
            String headingAddress = locality[2]+","+locality[3];
            addressHeading.setText(headingAddress.trim());
            Log.e(TAG, "setAddress: "+ locality[2]+","+locality[3]);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
