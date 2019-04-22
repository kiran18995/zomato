package io.mountblue.zomato.view;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import io.mountblue.zomato.data.RepoRestaurantResult;
import io.mountblue.zomato.data.RestaurantRepository;
import io.mountblue.zomato.module.Restaurant;

public class RestaurantViewModel extends ViewModel {

    private LiveData<RepoRestaurantResult> repoMoviesResult;

    private LiveData<PagedList<Restaurant>> pagedList;


    public RestaurantViewModel(final RestaurantRepository restaurantRepository) {
        repoMoviesResult = Transformations.map(repoMoviesResult, new Function<RepoRestaurantResult, RepoRestaurantResult>() {
            @Override
            public RepoRestaurantResult apply(RepoRestaurantResult input) {
                return restaurantRepository.loadRestaurants();
            }
        });
        pagedList = Transformations.switchMap(repoMoviesResult, new Function<RepoRestaurantResult, LiveData<PagedList<Restaurant>>>() {
            @Override
            public LiveData<PagedList<Restaurant>> apply(RepoRestaurantResult input) {
                return input.data;
            }
        });
    }

    public LiveData<PagedList<Restaurant>> getPagedList() {
        return pagedList;
    }

}
