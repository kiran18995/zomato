
package io.mountblue.zomato.module;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRating implements Parcelable {

    @SerializedName("aggregate_rating")
    @Expose
    private String aggregateRating;
    @SerializedName("rating_text")
    @Expose
    private String ratingText;
    @SerializedName("rating_color")
    @Expose
    private String ratingColor;
    @SerializedName("votes")
    @Expose
    private String votes;
    @SerializedName("custom_rating_text")
    @Expose
    private String customRatingText;
    @SerializedName("custom_rating_text_background")
    @Expose
    private String customRatingTextBackground;
    @SerializedName("rating_tool_tip")
    @Expose
    private String ratingToolTip;
    @SerializedName("has_fake_reviews")
    @Expose
    private Integer hasFakeReviews;

    protected UserRating(Parcel in) {
        aggregateRating = in.readString();
        ratingText = in.readString();
        ratingColor = in.readString();
        votes = in.readString();
        customRatingText = in.readString();
        customRatingTextBackground = in.readString();
        ratingToolTip = in.readString();
        if (in.readByte() == 0) {
            hasFakeReviews = null;
        } else {
            hasFakeReviews = in.readInt();
        }
    }

    public static final Creator<UserRating> CREATOR = new Creator<UserRating>() {
        @Override
        public UserRating createFromParcel(Parcel in) {
            return new UserRating(in);
        }

        @Override
        public UserRating[] newArray(int size) {
            return new UserRating[size];
        }
    };

    public String getAggregateRating() {
        return aggregateRating;
    }

    public void setAggregateRating(String aggregateRating) {
        this.aggregateRating = aggregateRating;
    }

    public String getRatingText() {
        return ratingText;
    }

    public void setRatingText(String ratingText) {
        this.ratingText = ratingText;
    }

    public String getRatingColor() {
        return ratingColor;
    }

    public void setRatingColor(String ratingColor) {
        this.ratingColor = ratingColor;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getCustomRatingText() {
        return customRatingText;
    }

    public void setCustomRatingText(String customRatingText) {
        this.customRatingText = customRatingText;
    }

    public String getCustomRatingTextBackground() {
        return customRatingTextBackground;
    }

    public void setCustomRatingTextBackground(String customRatingTextBackground) {
        this.customRatingTextBackground = customRatingTextBackground;
    }

    public String getRatingToolTip() {
        return ratingToolTip;
    }

    public void setRatingToolTip(String ratingToolTip) {
        this.ratingToolTip = ratingToolTip;
    }

    public Integer getHasFakeReviews() {
        return hasFakeReviews;
    }

    public void setHasFakeReviews(Integer hasFakeReviews) {
        this.hasFakeReviews = hasFakeReviews;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(aggregateRating);
        dest.writeString(ratingText);
        dest.writeString(ratingColor);
        dest.writeString(votes);
        dest.writeString(customRatingText);
        dest.writeString(customRatingTextBackground);
        dest.writeString(ratingToolTip);
        if (hasFakeReviews == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(hasFakeReviews);
        }
    }
}
