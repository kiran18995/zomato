package io.mountblue.zomato.data.remote.retrofit;

import java.util.List;

import io.mountblue.zomato.module.RestaurantResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface RestaurantService {

    @Headers("user-key: 90bf4334ba69bcdd538c6d1958839c3f")
    @GET("search")
    Call<RestaurantResponse> getRestaurants(@Query("start") int start);
}
