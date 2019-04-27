package io.mountblue.zomato.adapter;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mountblue.zomato.CurrentLocation;
import io.mountblue.zomato.R;
import io.mountblue.zomato.RestaurantDetails;
import io.mountblue.zomato.RoundedTransformation;
import io.mountblue.zomato.module.Restaurant;

public class RestaurantAdapter extends PagedListAdapter<Restaurant, RestaurantAdapter.RestaurantViewHolder> {

    private static final String TAG = "RestaurantAdapter";
    // private List<Restaurant> restaurantList;
    private Context context;

    public RestaurantAdapter(Context context) {
        super(Restaurant.CALLBACK);
        this.context = context;
    }

   /* public List<Restaurant> getRestaurantList() {
        return restaurantList;
    }

    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }*/

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cell, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {

        Bundle bundle = new Bundle();
        bundle.putParcelable("restaurant", getItem(position));
        bundle.putParcelable("userRating", getItem(position).getRestaurant().getUserRating());
        bundle.putParcelable("location", getItem(position).getRestaurant().getLocation());
        holder.restaurantCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RestaurantDetails.class);
                intent.putExtra("restaurant", bundle);
                context.startActivity(intent);
            }
        });
        holder.restaurantName.setText(getItem(position).getRestaurant().getName());
        holder.foodType.setText(String.format("%s Places", getItem(position).getRestaurant().getCuisines()));
        if (!getItem(position).getRestaurant().getThumb().isEmpty()) {
            //Log.e(TAG, "onBindViewHolder: " + restaurantList.get(position).getRestaurant().getPhotosUrl());
            Picasso.with(context).load(getItem(position).getRestaurant().getThumb())
                    .placeholder(R.drawable.food_placeholder)
                    .transform(new RoundedTransformation(10, 0))
                    .fit().centerCrop()
                    .into(holder.restaurantImage);
        }
        holder.restaurantRating.setText(getItem(position).getRestaurant().getUserRating().getAggregateRating());
        if (getItem(position).getRestaurant().getAverageCostForTwo() != null) {
            holder.restaurantRate.setText(getItem(position).getRestaurant().getAverageCostForTwo().toString());
        }
        float distance = setEstimateTime(getItem(position).getRestaurant().getLocation().getLatitude()
                ,getItem(position).getRestaurant().getLocation().getLongitude());
        holder.estimateTime.setText(getEstimateTimeRange(distance));
    }


    public class RestaurantViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rate_data)
        TextView restaurantRate;
        @BindView(R.id.restaurant_name)
        TextView restaurantName;
        @BindView(R.id.restaurant_rating)
        TextView restaurantRating;
        @BindView(R.id.main_image)
        ImageView restaurantImage;
        @BindView(R.id.food_type)
        TextView foodType;
        @BindView(R.id.estimate_time)
        TextView estimateTime;
        @BindView(R.id.restaurant_card)
        LinearLayout restaurantCard;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private float setEstimateTime(String restaurantLat, String restaurantLon) {
        CurrentLocation currentLocation = new CurrentLocation(context);
        Location startPoint = new Location("startPoint");
        Location endPoint = new Location("endPoint");
        double currentLat = currentLocation.getCurrentLatitude();
        double currentLon = currentLocation.getCurrentLongitude();

        startPoint.setLatitude(currentLat);
        startPoint.setLongitude(currentLon);
        endPoint.setLatitude(Double.parseDouble(restaurantLat));
        endPoint.setLongitude(Double.parseDouble(restaurantLon));

        return startPoint.distanceTo(endPoint) / 1000;
    }

    private String getEstimateTimeRange(float distance) {
        if (distance >= 10) {
            return "50-60 Mins";
        } else if (distance < 10 && distance >= 7) {
            return "40-50 Mins";
        } else if (distance < 7 && distance > 4) {
            return "25-35 Mins";
        } else {
            return "15-25 Mins";
        }
    }

}
