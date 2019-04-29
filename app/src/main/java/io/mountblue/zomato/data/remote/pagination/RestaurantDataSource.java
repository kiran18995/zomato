package io.mountblue.zomato.data.remote.pagination;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import io.mountblue.zomato.CurrentLocation;
import io.mountblue.zomato.data.remote.retrofit.RestaurantService;
import io.mountblue.zomato.module.Location;
import io.mountblue.zomato.module.Restaurant;
import io.mountblue.zomato.module.RestaurantResponse;
import io.mountblue.zomato.util.SharedPrefrenceAddress;
import io.mountblue.zomato.util.NetworkState;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class RestaurantDataSource extends PageKeyedDataSource<Integer, Restaurant> {

    private static final int FIRST_PAGE = 1;

    private static final String TAG = "RestaurantPageKeyedData";

    private final RestaurantService restaurantService;
    private Context context;

    private SharedPrefrenceAddress sharedPrefrenceAddress;

    private MutableLiveData<NetworkState> networkState = new MutableLiveData<>();


    public RestaurantDataSource(RestaurantService restaurantService, Context context) {
        this.restaurantService = restaurantService;
        this.context = context;
        networkState.postValue(NetworkState.LOADING);
    }

    public MutableLiveData getNetworkState() {
        return networkState;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Restaurant> callback) {
        networkState.postValue(NetworkState.LOADING);
        Observable<RestaurantResponse> restaurantObservable = restaurantService.getRestaurant(FIRST_PAGE, currentLocation.getCurrentLatitude(), currentLocation.getCurrentLongitude())
        sharedPrefrenceAddress = new SharedPrefrenceAddress(context);
        double latitude = Double.parseDouble(sharedPrefrenceAddress.getDefaultAddress("addressLatitude"));
        double longitude = Double.parseDouble(sharedPrefrenceAddress.getDefaultAddress("addressLongitude"));
        Observable<RestaurantResponse> restaurantObservable = restaurantService.getRestaurant(FIRST_PAGE, latitude, longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        restaurantObservable.subscribe(new Observer<RestaurantResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(RestaurantResponse restaurantResponse) {
                callback.onResult(restaurantResponse.getRestaurants(), null, FIRST_PAGE + 1);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onResponse: error" + e.getMessage());
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, e.getMessage()));

            }

            @Override
            public void onComplete() {
                networkState.postValue(NetworkState.LOADED);
            }
        });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Restaurant> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Restaurant> callback) {
        networkState.postValue(NetworkState.LOADING);
        Observable<RestaurantResponse> restaurantObservable = restaurantService.getRestaurant((params.key + 20), currentLocation.getCurrentLatitude(), currentLocation.getCurrentLongitude())
        sharedPrefrenceAddress = new SharedPrefrenceAddress(context);
        double latitude = Double.parseDouble(sharedPrefrenceAddress.getDefaultAddress("addressLatitude"));
        double longitude = Double.parseDouble(sharedPrefrenceAddress.getDefaultAddress("addressLongitude"));
        Observable<RestaurantResponse> restaurantObservable = restaurantService.getRestaurant((params.key + 20), latitude, longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        restaurantObservable.subscribe(new Observer<RestaurantResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(RestaurantResponse restaurantResponse) {
                callback.onResult(restaurantResponse.getRestaurants(), (params.key + 20));
                Log.e(TAG, "onResponse: load" + (params.key + 20));
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onResponse: error" + e.getMessage());
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, e.getMessage()));
            }

            @Override
            public void onComplete() {
                networkState.postValue(NetworkState.LOADED);
            }
        });

    }
}
