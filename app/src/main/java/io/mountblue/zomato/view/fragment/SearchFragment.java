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
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

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
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

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
    @BindView(R.id.no_result)
    LinearLayout noResultMessage;

    private CompositeDisposable disposable = new CompositeDisposable();
    private PublishSubject<String> publishSubject = PublishSubject.create();
    private RestaurantService apiService;



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


        typedText.requestFocus();
        searchClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typedText.setText("");
                typedText.clearFocus();
                searchClose.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        apiService = ApiClient.getRetrofitInstance().create(RestaurantService.class);

        DisposableObserver<RestaurantResponse> observer = getSearchObserver();

        disposable.add(publishSubject.debounce(300, TimeUnit.MICROSECONDS)
                .distinctUntilChanged()
                .switchMapSingle(new Function<String, Single<RestaurantResponse>>() {
                    @Override
                    public Single<RestaurantResponse> apply(String s) throws Exception {
                        return apiService.getData(1, latitude, longitude, s)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .subscribeWith(observer));


        disposable.add(RxTextView.textChangeEvents(typedText)
                .skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(searchContactsTextWatcher()));

        disposable.add(observer);

        publishSubject.onNext("");


        return view;
    }
    private DisposableObserver<RestaurantResponse> getSearchObserver() {
        return new DisposableObserver<RestaurantResponse>() {
            @Override
            public void onNext(RestaurantResponse restaurants) {
                Log.e(TAG, "onNext: " + restaurants.getRestaurants().size());
                if (restaurants.getRestaurants().size() > 0){
                    noResultMessage.setVisibility(View.GONE);
                }
                else {
                    noResultMessage.setVisibility(View.VISIBLE);
                }
                restaurantList.clear();
                restaurantList = new ArrayList<>();
                restaurantList.addAll(restaurants.getRestaurants());
                SearchAdapter searchAdapter = new SearchAdapter(getContext());
                searchAdapter.setRestaurantList(restaurantList);
                restaurantRecyclerView.setAdapter(searchAdapter);
                searchAdapter.notifyDataSetChanged();
                searchProgressBar.setVisibility(View.GONE);
                if (typedText.hasFocus()){
                    searchClose.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getStackTrace());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private DisposableObserver<TextViewTextChangeEvent> searchContactsTextWatcher() {
        return new DisposableObserver<TextViewTextChangeEvent>() {
            @Override
            public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                Log.d(TAG, "Search query: " + textViewTextChangeEvent.text());
                publishSubject.onNext(textViewTextChangeEvent.text().toString());
                if (typedText.hasFocus()){
                    searchProgressBar.setVisibility(View.VISIBLE);
                }
                searchClose.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        };
    }

}
