package io.mountblue.zomato.data.remote;

import io.mountblue.zomato.data.RepoRestaurantResult;

public interface DataSource {

    RepoRestaurantResult loadRestaurants();


}
