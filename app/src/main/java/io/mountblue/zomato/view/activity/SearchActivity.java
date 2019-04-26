package io.mountblue.zomato.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mountblue.zomato.R;
import io.mountblue.zomato.view.fragment.SearchFragment;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.delivery_address)
    TextView deliveryAddress;
    @BindView(R.id.address_heading)
    TextView addressHeading;
    @BindView(R.id.layout_location)
    LinearLayout layoutLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        deliveryAddress.setText(intent.getStringExtra("deliveryAddress"));
        addressHeading.setText(intent.getStringExtra("addressHeading"));

        Fragment fragment = new SearchFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.search_view, fragment)
                .commit();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        layoutLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addressIntent = new Intent(SearchActivity.this, AddressActivity.class);
                startActivity(addressIntent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
