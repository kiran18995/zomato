package io.mountblue.zomato;

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
    @BindView(R.id.layout_search)
    LinearLayout layoutSearch;
    @BindView(R.id.layout_location)
    LinearLayout layoutLocation;

    private PagedList<Restaurant> restaurantPagedList;

    private RestaurantViewModel restaurantViewModel;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    restaurantRecyclerView.setVisibility(View.VISIBLE);
                    layoutSearch.setVisibility(View.VISIBLE);
                    layoutSearch.setVisibility(View.VISIBLE);
                    layoutLocation.setVisibility(View.VISIBLE);
                    layoutLocation.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_dashboard:
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
                    return true;
            }
            return false;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
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
        restaurantRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && dy < 100){
                    layoutLocation.setVisibility(View.GONE);
                }else if (dy < 0 && dy >-100){
                    layoutLocation.setVisibility(View.VISIBLE);
                }
                Log.e(TAG, "onScrolled: "+dy+" - "+dx );
            }

        });

        Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);
        layoutLocation.animate().translationY(3);
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

        setCurrentAddress();
    }

    private void setCurrentAddress() {
        CurrentLocation currentLocation = new CurrentLocation(this);
        deliveryAddress.setText(currentLocation.getCurrentAddress());
        String[] locality = currentLocation.getCurrentAddress().split(",");
        String street = locality[2] + ", " + locality[3];
        addressHeading.setText(street.trim());
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
}
