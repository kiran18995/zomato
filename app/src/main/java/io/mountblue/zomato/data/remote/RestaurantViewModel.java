package io.mountblue.zomato.data.remote;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import io.mountblue.zomato.data.remote.pagination.RestaurantDataSource;
import io.mountblue.zomato.data.remote.pagination.RestaurantDataSourceFactory;
import io.mountblue.zomato.data.remote.retrofit.ApiClient;
import io.mountblue.zomato.data.remote.retrofit.RestaurantService;
import io.mountblue.zomato.module.Restaurant;
import io.mountblue.zomato.util.NetworkState;

public class RestaurantViewModel extends ViewModel {

    private LiveData<PagedList<Restaurant>> pagedList;
    private LiveData<RestaurantDataSource> restaurantDataSourceLiveData;
    private LiveData<NetworkState> networkState;
    private Executor executor;

    @Inject
    public RestaurantViewModel(Context context) {
        RestaurantService apiService = ApiClient.getRetrofitInstance().create(RestaurantService.class);

        RestaurantDataSourceFactory factory = new RestaurantDataSourceFactory(apiService, context);

        networkState = Transformations.switchMap(factory.getMutableLiveData(),
                dataSource -> dataSource.getNetworkState());

        restaurantDataSourceLiveData = factory.getMutableLiveData();

        PagedList.Config config = (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                .setPageSize(20).build();

        executor = Executors.newFixedThreadPool(5);

        pagedList = (new LivePagedListBuilder<Integer, Restaurant>(factory, config))
                .setFetchExecutor(executor)
                .build();

    }

    public LiveData<PagedList<Restaurant>> getPagedList() {
        return pagedList;
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }


}
