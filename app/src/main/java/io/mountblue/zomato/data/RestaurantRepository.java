package io.mountblue.zomato.data;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.mountblue.zomato.data.remote.retrofit.ApiClient;
import io.mountblue.zomato.data.remote.retrofit.RestaurantService;
import io.mountblue.zomato.module.gooutmodule.Collection;
import io.mountblue.zomato.module.gooutmodule.Collections;
import io.mountblue.zomato.module.reviewmodule.Review;
import io.mountblue.zomato.module.reviewmodule.UserReview;
import io.mountblue.zomato.util.NetworkState;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RestaurantRepository {
    private static final String TAG = "RestaurantRepository";
    private MutableLiveData<List<Collection>> mutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<UserReview>> userReviewLiveData = new MutableLiveData<>();
    private MutableLiveData<NetworkState> networkState = new MutableLiveData<>();

    public MutableLiveData<List<Collection>> getMutableLiveData() {
        networkState.postValue(NetworkState.LOADING);
        Observable<Collections> apiService = ApiClient.getRetrofitInstanceByGit().create(RestaurantService.class)
                .getCollections()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        apiService.subscribe(new Observer<Collections>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Collections collections) {
                Log.e(TAG, "onResponse: " + collections.getCollections().size());
                mutableLiveData.setValue(collections.getCollections());
                networkState.postValue(NetworkState.LOADED);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onResponse: Error " + e.getMessage());
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, e.getMessage()));
            }

            @Override
            public void onComplete() {

            }
        });

        return mutableLiveData;
    }

    public MutableLiveData<List<UserReview>> getUserReviewLiveData(int id) {
        networkState.postValue(NetworkState.LOADING);
        RestaurantService restaurantService = ApiClient.getRetrofitInstance().create(RestaurantService.class);

        Observable<Review> userReviewObservable = restaurantService
                .getReview(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        userReviewObservable.subscribe(new Observer<Review>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Review review) {
                Log.e(TAG, "onResponse: " + review.getUserReviews().size());
                userReviewLiveData.setValue(review.getUserReviews());
                networkState.postValue(NetworkState.LOADED);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "reviewRespons: Error " + e.getMessage());
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, e.getMessage()));
            }

            @Override
            public void onComplete() {

            }
        });

        return userReviewLiveData;
    }

    public MutableLiveData<NetworkState> stateMutableLiveData() {
        return networkState;
    }
}
