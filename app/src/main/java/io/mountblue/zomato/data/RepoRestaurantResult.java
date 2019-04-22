package io.mountblue.zomato.data;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import io.mountblue.zomato.data.remote.pagination.RestaurantPageKeyedDataSource;
import io.mountblue.zomato.module.Restaurant;

public class RepoRestaurantResult {
    public LiveData<PagedList<Restaurant>> data;
    public MutableLiveData<RestaurantPageKeyedDataSource> sourceLiveData;

    public RepoRestaurantResult(LiveData<PagedList<Restaurant>> data,
                                MutableLiveData<RestaurantPageKeyedDataSource> sourceLiveData) {
        this.data = data;
        this.sourceLiveData = sourceLiveData;
    }
}
