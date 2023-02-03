package com.example.rexpos;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rexpos.adapters.SetStatusListAdapter;
import com.example.rexpos.api.ApiClient;
import com.example.rexpos.models.Transaction;
import com.example.rexpos.models.response.ResTableTransaction;
import com.example.rexpos.models.response.ResTransaction;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KitchenSetStatusActivity extends BaseActivity implements SetStatusListAdapter.OnItemClickListener {


    private WebView mWebView;
    String tableName;
    String date_transaction;


    private ListView setStatusListView;
    private SetStatusListAdapter setStatusListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_kitchen_set_status);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        tableName = getIntent().getExtras().getString("tableName");
        date_transaction = getIntent().getExtras().getString("date_transaction");
        long tableId = getIntent().getExtras().getLong("tableId");

        setStatusListAdapter = new SetStatusListAdapter(this, this);
        getspecifiedtabletransactions(tableId, date_transaction);
        setStatusListView = findViewById(R.id.setStatusListView);
        setStatusListView.setAdapter(setStatusListAdapter);
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.btn_print) {
            doPrint(setStatusListAdapter.getAllData());
            return true;
        }

        if (id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    private void doPrint(List<Transaction> mListTransaction) {

        String commandsToPrint =
                        "<BIG><BOLD><CENTER>" + tableName + " <BR>\n" +
                        "<CENTER>"+ date_transaction +"<BR>\n";

        List<Transaction> transactions = mListTransaction;

        for (Transaction item: transactions) {
            commandsToPrint += "<BIG><BOLD><CENTER>"+ item.product_name +"<BR>\n";
        }

        commandsToPrint += "<BR>\n" +
                "<CUT>\n" +
                "<DRAWER>\n" // OPEN THE CASH DRAWER
        ;


        try {
            Intent intent = new Intent("pe.diegoveloper.printing");
            intent.setType("text/plain");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, commandsToPrint);
            startActivity(intent);

        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=pe.diegoveloper.printerserverapp"));
            startActivity(intent);
        }

    }







    public void getspecifiedtabletransactions(long table_id, String date_transaction){

        String shop_id = mPrefs.getUserShopId();

        showProgressDialog("waiting");
        Call<ResTableTransaction> call = ApiClient.getApiClient(this).getspecifiedtabletransactionsAPI(table_id, date_transaction, shop_id);
        call.enqueue(new Callback<ResTableTransaction>() {
            @Override
            public void onResponse(Call<ResTableTransaction> call, Response<ResTableTransaction> response) {
                hideProgressDialog();
                ResTableTransaction result = response.body();
                if (result != null){
                    if (result.errorCode == 0){
                        ResTableTransaction.Result results = result.results;
                        setStatusListAdapter.addAllList(results.transactions);

                    }else {
                        Toast.makeText(getBaseContext(), result.errMsg, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getBaseContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResTableTransaction> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(getBaseContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            }
        });
    }





    public void setStatus(long transaction_id, long table_id, String date_transaction, long product_id, int position, String status){

        showProgressDialog("waiting");

        Call<ResTransaction> call = ApiClient.getApiClient(this).setStatusAPI(transaction_id, table_id, date_transaction, product_id, status);
        call.enqueue(new Callback<ResTransaction>() {
            @Override
            public void onResponse(Call<ResTransaction> call, Response<ResTransaction> response) {
                hideProgressDialog();
                ResTransaction result = response.body();
                if (result != null){
                    if (result.errorCode == 0){
                        ResTransaction.Result results = result.results;
                        Transaction transaction = results.transaction;
                        setStatusListAdapter.updateItem(position, transaction);

                    }else {
                        Toast.makeText(getBaseContext(), result.errMsg, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getBaseContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResTransaction> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(getBaseContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            }
        });
    }





    @Override
    public void onBtnServedClicked(int pos) {
        Transaction transaction = setStatusListAdapter.getAllData().get(pos);
        long transaction_id = transaction.transaction_id;
        long table_id = transaction.table_id;
        String date_transaction = transaction.date_transaction;
        long product_id = transaction.product_id;
        String status = "Served";
        setStatus(transaction_id, table_id, date_transaction, product_id, pos, status);
    }


    @Override
    public void onBtnCancelClicked(int pos) {
        Transaction transaction = setStatusListAdapter.getAllData().get(pos);
        long transaction_id = transaction.transaction_id;
        long table_id = transaction.table_id;
        String date_transaction = transaction.date_transaction;
        long product_id = transaction.product_id;
        String status = "Cancel";
        setStatus(transaction_id, table_id, date_transaction, product_id, pos, status);
    }

    @Override
    public void onBtnPrintClicked(int pos) {
        Transaction transaction = setStatusListAdapter.getItem(pos);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        doPrint(transactions);
    }

}
