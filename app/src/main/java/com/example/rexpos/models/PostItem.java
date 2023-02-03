package com.example.rexpos.models;

import com.google.gson.annotations.SerializedName;

public class PostItem {

    @SerializedName("description")
    private String mDescription;
    @SerializedName("time")
    private String mTime;
    @SerializedName("title")
    private String mTitle;

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

}
