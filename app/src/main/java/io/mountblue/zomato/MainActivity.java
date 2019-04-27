package io.mountblue.zomato;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;
import io.mountblue.zomato.adapter.ViewPagerAdapter;
import io.mountblue.zomato.util.NonSwipeableViewPager;
import io.mountblue.zomato.view.activity.AddressActivity;
import io.mountblue.zomato.view.activity.SearchActivity;
import io.mountblue.zomato.view.fragment.GoOutFragment;
import io.mountblue.zomato.view.fragment.GoldFragment;
import io.mountblue.zomato.view.fragment.OrderFragment;
import io.mountblue.zomato.view.fragment.SearchFragment;

public class MainActivity extends DaggerAppCompatActivity {
    private static final String TAG = "MainActivity";


    @BindView(R.id.view_pager)
    NonSwipeableViewPager viewPager;
    @BindView(R.id.nav_view)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.delivery_address)
    TextView deliveryAddress;
    @BindView(R.id.address_heading)
    TextView addressHeading;
    @BindView(R.id.search_query)
    TextView searchQuery;
    @BindView(R.id.layout_location)
    LinearLayout linearLayoutLocation;
    @BindView(R.id.layout_search)
    LinearLayout linearLayoutSearch;
    @BindView(R.id.search_view)
    LinearLayout searchView;
    @BindView(R.id.sort_by)
    ImageView sortBy;
    private MenuItem prevMenuItem;

    private CurrentLocation currentLocation;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setClipToPadding(false);
                    viewPager.setPageMargin(60);
                    viewPager.setCurrentItem(0, false);
                    linearLayoutSearch.setVisibility(View.VISIBLE);
                    Log.e(TAG, "onNavigationItemSelected: " + viewPager.getCurrentItem());
                    linearLayoutLocation.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_dashboard:
                    linearLayoutSearch.setVisibility(View.GONE);
                    linearLayoutLocation.setVisibility(View.VISIBLE);
                    viewPager.setCurrentItem(1, false);
                    Log.e(TAG, "onNavigationItemSelected: " + viewPager.getCurrentItem());
                    return true;
                case R.id.navigation_notifications:
                    linearLayoutSearch.setVisibility(View.GONE);
                    linearLayoutLocation.setVisibility(View.GONE);
                    viewPager.setCurrentItem(2, false);
                    Log.e(TAG, "onNavigationItemSelected: " + viewPager.getCurrentItem());
                    return true;
                case R.id.navigation_search:
                    linearLayoutSearch.setVisibility(View.GONE);
                    linearLayoutLocation.setVisibility(View.VISIBLE);
                    viewPager.setCurrentItem(3, false);
                    Log.e(TAG, "onNavigationItemSelected: " + viewPager.getCurrentItem());
                    return true;
            }
            return false;
        }
    };

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (prevMenuItem != null) {
                prevMenuItem.setChecked(false);
            } else {
                bottomNavigationView.getMenu().getItem(0).setChecked(false);
            }
            Log.e("page", "onPageSelected: " + position);
            bottomNavigationView.getMenu().getItem(position).setChecked(true);
            prevMenuItem = bottomNavigationView.getMenu().getItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Inject
    String someString;

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        currentLocation = new CurrentLocation(this);

        bottomNavigationView.setItemIconTintList(null);

        Log.e(TAG, "onCreate: " + someString);

        viewPager.addOnPageChangeListener(onPageChangeListener);

        viewPager.setOffscreenPageLimit(4);

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("addressHeading", addressHeading.getText());
                intent.putExtra("deliveryAddress", deliveryAddress.getText());
                startActivity(intent);
            }
        });

        sortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        setCurrentAddress();

        setupViewPager(viewPager);
        linearLayoutLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addressIntent = new Intent(MainActivity.this, AddressActivity.class);
                startActivity(addressIntent);
            }
        });

        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                if (viewPager.getCurrentItem() == 0) {
                page.setTranslationY(150);
                }else {
                    page.setTranslationY(0);
                }
            }
        });
    }

    private void setCurrentAddress() {
        deliveryAddress.setText(currentLocation.getCurrentAddress());
        addressHeading.setText(getAddressHeading());
    }

    private String getAddressHeading() {
        String[] locality = currentLocation.getCurrentAddress().split(",");
        String addressHeading = locality[2] + ", " + locality[3];
        return addressHeading.trim();
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        OrderFragment orderFragment = new OrderFragment();

        GoOutFragment goOutFragment = new GoOutFragment();

        GoldFragment goldFragment = new GoldFragment();

        SearchFragment searchFragment = new SearchFragment();

        adapter.addFragment(orderFragment);

        adapter.addFragment(goOutFragment);

        adapter.addFragment(goldFragment);

        adapter.addFragment(searchFragment);

        viewPager.setAdapter(adapter);

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
