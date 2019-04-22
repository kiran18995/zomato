package io.mountblue.zomato.data.remote.pagination;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import io.mountblue.zomato.data.remote.retrofit.RestaurantService;
import io.mountblue.zomato.module.Restaurant;

public class RestaurantPageKeyedDataSourceFactory extends DataSource.Factory<Integer, Restaurant> {

    public MutableLiveData<RestaurantPageKeyedDataSource> sourceLiveData = new MutableLiveData<>();

    private final RestaurantService restaurantService;

    public RestaurantPageKeyedDataSourceFactory(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Override
    public DataSource<Integer, Restaurant> create() {
        RestaurantPageKeyedDataSource dataSource = new RestaurantPageKeyedDataSource(restaurantService);
        sourceLiveData.postValue(dataSource);
        return dataSource;
    }
}
