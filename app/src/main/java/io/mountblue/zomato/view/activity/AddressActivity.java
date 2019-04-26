package io.mountblue.zomato.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mountblue.zomato.R;
import io.mountblue.zomato.adapter.AddressAdapter;
import io.mountblue.zomato.data.remote.retrofit.ApiClient;
import io.mountblue.zomato.data.remote.retrofit.RestaurantService;
import io.mountblue.zomato.module.RestaurantResponse;
import io.mountblue.zomato.module.suggestion.LocationSuggestion;
import io.mountblue.zomato.module.suggestion.LocationSuggestions;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class AddressActivity extends AppCompatActivity {

    @BindView(R.id.location_query)
    EditText locationQuery;
    @BindView(R.id.location_suggestions)
    RecyclerView locationSuggestionsRecyclerView;
    @BindView(R.id.use_current_location)
    LinearLayout useCurrentLocation;
    @BindView(R.id.back)
    ImageView back;

    private static final String TAG = "AddressActivity";
    private CompositeDisposable disposable = new CompositeDisposable();
    private PublishSubject<String> publishSubject = PublishSubject.create();
    private RestaurantService apiService;
    List<LocationSuggestion> locationSuggestionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);

        locationSuggestionList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        locationSuggestionsRecyclerView.setLayoutManager(layoutManager);
        locationSuggestionsRecyclerView.smoothScrollToPosition(1);
        apiService = ApiClient.getRetrofitInstance().create(RestaurantService.class);
        DisposableObserver<LocationSuggestions> observer = getSearchObserver();

        disposable.add(publishSubject.debounce(300, TimeUnit.MICROSECONDS)
                .distinctUntilChanged()
                .switchMapSingle(new Function<String, Single<LocationSuggestions>>() {
                    @Override
                    public Single<LocationSuggestions> apply(String s) throws Exception {
                        Log.e(TAG, "apply: "+s);
                        return apiService.getAddressSuggestions(10,s)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .subscribeWith(observer));

        disposable.add(RxTextView.textChangeEvents(locationQuery)
                .skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(searchContactsTextWatcher()));

        disposable.add(observer);

        publishSubject.onNext("");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private DisposableObserver<LocationSuggestions> getSearchObserver() {
        return new DisposableObserver<LocationSuggestions>() {
            @Override
            public void onNext(LocationSuggestions locationSuggestions) {
                Log.e(TAG, "onNext: "+locationSuggestions.getLocationSuggestions().size());
                locationSuggestionList.clear();
                locationSuggestionList = new ArrayList<>();
                locationSuggestionList.addAll(locationSuggestions.getLocationSuggestions());
                AddressAdapter addressAdapter = new AddressAdapter(getApplicationContext());
                addressAdapter.setLocationSuggestions(locationSuggestionList);
                locationSuggestionsRecyclerView.setAdapter(addressAdapter);
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
                Log.d(TAG, "Location query: " + textViewTextChangeEvent.text());
                publishSubject.onNext(textViewTextChangeEvent.text().toString());
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
