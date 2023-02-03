package com.example.rexpos.models.response;

import com.example.rexpos.models.Transaction;

public class ResTransaction extends ResBase {

    public Result results = null;

    public class Result{
        public Transaction transaction = null;
    }
}
