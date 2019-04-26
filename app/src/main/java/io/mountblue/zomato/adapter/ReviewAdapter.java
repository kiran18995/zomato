package io.mountblue.zomato.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.mountblue.zomato.R;
import io.mountblue.zomato.module.reviewmodule.UserReview;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private Context context;
    private List<UserReview> userReviewList;

    public ReviewAdapter(Context context, List<UserReview> userReviews) {
        this.context = context;
        this.userReviewList = userReviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_row, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        if (userReviewList.size() > 0) {
            Picasso.with(context).load(userReviewList.get(position).getReview().getUser().getProfileImage())
                    .placeholder(R.drawable.user)
                    .error(R.drawable.user)
                    .fit()
                    .centerCrop()
                    .into(holder.personPhoto);
            holder.personName.setText(userReviewList.get(position).getReview().getUser().getName());
            holder.personRating.setText(userReviewList.get(position).getReview().getRating()+"★");
            holder.personReview.setText(userReviewList.get(position).getReview().getReviewText());
        }else {

        }

    }

    @Override
    public int getItemCount() {
        if (userReviewList.size() > 0) {
            return userReviewList.size();
        } else {
            return 0;
        }
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.person_photo)
        CircleImageView personPhoto;
        @BindView(R.id.person_name)
        TextView personName;
        @BindView(R.id.person_rating)
        TextView personRating;
        @BindView(R.id.person_review)
        TextView personReview;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
