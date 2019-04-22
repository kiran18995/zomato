package io.mountblue.zomato.data.remote;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import io.mountblue.zomato.data.RepoRestaurantResult;
import io.mountblue.zomato.data.remote.pagination.RestaurantPageKeyedDataSourceFactory;
import io.mountblue.zomato.data.remote.retrofit.RestaurantService;
import io.mountblue.zomato.module.Restaurant;

public class RestaurantRemoteDataSource {

    private static final int PAGE_SIZE = 20;

    private final RestaurantService restaurantService;

    private static volatile RestaurantRemoteDataSource sInstance;

    public RestaurantRemoteDataSource(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    public static RestaurantRemoteDataSource getInstance(RestaurantService restaurantService) {
        if (sInstance == null) {
            synchronized (RestaurantRemoteDataSource.class) {
                if (sInstance == null) {
                    sInstance = new RestaurantRemoteDataSource(restaurantService);
                }
            }
        }
        return sInstance;
    }

    public RepoRestaurantResult loadRestaurants() {
        RestaurantPageKeyedDataSourceFactory sourceFactory = new RestaurantPageKeyedDataSourceFactory(restaurantService);

        // paging configuration
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(PAGE_SIZE)
                .build();

        // Get the paged list
        LiveData<PagedList<Restaurant>> moviesPagedList = new LivePagedListBuilder<>(sourceFactory, config)
                .build();

        return new RepoRestaurantResult(moviesPagedList, sourceFactory.sourceLiveData);
    }

}

