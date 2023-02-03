package com.example.rexpos.models.response;

import com.google.gson.annotations.SerializedName;

public class ResBase {
    @SerializedName("error_code")
    public int errorCode = 0;
    @SerializedName("error_message")
    public String errMsg = "";
}
