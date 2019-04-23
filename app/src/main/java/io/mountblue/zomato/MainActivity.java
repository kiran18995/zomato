package io.mountblue.zomato;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mountblue.zomato.adapter.RestaurantAdapter;
import io.mountblue.zomato.data.remote.retrofit.ApiClient;
import io.mountblue.zomato.data.remote.retrofit.RestaurantService;
import io.mountblue.zomato.module.Restaurant;
import io.mountblue.zomato.module.RestaurantResponse;
import io.mountblue.zomato.data.remote.RestaurantViewModel;
import io.mountblue.zomato.view.gooutfragment.GoOutFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    //private RestaurantViewModel restaurantViewModel;
    @BindView(R.id.restaurantList)
    RecyclerView restaurantRecyclerView;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

    private PagedList<Restaurant> restaurantPagedList;
    private RestaurantViewModel restaurantViewModel;

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
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
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
