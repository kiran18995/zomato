package io.mountblue.zomato.view.fragment;


import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mountblue.zomato.R;


public class GoldFragment extends Fragment {
    @BindView(R.id.scratch_text)
    TextView scratchText;
    public GoldFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gold, container, false);
        ButterKnife.bind(this,view);

        scratchText.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        return view;
    }

}
