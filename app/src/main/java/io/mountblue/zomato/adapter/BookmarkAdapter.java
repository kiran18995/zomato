package io.mountblue.zomato.adapter;

import android.content.Context;
import android.content.Intent;
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
import io.mountblue.zomato.R;
import io.mountblue.zomato.RoundedTransformation;
import io.mountblue.zomato.module.Restaurant_;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder> {
    private static final String TAG = "BookmarkAdapter";

    private List<Restaurant_> restaurant;

    private Context context;

    public BookmarkAdapter(Context context) {
        this.context = context;
    }

    public List<Restaurant_> getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(List<Restaurant_> restaurant) {
        this.restaurant = restaurant;
    }

    @NonNull
    @Override
    public BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.bookmark_item_cell, parent, false);
        return new BookmarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkViewHolder holder, int position) {
        holder.restaurantName.setText(restaurant.get(position).getName());
        if (!restaurant.get(position).getThumb().isEmpty()) {
            //Log.e(TAG, "onBindViewHolder: " + restaurantList.get(position).getRestaurant().getPhotosUrl());
            Picasso.with(context).load(restaurant.get(position).getThumb())
                    .placeholder(R.drawable.food_placeholder)
                    .transform(new RoundedTransformation(10, 0))
                    .fit().centerCrop()
                    .into(holder.restaurantImage);
        }
        /*Bundle bundle = new Bundle();
        bundle.putParcelable("restaurant", restaurant);
        bundle.putParcelable("userRating", restaurant.getUserRating());
        bundle.putParcelable("location", restaurant.getLocation());
        holder.restaurantCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RestaurantDetails.class);
                intent.putExtra("restaurant", bundle);
                context.startActivity(intent);
            }
        });
*/
    }

    @Override
    public int getItemCount() {
        return restaurant.size();
    }

    public class BookmarkViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.restaurant_name)
        TextView restaurantName;
        @BindView(R.id.restaurant_rating)
        TextView restaurantRating;
        @BindView(R.id.main_image)
        ImageView restaurantImage;
        @BindView(R.id.restaurant_card)
        LinearLayout restaurantCard;
        @BindView(R.id.restaurant_address)
        TextView restaurantAddress;

        public BookmarkViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
