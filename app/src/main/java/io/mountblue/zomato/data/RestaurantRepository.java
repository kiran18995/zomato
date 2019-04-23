package io.mountblue.zomato.data;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.mountblue.zomato.data.remote.retrofit.ApiClient;
import io.mountblue.zomato.data.remote.retrofit.RestaurantService;
import io.mountblue.zomato.module.gooutmodule.Collection;
import io.mountblue.zomato.module.gooutmodule.Collections;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepository {
    private static final String TAG = "RestaurantRepository";
    private MutableLiveData<List<Collection>> mutableLiveData = new MutableLiveData<>();

    public MutableLiveData<List<Collection>> getMutableLiveData() {
        RestaurantService apiService = ApiClient.getRetrofitInstanceByGit().create(RestaurantService.class);

        apiService.getCollections().enqueue(new Callback<Collections>() {
            @Override
            public void onResponse(Call<Collections> call, Response<Collections> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e(TAG, "onResponse: " + response.body().getCollections().size());
                    mutableLiveData.setValue(response.body().getCollections());
                } else {
                    Log.e(TAG, "onResponse: Error ");
                }
            }

            @Override
            public void onFailure(Call<Collections> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        return mutableLiveData;
    }


}
