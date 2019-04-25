package io.mountblue.zomato;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;
import io.mountblue.zomato.adapter.RestaurantAdapter;
import io.mountblue.zomato.module.Restaurant;
import io.mountblue.zomato.data.remote.RestaurantViewModel;
import io.mountblue.zomato.view.activity.SearchActivity;
import io.mountblue.zomato.view.fragment.GoOutFragment;
import io.mountblue.zomato.view.fragment.SearchFragment;

public class MainActivity extends DaggerAppCompatActivity {
    private static final String TAG = "MainActivity";
    @BindView(R.id.restaurantList)
    RecyclerView restaurantRecyclerView;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.delivery_address)
    TextView deliveryAddress;
    @BindView(R.id.address_heading)
    TextView addressHeading;
    @BindView(R.id.layout_search)
    LinearLayout layoutSearch;
    @BindView(R.id.layout_location)
    LinearLayout layoutLocation;
    @BindView(R.id.search_query)
    TextView searchQuery;
    Fragment fragment;
    CurrentLocation currentLocation;

    private PagedList<Restaurant> restaurantPagedList;

    private RestaurantViewModel restaurantViewModel;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    searchQuery.clearFocus();
                    restaurantRecyclerView.setVisibility(View.VISIBLE);
                    layoutSearch.setVisibility(View.VISIBLE);
                    layoutSearch.setVisibility(View.VISIBLE);
                    layoutLocation.setVisibility(View.VISIBLE);
                    layoutLocation.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_dashboard:
                    fragment = new GoOutFragment();
                    showFragment(fragment);
                    restaurantRecyclerView.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    layoutSearch.setVisibility(View.GONE);
                    layoutLocation.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_notifications:
                    restaurantRecyclerView.setVisibility(View.GONE);
                    layoutSearch.setVisibility(View.GONE);
                    layoutLocation.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_search:
                    fragment = new SearchFragment();
                    showFragment(fragment);
                    restaurantRecyclerView.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    layoutSearch.setVisibility(View.GONE);
                    layoutLocation.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }
    };

    @Inject
    String someString;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentLocation = new CurrentLocation(this);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        fragment = new GoOutFragment();
        navView.setItemIconTintList(null);
        ButterKnife.bind(this);

        Log.e(TAG, "onCreate: " + someString);
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
        showFragment(fragment);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setCurrentAddress();

        searchQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("addressHeading",addressHeading.getText());
                intent.putExtra("deliveryAddress",deliveryAddress.getText());
                startActivity(intent);
            }
        });
    }

    private void setCurrentAddress() {
        CurrentLocation currentLocation = new CurrentLocation(this);
        deliveryAddress.setText(currentLocation.getCurrentAddress());
        addressHeading.setText(getAddressHeading());
    }

    private String getAddressHeading() {
        String[] locality = currentLocation.getCurrentAddress().split(",");
        String addressHeading = locality[2] + ", " + locality[3];
        return addressHeading.trim();
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.frameLayout, fragment)
                .commit();
    }

    private void setRecyclerView() {
        RestaurantAdapter restaurantAdapter = new RestaurantAdapter(MainActivity.this);
        restaurantAdapter.submitList(restaurantPagedList);
        restaurantRecyclerView.setAdapter(restaurantAdapter);
        restaurantAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }
}
