package io.mountblue.zomato.view;


import android.content.Context;

import io.mountblue.zomato.data.RestaurantRepository;
import io.mountblue.zomato.data.remote.RestaurantRemoteDataSource;
import io.mountblue.zomato.data.remote.retrofit.ApiClient;
import io.mountblue.zomato.data.remote.retrofit.RestaurantService;

public class Injection {


    public static RestaurantRemoteDataSource provideMoviesRemoteDataSource() {
        RestaurantService apiService = ApiClient.getInstance();
        return RestaurantRemoteDataSource.getInstance(apiService);
    }


    public static RestaurantRepository provideMovieRepository(Context context) {
        RestaurantRemoteDataSource remoteDataSource = provideMoviesRemoteDataSource();
        return RestaurantRepository.getInstance(remoteDataSource);
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        RestaurantRepository repository = provideMovieRepository(context);
        return ViewModelFactory.getInstance(repository);
    }
}
