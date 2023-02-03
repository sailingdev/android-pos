package com.example.rexpos.models.response;

import com.example.rexpos.models.Transaction;

import java.util.List;

public class ResTableTransaction extends ResBase{

    public Result results = null;

    public class Result{
        public List<Transaction> transactions = null;
        public String price;
        public String vat;
        public String total;
    }


}
