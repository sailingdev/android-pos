package com.example.rexpos.models.response;

import com.example.rexpos.models.KitchenTableTransaction;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResKitchenTableTransaction extends ResBase{

    @SerializedName("results")
    public Result results = null;

    public class Result{
        @SerializedName("tables")
        public ArrayList<KitchenTableTransaction> tables = new ArrayList<>();


        @SerializedName("totalPage")
        public int totalPage = 0;
    }

}
