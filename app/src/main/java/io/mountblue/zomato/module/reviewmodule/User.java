package io.mountblue.zomato.module.reviewmodule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("foodie_level")
    @Expose
    public String foodieLevel;
    @SerializedName("foodie_level_num")
    @Expose
    public Integer foodieLevelNum;
    @SerializedName("foodie_color")
    @Expose
    public String foodieColor;
    @SerializedName("profile_url")
    @Expose
    public String profileUrl;
    @SerializedName("profile_image")
    @Expose
    public String profileImage;
    @SerializedName("profile_deeplink")
    @Expose
    public String profileDeeplink;

    public User(String name, String foodieLevel, Integer foodieLevelNum, String foodieColor, String profileUrl, String profileImage, String profileDeeplink) {
        this.name = name;
        this.foodieLevel = foodieLevel;
        this.foodieLevelNum = foodieLevelNum;
        this.foodieColor = foodieColor;
        this.profileUrl = profileUrl;
        this.profileImage = profileImage;
        this.profileDeeplink = profileDeeplink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFoodieLevel() {
        return foodieLevel;
    }

    public void setFoodieLevel(String foodieLevel) {
        this.foodieLevel = foodieLevel;
    }

    public Integer getFoodieLevelNum() {
        return foodieLevelNum;
    }

    public void setFoodieLevelNum(Integer foodieLevelNum) {
        this.foodieLevelNum = foodieLevelNum;
    }

    public String getFoodieColor() {
        return foodieColor;
    }

    public void setFoodieColor(String foodieColor) {
        this.foodieColor = foodieColor;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileDeeplink() {
        return profileDeeplink;
    }

    public void setProfileDeeplink(String profileDeeplink) {
        this.profileDeeplink = profileDeeplink;
    }
}
