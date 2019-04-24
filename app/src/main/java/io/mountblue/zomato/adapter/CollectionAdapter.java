package io.mountblue.zomato.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mountblue.zomato.R;
import io.mountblue.zomato.RoundedTransformation;
import io.mountblue.zomato.module.gooutmodule.Collection;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder> {

    List<Collection> collectionList;
    Context context;

    public CollectionAdapter(Context context) {
        this.context = context;
    }

    public List<Collection> getCollectionList() {
        return collectionList;
    }

    public void setCollectionList(List<Collection> collectionList) {
        this.collectionList = collectionList;
    }

    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.go_out_cell, parent, false);
        return new CollectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, int position) {
        holder.heading.setText(collectionList.get(position).getCollection().getTitle());
        holder.information.setText(collectionList.get(position).getCollection().getDescription());
        holder.places.setText(collectionList.get(position).getCollection().getResCount().toString() + " Places");

        if (collectionList.get(position).getCollection().getImageUrl() != null) {
            Picasso.with(context).load(collectionList.get(position).getCollection().getImageUrl())
                    .placeholder(R.drawable.food_placeholder)
                    .error(R.drawable.food_placeholder)
                    .transform(new RoundedTransformation(10, 0))
                    .fit()
                    .centerCrop()
                    .into(holder.imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.imageView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {
                            holder.imageView.setVisibility(View.GONE);
                        }
                    });

        }
    }

    @Override
    public int getItemCount() {
        if (collectionList.size() > 0) {
            return collectionList.size();
        } else {
            return 0;
        }
    }

    public class CollectionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.collection_image)
        ImageView imageView;
        @BindView(R.id.text_heading)
        TextView heading;
        @BindView(R.id.text_information)
        TextView information;
        @BindView(R.id.text_places)
        TextView places;

        public CollectionViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
