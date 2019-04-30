package io.mountblue.zomato.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mountblue.zomato.MainActivity;
import io.mountblue.zomato.R;
import io.mountblue.zomato.adapter.CollectionAdapter;
import io.mountblue.zomato.module.Restaurant_;
import io.mountblue.zomato.module.gooutmodule.Collection;
import io.mountblue.zomato.util.NetworkState;
import io.mountblue.zomato.view.CollectionViewModel;
import io.mountblue.zomato.viewmodel.ViewModelProviderFactory;

public class GoOutFragment extends Fragment {

    private static final String TAG = "GoOutFragment";

    @BindView(R.id.recycler_go_out_list)
    RecyclerView goOutRecyclerView;
    @BindView(R.id.search_progress_bar)
    ProgressBar progressBar;

    private CollectionViewModel collectionViewModel;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;


    public GoOutFragment() {
    }

    private List<Collection> collectionList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_go_out, container, false);
        ButterKnife.bind(this, view);
        collectionList = new ArrayList<>();
            collectionViewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(CollectionViewModel.class);
            collectionViewModel.getAllCollection().observe(this, new Observer<List<Collection>>() {
            @Override
            public void onChanged(List<Collection> collections) {
                collectionList = collections;
                getCollection();
            }
        });
        collectionViewModel.getNetwork().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(NetworkState networkState) {
                if (networkState == NetworkState.LOADING) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        collectionViewModel.getRestaurant(getContext()).observe(this, new Observer<List<Restaurant_>>() {
            @Override
            public void onChanged(List<Restaurant_> restaurant_s) {
                Log.e(TAG, "onChanged: " + restaurant_s.size());
            }
        });

        return view;
    }

    private void getCollection() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        goOutRecyclerView.setHasFixedSize(true);
        goOutRecyclerView.setLayoutManager(layoutManager);
        CollectionAdapter collectionAdapter = new CollectionAdapter(getContext());
        collectionAdapter.setCollectionList(collectionList);
        goOutRecyclerView.smoothScrollToPosition(1);
        goOutRecyclerView.setAdapter(collectionAdapter);
        collectionAdapter.notifyDataSetChanged();
    }

}
