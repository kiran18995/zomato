package io.mountblue.zomato.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mountblue.zomato.CurrentLocation;
import io.mountblue.zomato.MainActivity;
import io.mountblue.zomato.R;
import io.mountblue.zomato.adapter.RestaurantAdapter;
import io.mountblue.zomato.data.remote.RestaurantViewModel;
import io.mountblue.zomato.module.Restaurant;
import io.mountblue.zomato.util.NetworkState;
import io.mountblue.zomato.view.activity.SearchActivity;

public class OrderFragment extends Fragment {

    @BindView(R.id.restaurantList)
    RecyclerView restaurantRecyclerView;
    @BindView(R.id.search_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerFrameLayout;
    @BindView(R.id.pull_to_refresh)
    SwipeRefreshLayout pullToRefresh;

    private PagedList<Restaurant> restaurantPagedList;
    private RestaurantViewModel restaurantViewModel;
    RestaurantAdapter restaurantAdapter;

    public OrderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        restaurantRecyclerView.setLayoutManager(layoutManager);
        restaurantRecyclerView.smoothScrollToPosition(1);
        restaurantViewModel = new RestaurantViewModel(getContext());

        restaurantViewModel.getPagedList().observe(this, new Observer<PagedList<Restaurant>>() {
            @Override
            public void onChanged(PagedList<Restaurant> restaurants) {
                restaurantPagedList = restaurants;
                setRecyclerView();
            }
        });

        restaurantViewModel.getNetworkState().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(NetworkState networkState) {
                if (networkState == NetworkState.LOADING) {
                } else {
                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRecyclerView();
                pullToRefresh.setRefreshing(false);
            }
        });

        return view;
    }

    private void setRecyclerView() {
        restaurantAdapter = new RestaurantAdapter(getContext());
        restaurantAdapter.submitList(restaurantPagedList);
        restaurantRecyclerView.setAdapter(restaurantAdapter);
        restaurantAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmerAnimation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onPause() {
        shimmerFrameLayout.stopShimmerAnimation();
        super.onPause();
    }
}
