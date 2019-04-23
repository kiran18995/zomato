package io.mountblue.zomato.data.remote.pagination;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import io.mountblue.zomato.data.remote.retrofit.RestaurantService;

public class RestaurantDataSourceFactory extends DataSource.Factory {

    private RestaurantDataSource restaurantDataSource;
    private RestaurantService restaurantService;
    private MutableLiveData<RestaurantDataSource> mutableLiveData;

    public RestaurantDataSourceFactory(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;

        mutableLiveData = new MutableLiveData<>();

    }

    @NonNull
    @Override
    public DataSource create() {
        restaurantDataSource = new RestaurantDataSource(restaurantService);
        mutableLiveData.postValue(restaurantDataSource);
        return restaurantDataSource;
    }

    public MutableLiveData<RestaurantDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
