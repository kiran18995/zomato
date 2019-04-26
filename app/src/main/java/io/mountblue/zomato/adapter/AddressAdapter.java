package io.mountblue.zomato.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mountblue.zomato.R;
import io.mountblue.zomato.module.suggestion.LocationSuggestion;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private static final String TAG = "AddressAdapter";
    private List<LocationSuggestion> locationSuggestions;
    private Context context;

    public List<LocationSuggestion> getLocationSuggestions() {
        return locationSuggestions;
    }

    public void setLocationSuggestions(List<LocationSuggestion> locationSuggestions) {
        this.locationSuggestions = locationSuggestions;
    }

    public AddressAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AddressAdapter.AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.location_suggestions_cell, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.AddressViewHolder holder, int position) {
        holder.suggestedAddress.setText(locationSuggestions.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return locationSuggestions.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.suggested_address)
        TextView suggestedAddress;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
