package io.mountblue.zomato.data.remote.pagination;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import io.mountblue.zomato.data.remote.retrofit.RestaurantService;
import io.mountblue.zomato.module.Restaurant;
import io.mountblue.zomato.module.RestaurantResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantDataSource extends PageKeyedDataSource<Integer, Restaurant> {

    private static final int FIRST_PAGE = 1;

    private static final String TAG = "RestaurantPageKeyedData";

    private final RestaurantService restaurantService;


    public RestaurantDataSource(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Restaurant> callback) {

        restaurantService.getRestaurants(FIRST_PAGE).enqueue(new Callback<RestaurantResponse>() {
            @Override
            public void onResponse(Call<RestaurantResponse> call, Response<RestaurantResponse> response) {
                if (response.isSuccessful()) {
                    callback.onResult(response.body().getRestaurants(), null, FIRST_PAGE + 1);
                } else {
                    Log.e(TAG, "onResponse: error");
                }
            }

            @Override
            public void onFailure(Call<RestaurantResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Restaurant> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Restaurant> callback) {
        restaurantService.getRestaurants((params.key + 20)).enqueue(new Callback<RestaurantResponse>() {
            @Override
            public void onResponse(Call<RestaurantResponse> call, Response<RestaurantResponse> response) {
                if (response.isSuccessful()) {
                    callback.onResult(response.body().getRestaurants(), (params.key + 20));
                    Log.e(TAG, "onResponse: " + (params.key + 20));
                } else {
                    Log.e(TAG, "onResponse: error");
                }
            }

            @Override
            public void onFailure(Call<RestaurantResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
