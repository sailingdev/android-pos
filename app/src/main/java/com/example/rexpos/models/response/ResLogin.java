package com.example.rexpos.models.response;

import com.example.rexpos.models.Login;
import com.google.gson.annotations.SerializedName;

public class ResLogin extends ResBase {

    @SerializedName("results")
    public Login results = null;
}
