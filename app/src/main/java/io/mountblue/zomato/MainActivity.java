package io.mountblue.zomato;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
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
import io.mountblue.zomato.view.Injection;
import io.mountblue.zomato.view.RestaurantViewModel;
import io.mountblue.zomato.view.ViewModelFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    //private RestaurantViewModel restaurantViewModel;
    @BindView(R.id.restaurantList)
    RecyclerView restaurantRecyclerView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //  mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    //  mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    // mTextMessage.setText(R.string.title_notifications);
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
        // mTextMessage = findViewById(R.id.message);
//        restaurantViewModel = obtainViewModel(MainActivity.this);
//        restaurantViewModel.getPagedList().observe(this, new Observer<PagedList<Restaurant>>() {
//            @Override
//            public void onChanged(PagedList<Restaurant> restaurants) {
//                Log.e(TAG, "onChanged: ");
//            }
//        });

        RestaurantService apiService =
                ApiClient.getRetrofitInstance().create(RestaurantService.class);
        apiService.getRestaurants().enqueue(new Callback<RestaurantResponse>() {
            @Override
            public void onResponse(Call<RestaurantResponse> call, Response<RestaurantResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e(TAG, "onResponse: hi" + response.body().getResultsFound());
                    Log.e(TAG, "onResponse: " + response.body().getRestaurants().size());
                    RestaurantAdapter restaurantAdapter = new RestaurantAdapter(MainActivity.this);
                    restaurantAdapter.setRestaurantList(response.body().getRestaurants());
                    restaurantRecyclerView.setAdapter(restaurantAdapter);
                } else {
                    Log.e(TAG, "onResponse: Error ");
                }
            }

            @Override
            public void onFailure(Call<RestaurantResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public RestaurantViewModel obtainViewModel(Activity activity) {
        ViewModelFactory factory = Injection.provideViewModelFactory(activity);
        return ViewModelProviders.of(this, factory).get(RestaurantViewModel.class);
    }

}
