package io.mountblue.zomato.module.reviewmodule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Review {
    public Review(Integer reviewsCount, Integer reviewsStart, Integer reviewsShown, List<UserReview> userReviews, String respondToReviewsViaZomatoDashboard) {
        this.reviewsCount = reviewsCount;
        this.reviewsStart = reviewsStart;
        this.reviewsShown = reviewsShown;
        this.userReviews = userReviews;
        this.respondToReviewsViaZomatoDashboard = respondToReviewsViaZomatoDashboard;
    }

    @SerializedName("reviews_count")
    @Expose
    public Integer reviewsCount;
    @SerializedName("reviews_start")
    @Expose
    public Integer reviewsStart;
    @SerializedName("reviews_shown")
    @Expose
    public Integer reviewsShown;
    @SerializedName("user_reviews")
    @Expose
    public List<UserReview> userReviews = null;
    @SerializedName("Respond to reviews via Zomato Dashboard")
    @Expose
    public String respondToReviewsViaZomatoDashboard;

    public Integer getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(Integer reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public Integer getReviewsStart() {
        return reviewsStart;
    }

    public void setReviewsStart(Integer reviewsStart) {
        this.reviewsStart = reviewsStart;
    }

    public Integer getReviewsShown() {
        return reviewsShown;
    }

    public void setReviewsShown(Integer reviewsShown) {
        this.reviewsShown = reviewsShown;
    }

    public List<UserReview> getUserReviews() {
        return userReviews;
    }

    public void setUserReviews(List<UserReview> userReviews) {
        this.userReviews = userReviews;
    }

    public String getRespondToReviewsViaZomatoDashboard() {
        return respondToReviewsViaZomatoDashboard;
    }

    public void setRespondToReviewsViaZomatoDashboard(String respondToReviewsViaZomatoDashboard) {
        this.respondToReviewsViaZomatoDashboard = respondToReviewsViaZomatoDashboard;
    }
}
