package io.mountblue.zomato.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mountblue.zomato.RoundedTransformation;
import io.mountblue.zomato.R;
import io.mountblue.zomato.module.Restaurant;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private static final String TAG = "RestaurantAdapter";
    private List<Restaurant> restaurantList;
    private Context context;

    public RestaurantAdapter(Context context) {
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
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cell, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        holder.restaurantName.setText(restaurantList.get(position).getRestaurant().getName());
        holder.foodType.setText(restaurantList.get(position).getRestaurant().getCuisines());
        if (!restaurantList.get(position).getRestaurant().getThumb().isEmpty()) {
            //Log.e(TAG, "onBindViewHolder: " + restaurantList.get(position).getRestaurant().getPhotosUrl());
            Picasso.with(context).load(restaurantList.get(position).getRestaurant().getThumb())
                    .placeholder(R.drawable.food_placeholder)
                    .transform(new RoundedTransformation(10,0))
                    .fit().centerCrop()
                    .into(holder.restaurantImage);
        }
        holder.restaurantRating.setText(restaurantList.get(position).getRestaurant().getUserRating().getAggregateRating());
        if (restaurantList.get(position).getRestaurant().getAverageCostForTwo() != null) {
            holder.restaurantRate.setText(restaurantList.get(position).getRestaurant().getAverageCostForTwo().toString());
        }
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
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

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
