package io.mountblue.zomato;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mountblue.zomato.adapter.ReviewAdapter;
import io.mountblue.zomato.data.local.RestaurantEntity;
import io.mountblue.zomato.module.Location;
import io.mountblue.zomato.module.Restaurant;
import io.mountblue.zomato.module.Restaurant_;
import io.mountblue.zomato.module.UserRating;
import io.mountblue.zomato.module.reviewmodule.UserReview;
import io.mountblue.zomato.util.NetworkState;
import io.mountblue.zomato.view.CollectionViewModel;
import io.mountblue.zomato.viewmodel.ViewModelProviderFactory;

public class RestaurantDetails extends AppCompatActivity {
    private static final String TAG = "RestaurantDetails";
    public static final String CROP_IMAGE = "?fit=around%7C200%3A200&crop=200%3A200%3B%2A%2C%2A";
    @BindView(R.id.backdrop)
    ImageView backDrop;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.restaurant_title)
    TextView restaurantTitle;
    @BindView(R.id.rating_average)
    TextView averageRating;
    @BindView(R.id.sub_title)
    TextView subTitle;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.recycler_review)
    RecyclerView recycler_review;
    @BindView(R.id.search_progress_bar)
    ProgressBar progressBar;
    private static boolean isbookmarked;
    @BindView(R.id.direction)
    LinearLayout directionButton;

    DirectionBottomSheet directionBottomSheet;

    private CollectionViewModel collectionViewModel;
    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    private Restaurant_ restaurant;
    private UserRating userRating;
    private Location restaurantLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_restaurant_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recycler_review.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        Bundle bundle = intent.getParcelableExtra("restaurant");
        restaurant = bundle.getParcelable("restaurant");
        userRating = bundle.getParcelable("userRating");
        restaurantLocation = bundle.getParcelable("location");
        if (!restaurant.getThumb().isEmpty()) {
            Picasso.with(this).load(restaurant.getThumb().replace(CROP_IMAGE, "")).placeholder(R.drawable.detail_placeholder).centerCrop().fit().into(backDrop);
        }
        restaurantTitle.setText(restaurant.getName());
        averageRating.setText(userRating.getAggregateRating());
        subTitle.setText(String.format("%s Places", restaurant.getCuisines()));
        location.setText(restaurantLocation.getAddress());
        collectionViewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(CollectionViewModel.class);
        collectionViewModel.getAllReview(Integer.parseInt(restaurant.getId())).observe(this, new Observer<List<UserReview>>() {
            @Override
            public void onChanged(List<UserReview> userReviews) {
                Log.e(TAG, "onChanged: " + userReviews.size());
                setRecyclerView(userReviews);
            }
        });
        collectionViewModel.getNetwork().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(NetworkState networkState) {
                if (networkState == NetworkState.LOADING) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        directionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionBottomSheet = new DirectionBottomSheet(restaurantLocation.getLatitude(), restaurantLocation.getLongitude(), restaurantLocation.getAddress());
                directionBottomSheet.show(getSupportFragmentManager(), directionBottomSheet.getTag());
            }
        });
        collectionViewModel.getRestaurant(RestaurantDetails.this,
                restaurant.getId())
                .observe(this, new Observer<List<RestaurantEntity>>() {
                    @Override
                    public void onChanged(List<RestaurantEntity> restaurant_s) {
                        Log.e(TAG, "onChanged: " + restaurant_s.size());
                        isbookmarked = restaurant_s.size() > 0;
                    }
                });
    }

    private void setRecyclerView(List<UserReview> userReviews) {
        ReviewAdapter reviewAdapter = new ReviewAdapter(this, userReviews);
        recycler_review.setAdapter(reviewAdapter);
        reviewAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_restaurant_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
            case R.id.action_share: {
                String url = restaurant.getUrl();
                Intent i = new Intent(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_TEXT, url);
                i.setType("text/plain");
                startActivity(i);
                break;
            }
            case R.id.action_bookmark: {
                Log.e(TAG, "onOptionsItemSelected: " + restaurant.getId());
                if (isbookmarked) {
                    collectionViewModel.removeBookmark(RestaurantDetails.this
                            , restaurant.getId());
                    isbookmarked = false;
                } else {
                    RestaurantEntity restaurantEntity = new RestaurantEntity();
                    Log.e(TAG, "onOptionsItemSelected: " + userRating.getAggregateRating());
                    restaurantEntity.setId(restaurant.getId());
                    restaurantEntity.setCuisines(restaurant.getCuisines());
                    restaurantEntity.setName(restaurant.getName());
                    restaurantEntity.setUrl(restaurant.getUrl());
                    restaurantEntity.setAddress(restaurantLocation.getAddress());
                    restaurantEntity.setThumb(restaurant.getThumb());
                    restaurantEntity.setAggregateRating(userRating.getAggregateRating());
                    collectionViewModel.saveToBookmark(restaurantEntity, RestaurantDetails.this);
                    isbookmarked = true;
                }
            }
        }
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
