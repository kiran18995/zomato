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
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mountblue.zomato.CurrentLocation;
import io.mountblue.zomato.R;
import io.mountblue.zomato.RestaurantDetails;
import io.mountblue.zomato.RoundedTransformation;
import io.mountblue.zomato.module.Restaurant;
import io.mountblue.zomato.util.SharedPrefrenceAddress;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private static final String TAG = "RestaurantAdapter";
    private List<Restaurant> restaurantList;
    private Context context;

    public SearchAdapter(Context context) {
        this.context = context;
    }

    public List<Restaurant> getRestaurantList() {
        return restaurantList;
    }

    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cell, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.restaurantName.setText(restaurantList.get(position).getRestaurant().getName());
        holder.foodType.setText(String.format("%s Places", restaurantList.get(position).getRestaurant().getCuisines()));
        if (!restaurantList.get(position).getRestaurant().getThumb().isEmpty()) {
            //Log.e(TAG, "onBindViewHolder: " + restaurantList.get(position).getRestaurant().getPhotosUrl());
            Picasso.with(context).load(restaurantList.get(position).getRestaurant().getThumb())
                    .placeholder(R.drawable.food_placeholder)
                    .transform(new RoundedTransformation(10, 0))
                    .fit().centerCrop()
                    .into(holder.restaurantImage);
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable("restaurant", restaurantList.get(position).getRestaurant());
        bundle.putParcelable("userRating", restaurantList.get(position).getRestaurant().getUserRating());
        bundle.putParcelable("location", restaurantList.get(position).getRestaurant().getLocation());
        holder.restaurantCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RestaurantDetails.class);
                intent.putExtra("restaurant", bundle);
                context.startActivity(intent);
            }
        });

        holder.restaurantRating.setText(restaurantList.get(position).getRestaurant().getUserRating().getAggregateRating());
        if (restaurantList.get(position).getRestaurant().getAverageCostForTwo() != null) {
            holder.restaurantRate.setText(restaurantList.get(position).getRestaurant().getAverageCostForTwo().toString());
        }
        float distance = setEstimateTime(restaurantList.get(position).getRestaurant().getLocation().getLatitude()
                , restaurantList.get(position).getRestaurant().getLocation().getLongitude());
        holder.estimateTime.setText(getEstimateTimeRange(distance));
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }


    public class SearchViewHolder extends RecyclerView.ViewHolder {

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

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private float setEstimateTime(String restaurantLat, String restaurantLon) {
        SharedPrefrenceAddress sharedPrefrenceAddress = new SharedPrefrenceAddress(context);
        Location startPoint = new Location("startPoint");
        Location endPoint = new Location("endPoint");
        double currentLat = Double.parseDouble(sharedPrefrenceAddress.getDefaultAddress("addressLatitude"));
        double currentLon = Double.parseDouble(sharedPrefrenceAddress.getDefaultAddress("addressLongitude"));

        startPoint.setLatitude(currentLat);
        startPoint.setLongitude(currentLon);
        endPoint.setLatitude(Double.parseDouble(restaurantLat));
        endPoint.setLongitude(Double.parseDouble(restaurantLon));

        return startPoint.distanceTo(endPoint) / 1000;
    }

    private String getEstimateTimeRange(float distance) {
        if (distance >= 10) {
            return context.getString(R.string.fifty_to_sixty);
        } else if (distance < 10 && distance >= 7) {
            return context.getString(R.string.fourty_to_fifty);
        } else if (distance < 7 && distance > 4) {
            return context.getString(R.string.twentyfive_to_thirtyfive);
        } else {
            return context.getString(R.string.fifteen_to_twentyfive);
        }
    }
}
