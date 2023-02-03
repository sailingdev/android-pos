package com.example.rexpos.models.response;

import com.example.rexpos.models.Category;
import com.example.rexpos.models.Table;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResTable extends ResBase{

    @SerializedName("results")
    public Result results = null;

    public class Result{
        @SerializedName("tables")
        public ArrayList<Table> tables = new ArrayList<>();

        public ArrayList<Category> categories = new ArrayList<>();

        @SerializedName("totalPage")
        public int totalPage = 0;
    }


}
