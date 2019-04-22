package io.mountblue.zomato.data;

import io.mountblue.zomato.data.remote.DataSource;
import io.mountblue.zomato.data.remote.RestaurantRemoteDataSource;

public class RestaurantRepository implements DataSource {

    private static volatile RestaurantRepository sInstance;

    private final RestaurantRemoteDataSource mRemoteDataSource;

    public RestaurantRepository(RestaurantRemoteDataSource mRemoteDataSource) {
        this.mRemoteDataSource = mRemoteDataSource;
    }

    public static RestaurantRepository getInstance(RestaurantRemoteDataSource restaurantRemoteDataSource) {
        if (sInstance == null) {
            sInstance = new RestaurantRepository(restaurantRemoteDataSource);
        }
        return sInstance;
    }

    @Override
    public RepoRestaurantResult loadRestaurants() {
        return mRemoteDataSource.loadRestaurants();
    }
}
