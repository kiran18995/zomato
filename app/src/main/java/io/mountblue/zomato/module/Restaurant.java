
package io.mountblue.zomato.module;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Restaurant implements Parcelable {

    @SerializedName("restaurant")
    @Expose
    private Restaurant_ restaurant;

    protected Restaurant(Parcel in) {
        restaurant = in.readParcelable(Restaurant_.class.getClassLoader());
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public Restaurant_ getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant_ restaurant) {
        this.restaurant = restaurant;
    }

    public static final DiffUtil.ItemCallback<Restaurant> CALLBACK = new DiffUtil.ItemCallback<Restaurant>() {
        @Override
        public boolean areItemsTheSame(@NonNull Restaurant oldItem, @NonNull Restaurant newItem) {
            return oldItem.getRestaurant().getId() == newItem.getRestaurant().getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Restaurant oldItem, @NonNull Restaurant newItem) {
            return true;
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(restaurant, flags);
    }
}
