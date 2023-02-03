package com.example.rexpos.models;

import java.io.Serializable;
import java.util.List;

public class KitchenTableTransaction {

    public long table_id;
    public String table_name;
    public String date_transaction;

    public List<Transaction> transactions;
}
