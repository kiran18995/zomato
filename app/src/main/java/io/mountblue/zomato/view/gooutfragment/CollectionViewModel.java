package io.mountblue.zomato.view.gooutfragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.mountblue.zomato.data.RestaurantRepository;
import io.mountblue.zomato.module.gooutmodule.Collection;

public class CollectionViewModel extends ViewModel {
    private RestaurantRepository restaurantRepository;

    public CollectionViewModel() {
        restaurantRepository = new RestaurantRepository();
    }

    public LiveData<List<Collection>> getAllCollection() {
        return restaurantRepository.getMutableLiveData();
    }
}
