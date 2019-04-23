package io.mountblue.zomato.view.gooutfragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mountblue.zomato.R;
import io.mountblue.zomato.adapter.CollectionAdapter;
import io.mountblue.zomato.data.remote.retrofit.ApiClient;
import io.mountblue.zomato.data.remote.retrofit.RestaurantService;
import io.mountblue.zomato.module.gooutmodule.Collections;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoOutFragment extends Fragment {

    private static final String TAG = "GoOutFragment";

    @BindView(R.id.recycler_go_out_list)
    RecyclerView goOutRecyclerView;

    public GoOutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_go_out, container, false);
        ButterKnife.bind(this, view);
        getCollection();
        return view;
    }

    private void getCollection() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        goOutRecyclerView.setHasFixedSize(true);
        goOutRecyclerView.setLayoutManager(layoutManager);

        RestaurantService apiService =
                ApiClient.getRetrofitInstance().create(RestaurantService.class);

        apiService.getCollections().enqueue(new Callback<Collections>() {
            @Override
            public void onResponse(Call<Collections> call, Response<Collections> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Log.e(TAG, "onResponse: " + response.body().getCollections().size());
                    CollectionAdapter collectionAdapter = new CollectionAdapter(getContext());
                    collectionAdapter.setCollectionList(response.body().getCollections());
                    goOutRecyclerView.setAdapter(collectionAdapter);
                } else {
                    Log.e(TAG, "onResponse: Error ");
                }
            }

            @Override
            public void onFailure(Call<Collections> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

}
