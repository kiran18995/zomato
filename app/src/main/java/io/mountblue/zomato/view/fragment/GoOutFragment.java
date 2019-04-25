package io.mountblue.zomato.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mountblue.zomato.R;
import io.mountblue.zomato.adapter.CollectionAdapter;
import io.mountblue.zomato.module.gooutmodule.Collection;

public class GoOutFragment extends Fragment {

    private static final String TAG = "GoOutFragment";

    @BindView(R.id.recycler_go_out_list)
    RecyclerView goOutRecyclerView;

    private CollectionViewModel collectionViewModel;

    public GoOutFragment() {
    }

    private List<Collection> collectionList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_go_out, container, false);
        ButterKnife.bind(this, view);
        collectionList = new ArrayList<>();
        collectionViewModel = new CollectionViewModel();
        collectionViewModel.getAllCollection().observe(this, new Observer<List<Collection>>() {
            @Override
            public void onChanged(List<Collection> collections) {
                collectionList = collections;
                getCollection();
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
        goOutRecyclerView.setAdapter(collectionAdapter);
        collectionAdapter.notifyDataSetChanged();
    }

}
