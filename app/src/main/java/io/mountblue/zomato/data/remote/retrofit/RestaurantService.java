package io.mountblue.zomato.data.remote.retrofit;

import java.util.List;

import io.mountblue.zomato.module.Restaurant;
import io.mountblue.zomato.module.RestaurantResponse;
import io.mountblue.zomato.module.gooutmodule.Collections;
import io.mountblue.zomato.module.suggestion.LocationSuggestions;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface RestaurantService {


    @Headers("user-key: ef8c51221cb43b81b160316fb66a3c82")
    @GET("search")
    Observable<RestaurantResponse> getRestaurants(@Query("start") int start);

    @Headers("user-key: ef8c51221cb43b81b160316fb66a3c82")
    @GET("search")
    Observable<RestaurantResponse> getSearch(@Query("start") int start,
                                             @Query("lat") double lat,
                                             @Query("lon") double lon,
                                             @Query("q") CharSequence q);

    @Headers("user-key: ef8c51221cb43b81b160316fb66a3c82")
    @GET("search")
    Single<RestaurantResponse> getData(@Query("start") int start,
                                       @Query("lat") double lat,
                                       @Query("lon") double lon,
                                       @Query("q") CharSequence q);

    @Headers("user-key: ef8c51221cb43b81b160316fb66a3c82")
    @GET("locations")
    Single<LocationSuggestions> getAddressSuggestions(@Query("count") int count,
                                       @Query("query") CharSequence q);



  /*  @GET("restaurant.json")
    Call<RestaurantResponse> getRestaurants();*/

    @GET("collection.json")
    Observable<Collections> getCollections();
}
