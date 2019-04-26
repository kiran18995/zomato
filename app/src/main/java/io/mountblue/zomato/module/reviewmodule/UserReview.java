package io.mountblue.zomato.module.reviewmodule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserReview {

    @SerializedName("review")
    @Expose
    public Review_ review;

    public UserReview(Review_ review) {
        this.review = review;
    }

    public Review_ getReview() {
        return review;
    }

    public void setReview(Review_ review) {
        this.review = review;
    }
}
