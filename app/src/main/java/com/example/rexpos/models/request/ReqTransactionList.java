package com.example.rexpos.models.request;

import com.example.rexpos.models.Transaction;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ReqTransactionList {

    @SerializedName("transactions")
    @Expose
    private List<Transaction> transactions;
    public ReqTransactionList(List<Transaction> transactions) {
        this.transactions=transactions;
    }


}
