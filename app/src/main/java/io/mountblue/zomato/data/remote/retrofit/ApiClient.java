package io.mountblue.zomato.data.remote.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit;
    private static RestaurantService sInstance;
    private static final Object sLock = new Object();
    //private static final String BASE_URL = "https://developers.zomato.com/api/v2.1/";
    private static final String BASE_URL = "https://gitlab.com/saurav053/zomato_api/raw/master/";


    public static RestaurantService getInstance() {
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = getRetrofitInstance().create(RestaurantService.class);
            }
            return sInstance;
        }
    }


    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
