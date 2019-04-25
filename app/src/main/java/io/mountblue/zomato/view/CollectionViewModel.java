package io.mountblue.zomato.view;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import io.mountblue.zomato.data.RestaurantRepository;
import io.mountblue.zomato.module.gooutmodule.Collection;

public class CollectionViewModel extends ViewModel {
    private RestaurantRepository restaurantRepository;


    @Inject
    public CollectionViewModel() {
        Log.e("work","work");
        restaurantRepository = new RestaurantRepository();
    }

    public LiveData<List<Collection>> getAllCollection() {
        return restaurantRepository.getMutableLiveData();
    }
}
