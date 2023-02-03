package com.example.rexpos.models;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    public int id = 0;

    @SerializedName("username")
    public String username = "";

    @SerializedName("email")
    public String email = "";

    @SerializedName("password")
    public String password = "";

    public String shop_id;

    public String shop_name;

    public String phone;
    public String address;
}