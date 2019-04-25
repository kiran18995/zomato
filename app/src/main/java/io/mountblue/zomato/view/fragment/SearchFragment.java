package io.mountblue.zomato.view.fragment;

import android.app.Service;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mountblue.zomato.CurrentLocation;
import io.mountblue.zomato.R;
import io.mountblue.zomato.adapter.SearchAdapter;
import io.mountblue.zomato.data.remote.retrofit.ApiClient;
import io.mountblue.zomato.data.remote.retrofit.RestaurantService;
import io.mountblue.zomato.module.Restaurant;
import io.mountblue.zomato.module.RestaurantResponse;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";

    @BindView(R.id.restaurantList_fragment)
    RecyclerView restaurantRecyclerView;
    @BindView(R.id.search_on_fragment)
    EditText typedText;
    @BindView(R.id.search_progress_bar)
    ProgressBar searchProgressBar;
    @BindView(R.id.search_close)
    ImageView searchClose;


    List<Restaurant> restaurantList;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        restaurantList = new ArrayList<>();
        CurrentLocation currentLocation = new CurrentLocation(getContext());
        double latitude = currentLocation.getCurrentLatitude();
        double longitude = currentLocation.getCurrentLongitude();
        Log.e(TAG, "onCreateView: "+latitude+"  "+longitude);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        restaurantRecyclerView.setLayoutManager(layoutManager);
        restaurantRecyclerView.smoothScrollToPosition(1);

        typedText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                searchClose.setVisibility(View.GONE);
                if (count > 0) {
                    searchProgressBar.setVisibility(View.VISIBLE);
                    Observable<RestaurantResponse> apiService = ApiClient.getRetrofitInstance().create(RestaurantService.class)
                            .getSearch(1, latitude, longitude, s)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                    apiService.subscribe(new Observer<RestaurantResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(RestaurantResponse restaurantResponse) {
                            Log.e(TAG, "onNext: " + restaurantResponse.getRestaurants().size());
                            restaurantList.clear();
                            restaurantList=new ArrayList<>();
                            restaurantList.addAll(restaurantResponse.getRestaurants());
                            SearchAdapter  searchAdapter = new SearchAdapter(getContext());
                            searchAdapter.setRestaurantList(restaurantList);
                            restaurantRecyclerView.setAdapter(searchAdapter);
                            searchAdapter.notifyDataSetChanged();
                            searchProgressBar.setVisibility(View.GONE);
                            searchClose.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "onError: " + e.getStackTrace());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

                }

            }
        });
        typedText.requestFocus();
        searchClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typedText.setText("");
                typedText.clearFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
        return view;
    }



}
