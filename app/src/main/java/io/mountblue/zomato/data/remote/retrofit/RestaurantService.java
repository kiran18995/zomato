package io.mountblue.zomato.data.remote.retrofit;

import java.util.List;

import io.mountblue.zomato.module.RestaurantResponse;
import io.mountblue.zomato.module.gooutmodule.Collections;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface RestaurantService {


    @Headers("user-key: 90bf4334ba69bcdd538c6d1958839c3f")
    @GET("search")
    Call<RestaurantResponse> getRestaurants(@Query("start") int start);

  /*  @GET("restaurant.json")
    Call<RestaurantResponse> getRestaurants();*/

    @GET("collection.json")
    Call<Collections> getCollections();
}
