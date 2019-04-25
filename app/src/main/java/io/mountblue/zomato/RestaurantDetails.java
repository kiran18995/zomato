package io.mountblue.zomato;

import android.content.Intent;
import android.os.Bundle;

import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mountblue.zomato.module.Location;
import io.mountblue.zomato.module.Restaurant;
import io.mountblue.zomato.module.UserRating;

public class RestaurantDetails extends AppCompatActivity {
    private static final String TAG = "RestaurantDetails";
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

    private Restaurant restaurant;
    private UserRating userRating;
    private Location restaurantLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getParcelableExtra("restaurant");
        restaurant = bundle.getParcelable("restaurant");
        userRating = bundle.getParcelable("userRating");
        restaurantLocation = bundle.getParcelable("location");
        if (!restaurant.getRestaurant().getThumb().isEmpty()) {
            Picasso.with(this).load(restaurant.getRestaurant().getThumb().replace("?fit=around%7C200%3A200&crop=200%3A200%3B%2A%2C%2A", "")).placeholder(R.drawable.food_placeholder).centerCrop().fit().into(backDrop);
        }
        restaurantTitle.setText(restaurant.getRestaurant().getName());
        averageRating.setText(userRating.getAggregateRating());
        subTitle.setText(String.format("%s Places", restaurant.getRestaurant().getCuisines()));
        location.setText(restaurantLocation.getAddress());
    }
}
