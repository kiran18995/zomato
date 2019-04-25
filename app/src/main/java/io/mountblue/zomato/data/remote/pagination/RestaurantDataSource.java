package io.mountblue.zomato.data.remote.pagination;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import io.mountblue.zomato.CurrentLocation;
import io.mountblue.zomato.data.remote.retrofit.RestaurantService;
import io.mountblue.zomato.module.Location;
import io.mountblue.zomato.module.Restaurant;
import io.mountblue.zomato.module.RestaurantResponse;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class RestaurantDataSource extends PageKeyedDataSource<Integer, Restaurant> {

    private static final int FIRST_PAGE = 1;

    private static final double lat = 12.9040821;

    private static final double lon = 77.5990793;


    private static final String TAG = "RestaurantPageKeyedData";

    private final RestaurantService restaurantService;


    public RestaurantDataSource(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Restaurant> callback) {

        Observable<RestaurantResponse> restaurantObservable = restaurantService.getSearch(FIRST_PAGE,lat,lon," ")
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
            }

            @Override
            public void onComplete() {
            }
        });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Restaurant> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Restaurant> callback) {

        Observable<RestaurantResponse> restaurantObservable = restaurantService.getSearch((params.key + 20),lat,lon," ")
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
            }

            @Override
            public void onComplete() {
            }
        });

    }
}
