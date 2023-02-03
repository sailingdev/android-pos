package com.example.rexpos.models.response;

import com.example.rexpos.models.Product;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResProduct extends ResBase {

    @SerializedName("results")
    public Result results = null;

    public class Result{
        @SerializedName("products")
        public ArrayList<Product> products = new ArrayList<>();
    }

}
