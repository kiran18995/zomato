package io.mountblue.zomato.data.remote.pagination;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import io.mountblue.zomato.data.remote.retrofit.RestaurantService;

public class RestaurantDataSourceFactory extends DataSource.Factory {

    private RestaurantDataSource restaurantDataSource;
    private Context context;
    private RestaurantService restaurantService;
    private MutableLiveData<RestaurantDataSource> mutableLiveData;

    public RestaurantDataSourceFactory(RestaurantService restaurantService,Context context) {
        this.restaurantService = restaurantService;
        this.context=context;
        mutableLiveData = new MutableLiveData<>();

    }

    @NonNull
    @Override
    public DataSource create() {
        restaurantDataSource = new RestaurantDataSource(restaurantService,context);
        mutableLiveData.postValue(restaurantDataSource);
        return restaurantDataSource;
    }

    public MutableLiveData<RestaurantDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
