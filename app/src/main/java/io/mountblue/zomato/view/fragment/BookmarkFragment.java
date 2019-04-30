package io.mountblue.zomato.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mountblue.zomato.R;
import io.mountblue.zomato.adapter.BookmarkAdapter;
import io.mountblue.zomato.adapter.CollectionAdapter;
import io.mountblue.zomato.adapter.SearchAdapter;
import io.mountblue.zomato.module.Restaurant_;
import io.mountblue.zomato.module.gooutmodule.Collection;
import io.mountblue.zomato.view.CollectionViewModel;
import io.mountblue.zomato.view.activity.BookmarkActivity;
import io.mountblue.zomato.viewmodel.ViewModelProviderFactory;

public class BookmarkFragment extends Fragment {

    @BindView(R.id.restaurantList)
    RecyclerView recyclerView;

    private CollectionViewModel collectionViewModel;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    private List<Restaurant_> restaurantList;

    public BookmarkFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        ButterKnife.bind(this, view);
        restaurantList = new ArrayList<>();
        collectionViewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(CollectionViewModel.class);
        collectionViewModel.getRestaurant(getContext()).observe(this, new Observer<List<Restaurant_>>() {
            @Override
            public void onChanged(List<Restaurant_> restaurant_s) {
                restaurantList = restaurant_s;
                getBookmark();
            }
        });
        return view;
    }


    private void getBookmark() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        BookmarkAdapter bookmarkAdapter = new BookmarkAdapter(getContext());
        bookmarkAdapter.setRestaurant(restaurantList);
        recyclerView.smoothScrollToPosition(1);
        recyclerView.setAdapter(bookmarkAdapter);
        bookmarkAdapter.notifyDataSetChanged();
    }

}
