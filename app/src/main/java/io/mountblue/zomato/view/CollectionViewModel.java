package io.mountblue.zomato.view;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import io.mountblue.zomato.data.RestaurantRepository;
import io.mountblue.zomato.module.gooutmodule.Collection;
import io.mountblue.zomato.module.reviewmodule.Review;
import io.mountblue.zomato.module.reviewmodule.UserReview;
import io.mountblue.zomato.util.NetworkState;

public class CollectionViewModel extends ViewModel {
    private RestaurantRepository restaurantRepository;


    @Inject
    public CollectionViewModel() {
        Log.e("work", "work");
        restaurantRepository = new RestaurantRepository();
    }

    public LiveData<List<Collection>> getAllCollection() {
        return restaurantRepository.getMutableLiveData();
    }

    public LiveData<List<UserReview>> getAllReview(int id) {
        return restaurantRepository.getUserReviewLiveData(id);
    }

    public LiveData<NetworkState> getNetwork() {
        return restaurantRepository.stateMutableLiveData();
    }
}
