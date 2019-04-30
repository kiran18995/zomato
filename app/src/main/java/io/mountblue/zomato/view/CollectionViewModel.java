package io.mountblue.zomato.view;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import io.mountblue.zomato.data.RestaurantRepository;
import io.mountblue.zomato.module.Restaurant_;
import io.mountblue.zomato.module.gooutmodule.Collection;
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

    public LiveData<List<Restaurant_>> getRestaurant(Context context) {
        return restaurantRepository.getRestaurantMutableLiveData(context);
    }

    public void saveToBookmark(Restaurant_ restaurant, Context context) {
        restaurantRepository.saveMovie(restaurant, context);
    }

    public void removeBookmark(Context context, String id) {
        restaurantRepository.removeBookmark(context, id);
    }

    public LiveData<List<Restaurant_>> getRestaurant(Context context, String id) {
        return restaurantRepository.getRestaurantMutableLiveData(context, id);
    }

}
