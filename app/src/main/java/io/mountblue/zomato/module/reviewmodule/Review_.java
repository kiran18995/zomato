package io.mountblue.zomato.module.reviewmodule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review_ {

    @SerializedName("rating")
    @Expose
    public Integer rating;
    @SerializedName("review_text")
    @Expose
    public String reviewText;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("rating_color")
    @Expose
    public String ratingColor;
    @SerializedName("review_time_friendly")
    @Expose
    public String reviewTimeFriendly;
    @SerializedName("rating_text")
    @Expose
    public String ratingText;
    @SerializedName("timestamp")
    @Expose
    public Integer timestamp;
    @SerializedName("likes")
    @Expose
    public Integer likes;
    @SerializedName("user")
    @Expose
    public User user;
    @SerializedName("comments_count")
    @Expose
    public Integer commentsCount;

    public Review_(Integer rating, String reviewText, Integer id, String ratingColor, String reviewTimeFriendly, String ratingText, Integer timestamp, Integer likes, User user, Integer commentsCount) {
        this.rating = rating;
        this.reviewText = reviewText;
        this.id = id;
        this.ratingColor = ratingColor;
        this.reviewTimeFriendly = reviewTimeFriendly;
        this.ratingText = ratingText;
        this.timestamp = timestamp;
        this.likes = likes;
        this.user = user;
        this.commentsCount = commentsCount;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRatingColor() {
        return ratingColor;
    }

    public void setRatingColor(String ratingColor) {
        this.ratingColor = ratingColor;
    }

    public String getReviewTimeFriendly() {
        return reviewTimeFriendly;
    }

    public void setReviewTimeFriendly(String reviewTimeFriendly) {
        this.reviewTimeFriendly = reviewTimeFriendly;
    }

    public String getRatingText() {
        return ratingText;
    }

    public void setRatingText(String ratingText) {
        this.ratingText = ratingText;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }
}
