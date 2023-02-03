package com.example.rexpos;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.rexpos.adapters.CategorySpinnerAdapter;
import com.example.rexpos.adapters.DiscountAdapter;
import com.example.rexpos.adapters.TableSpinnerAdater;
import com.example.rexpos.adapters.TransactionListAdapter;
import com.example.rexpos.api.ApiClient;
import com.example.rexpos.common.GlobalConst;
import com.example.rexpos.models.Category;
import com.example.rexpos.models.Discount;
import com.example.rexpos.models.Product;
import com.example.rexpos.models.Table;
import com.example.rexpos.models.Transaction;
import com.example.rexpos.models.response.ResProduct;
import com.example.rexpos.models.response.ResTable;
import com.example.rexpos.models.response.ResTableTransaction;
import com.example.rexpos.utils.DateTimeUtils;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, TransactionListAdapter.OnTransactionItemLongClickListener, View.OnClickListener {

    private WebView mWebView;

    Spinner mSpinnerTable, mSpinnerCategory, mSpinnerDiscount;
    ListView mListViewProducts;
    TableSpinnerAdater mTableSpinnerAdapter;
    CategorySpinnerAdapter mCategorySpinnerAdapter;
    DiscountAdapter mDiscountAdapter;

    TransactionListAdapter mTransactionListAdapter;
    List<Table> mListTable = new ArrayList<>();
    List<Category> mListCategories = new ArrayList<>();
    List<Product> mListProducts = new ArrayList<>();
    List<Transaction> mListTransaction = new ArrayList<>();

    LinearLayout mLinearProductContainer;
    LinearLayout mLinearRowContainerView;

    TextView btnBill, btnComplete;
    TextView mTextPrice, mTextVat, mTextTotal;

    private long selectedTableID = 0;
    private String selectedTableName = "";
    private String seletedTime = "";
    private double total_amount = 0;
    private double vatPrice = 0;
    private double price = 0;
    private double discountPrice = 0;
    private double discountedPrice = 0;
    private int discountValue = 0;
    private int vatRatio = 20;

    private static int[] discountValueArray = new int[1000];
    private int tablePosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initView();
        getListTable();
        mTableSpinnerAdapter = new TableSpinnerAdater(this);
        mSpinnerTable.setAdapter(mTableSpinnerAdapter);
        mCategorySpinnerAdapter = new CategorySpinnerAdapter(this);
        mSpinnerCategory.setAdapter(mCategorySpinnerAdapter);

        mTransactionListAdapter = new TransactionListAdapter(this, this);
        mListViewProducts.setAdapter(mTransactionListAdapter);

        mDiscountAdapter = new DiscountAdapter(this);
        mSpinnerDiscount.setAdapter(mDiscountAdapter);


        setDiscountSpinner(discountValueArray[tablePosition]);

    }


    private void setDiscountSpinner(int discountValue){

        int position = 0;

        switch (discountValue){
            case 0:
                position = 0;
                break;
            case 10:
                position = 1;
                break;
            case 20:
                position = 2;
                break;
                default:
        }

        mSpinnerDiscount.setSelection(position);
    }




    private void initView(){
        mSpinnerTable = findViewById(R.id.spinner_table);
        mSpinnerTable.setOnItemSelectedListener(this);
        mSpinnerCategory = findViewById(R.id.spinner_category);
        mSpinnerCategory.setOnItemSelectedListener(this);
        mListViewProducts = findViewById(R.id.list_products);
        mLinearProductContainer = findViewById(R.id.product_container);
        mLinearRowContainerView = findViewById(R.id.row_container);
        btnBill = findViewById(R.id.btn_bill);
        btnBill.setOnClickListener(this);

        mTextPrice = findViewById(R.id.txt_price);
        mTextVat = findViewById(R.id.txt_vat);
        mTextTotal = findViewById(R.id.txt_total);

        btnComplete = findViewById(R.id.btn_complete);
        btnComplete.setOnClickListener(this);

        mSpinnerDiscount = findViewById(R.id.spinner_discount);
        mSpinnerDiscount.setOnItemSelectedListener(this);
    }


    public void getListTable(){
        //showProgressDialog("waiting");
        Call<ResTable> call = ApiClient.getApiClient(this).getListTableAPI();
        call.enqueue(new Callback<ResTable>() {
            @Override
            public void onResponse(Call<ResTable> call, Response<ResTable> response) {
                //hideProgressDialog();

                ResTable result = response.body();
                if (result != null){
                    if (result.errorCode == 0){
                        ResTable.Result results = result.results;
                        mListTable = results.tables;
                        mTableSpinnerAdapter.addAllTable(mListTable);
                        mListCategories = results.categories;
                        mCategorySpinnerAdapter.addAllCategories(mListCategories);

                    }else {
                        Toast.makeText(getBaseContext(), result.errMsg, Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getBaseContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResTable> call, Throwable t) {
                //hideProgressDialog();
                Toast.makeText(getBaseContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            }
        });
    }





    public void getProducts(long category_id){

        String shop_id = mPrefs.getUserShopId();



        //showProgressDialog("waiting");
        Call<ResProduct> call = ApiClient.getApiClient(this).getProductAPI(category_id, shop_id);
        call.enqueue(new Callback<ResProduct>() {
            @Override
            public void onResponse(Call<ResProduct> call, Response<ResProduct> response) {
                //hideProgressDialog();
                ResProduct result = response.body();
                if (result != null){
                    if (result.errorCode == 0){
                        ResProduct.Result results = result.results;
                        mListProducts = results.products;
                        addProductList(mListProducts);
                    }else {
                        Toast.makeText(getBaseContext(), result.errMsg, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getBaseContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResProduct> call, Throwable t) {
                //hideProgressDialog();
                Toast.makeText(getBaseContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void billAPI(long tableId){

        String shop_id = mPrefs.getUserShopId();

        showProgressDialog("waiting");
        Call<ResTableTransaction> call = ApiClient.getApiClient(this).billAPI(tableId, shop_id);
        call.enqueue(new Callback<ResTableTransaction>() {
            @Override
            public void onResponse(Call<ResTableTransaction> call, Response<ResTableTransaction> response) {
                hideProgressDialog();
                ResTableTransaction result = response.body();
                if (result != null){
                    if (result.errorCode == 0){
                        ResTableTransaction.Result results = result.results;
                        List<Transaction> transactions = results.transactions;

                        doPrint(transactions);
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




    /*
    public void postTransactionAPI(){
        showProgressDialog("waiting");
        List<Transaction> mListTransaction = mTransactionListAdapter.getAllTransactions();
        ReqTransactionList reqTransactionList = new ReqTransactionList(mListTransaction);

        Call<ResTransaction> call = ApiClient.getApiClient(this).postTransactionAPI(reqTransactionList);
        call.enqueue(new Callback<ResTransaction>() {
            @Override
            public void onResponse(Call<ResTransaction> call, Response<ResTransaction> response) {
                hideProgressDialog();
                ResTransaction result = response.body();
                if (result != null){
                    if (result.errorCode == 0){
                        showPrice();
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
    }*/







    private void addProductList(List<Product> mListProducts){

        if (mLinearRowContainerView.getChildCount() > 0)
            mLinearRowContainerView.removeAllViews();

        for (int i= 0; i<mListProducts.size() ;i+=GlobalConst.PRODUCT_ROW_NUMBER) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View convertViewRow = layoutInflater.inflate(R.layout.product_row, null);
            mLinearRowContainerView.addView(convertViewRow);
            for (int j = 0; j < GlobalConst.PRODUCT_ROW_NUMBER; j++){
                LinearLayout mLinearRow = convertViewRow.findViewById(R.id.product_container);
                View convertView = layoutInflater.inflate(R.layout.product_item, null);
                final int pos = i + j;


                Product product = mListProducts.get(pos);
                TextView mTextProductName = convertView.findViewById(R.id.txt_product_name);
                mTextProductName.setText(product.product_name);


                ImageView mImageFood = convertView.findViewById(R.id.img_food);
                Picasso.with(this).load(GlobalConst.HOST_FOODS + product.photo).fit().centerCrop().placeholder(R.drawable.empty).into(mImageFood);


                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.width = GlobalConst.PRODUCT_ROW_WIDTH / GlobalConst.PRODUCT_ROW_NUMBER;
                convertView.setLayoutParams(params);


                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Product product = mListProducts.get(pos);

                        Transaction transaction = new Transaction();
                        transaction.table_id = selectedTableID;
                        transaction.product_name = product.product_name;
                        transaction.product_id = product.product_id;
                        transaction.shop_id = product.shop_id;
                        transaction.status = "Open";
                        transaction.unit_price = product.price;
                        DecimalFormat twoDForm = new DecimalFormat("#.##");
                        transaction.tax_amount = Double.parseDouble(twoDForm.format(product.price * GlobalConst.taxScale));
                        transaction.total_amount = transaction.unit_price + transaction.tax_amount;

                        addOrderAPI(transaction);
                    }
                });

                mLinearRow.addView(convertView);
                if (pos == mListProducts.size() - 1) return;
            }
        }
    }





    public void addOrderAPI(Transaction transaction){

        showProgressDialog("waiting");

        Map<String, String> map = new HashMap<>();

        map.put("table_id", String.valueOf(transaction.table_id));
        map.put("product_name", transaction.product_name);
        map.put("product_id", String.valueOf(transaction.product_id));
        map.put("shop_id", String.valueOf(transaction.shop_id));
        map.put("status", transaction.status);
        map.put("unit_price", String.valueOf(transaction.unit_price));
        map.put("tax_amount", String.valueOf(transaction.tax_amount));
        map.put("total_amount", String.valueOf(transaction.total_amount));

        Log.e("transaction", map.toString());
        Call<ResTableTransaction> call = ApiClient.getApiClient(this).addOrderAPI(map);
        call.enqueue(new Callback<ResTableTransaction>() {
            @Override
            public void onResponse(Call<ResTableTransaction> call, Response<ResTableTransaction> response) {
                hideProgressDialog();
                ResTableTransaction result = response.body();
                Log.e("result", "code=" + response.code() + ", mesage=" + response.message());
                if (result != null){
                    if (result.errorCode == 0){
                        ResTableTransaction.Result results = result.results;
                        List<Transaction> transactions = results.transactions;
                        mTransactionListAdapter.removeAll();
                        mTransactionListAdapter.addAllTransaction(transactions);

                        setPrices(results.price);

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





    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spinner_table:
                tablePosition = position;

                setDiscountSpinner(discountValueArray[tablePosition]);

                selectedTableID = id;
                selectedTableName = mListTable.get(position).table_name;
                seletedTime = DateTimeUtils.getDateTimeString(DateTimeUtils.FMT_STANDARD);
                getOrdersFromTableAPI(selectedTableID);

                break;
            case R.id.spinner_category:

                Log.e("==========", mPrefs.getUserShopId());
                Log.e("==========", "dsafsda");

                getProducts(id);
                break;

            case R.id.spinner_discount:
                Discount discount = mDiscountAdapter.getItem(position);
                discountValue = discount.discountValue;

                discountValueArray[tablePosition] = discount.discountValue;

                discountPrice = (price * discount.discountValue) / 100;
                discountedPrice = price - discountPrice;
                vatPrice = discountedPrice * vatRatio / 100;
                total_amount = discountedPrice + vatPrice;
                DecimalFormat twoDForm = new DecimalFormat("#.##");
                mTextVat.setText(String.valueOf(Double.valueOf(twoDForm.format(Double.parseDouble(String.valueOf(vatPrice))))));
                mTextTotal.setText(String.valueOf(Double.valueOf(twoDForm.format(Double.parseDouble(String.valueOf(total_amount))))));

                break;

                default:
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}





    public void getOrdersFromTableAPI(long tableId){

        String shop_id = mPrefs.getUserShopId();

        showProgressDialog("waiting");
        Call<ResTableTransaction> call = ApiClient.getApiClient(this).getOrdersFromTableAPI(tableId, shop_id);
        call.enqueue(new Callback<ResTableTransaction>() {
            @Override
            public void onResponse(Call<ResTableTransaction> call, Response<ResTableTransaction> response) {
                hideProgressDialog();
                ResTableTransaction result = response.body();
                if (result != null){
                    if (result.errorCode == 0){
                        ResTableTransaction.Result results = result.results;
                        List<Transaction> transactions = results.transactions;
                        mTransactionListAdapter.removeAll();
                        mTransactionListAdapter.addAllTransaction(transactions);

                        setPrices(results.price);

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




    private void setPrices(String price){

        discountValue = discountValueArray[tablePosition];

        this.price = Double.parseDouble(price);
        discountPrice = this.price * discountValue / 100;
        discountedPrice = this.price - discountPrice;
        vatPrice = discountedPrice * vatRatio / 100;
        total_amount = discountedPrice + vatPrice;

        discountedPrice = total_amount * (100 - discountValue) / 100;
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        mTextPrice.setText(String.valueOf(Double.valueOf(twoDForm.format(Double.parseDouble(price)))));
        mTextVat.setText(String.valueOf(Double.valueOf(twoDForm.format(Double.parseDouble(String.valueOf(this.vatPrice))))));
        mTextTotal.setText(String.valueOf(Double.valueOf(twoDForm.format(Double.parseDouble(String.valueOf(total_amount))))));
    }



    @Override
    public void longClicked(int pos) {
        /*
        mListViewProducts.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTransactionListAdapter.removeItem(pos);
            }
        }, 500);

        */

        Transaction transaction = mTransactionListAdapter.getItem(pos);
        if (transaction.status.equals("Open") || transaction.status.equals("Cancel")){
            AlertDialog.Builder dialog = new AlertDialog.Builder(OrderActivity.this);
            dialog.setCancelable(false);
            dialog.setTitle("Confirm!");
            dialog.setMessage("Are you sure you want to delete this product?" );
            dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    //Action for "Delete".
                    deleteOrderItemAPI(transaction);
                }
            })
                    .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Action for "Cancel".
                        }
                    });

            final AlertDialog alert = dialog.create();
            alert.show();

        } else {
            Toast.makeText(this, "Can't remove it", Toast.LENGTH_SHORT).show();
        }


    }




    public void deleteOrderItemAPI(Transaction transaction){
        showProgressDialog("waiting");

        long tableId = transaction.table_id;
        long productId = transaction.product_id;
        long transationId = transaction.transaction_id;
        String shop_id = mPrefs.getUserShopId();

        Log.e("-----------", shop_id);

        Call<ResTableTransaction> call = ApiClient.getApiClient(this).deleteOrderItemAPI(transationId, tableId, productId, shop_id);
        call.enqueue(new Callback<ResTableTransaction>() {
            @Override
            public void onResponse(Call<ResTableTransaction> call, Response<ResTableTransaction> response) {
                hideProgressDialog();
                ResTableTransaction result = response.body();
                if (result != null){
                    if (result.errorCode == 0){
                        ResTableTransaction.Result results = result.results;
                        List<Transaction> transactions = results.transactions;
                        mTransactionListAdapter.removeAll();
                        mTransactionListAdapter.addAllTransaction(transactions);

                        setPrices(results.price);
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






    private void doPrint(List<Transaction> mListTransaction) {

        DecimalFormat twoDForm = new DecimalFormat("#.##");

        String commandsToPrint =
                        "<BIG><BOLD><LEFT> "+ mPrefs.getUserShopName() +" <BR>\n" +
                        "<MEDIUM1><LEFT> "+ mPrefs.getUserPhone() +"<BR>\n" +
                        "<MEDIUM1><LEFT> "+ mPrefs.getUserAddress() +"<BR>\n" +
                        "<RIGHT> "+ seletedTime +"<BR>\n" +
                        "<CENTER>Invoice<BR>\n" +
                        "<BOLD>Items                           Total<BR>\n"
                ;


        List<Transaction> transactions = mListTransaction;

        for (Transaction item: transactions) {
            commandsToPrint += "<LEFT>"+ item.product_name +"                       "+ item.unit_price +"<BR>\n";
        }

        commandsToPrint += "<BOLD>Total                           "+ price +"<BR>\n";

        commandsToPrint +=
                "<RIGHT><BOLD>Discount: $ "+ discountPrice +"<BR>\n" +
                "<RIGHT><BOLD>Vat: $ "+ vatPrice +"<BR>\n" +
                "<RIGHT><BOLD>Total: $ "+ Double.valueOf(twoDForm.format(Double.parseDouble(String.valueOf(total_amount)))) +"<BR>\n" +
                "<BR>\n" +
                "<BOLD><CENTER>Thank you<BR>\n" +
                "<CUT>\n" +
                "<DRAWER>\n" // OPEN THE CASH DRAWER
        ;



        try {
            Intent intent = new Intent("pe.diegoveloper.printing");
            intent.setType("text/plain");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, commandsToPrint);
            startActivityForResult(intent, 101);

        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=pe.diegoveloper.printerserverapp"));
            startActivity(intent);
        }
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        // super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101){
            if (resultCode == RESULT_OK){
                //Printing is ok
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            } else {
                if (data != null) {
                    String errorMessage = data.getStringExtra("errorMessage");
                    //Printing with error
                }
            }
        }
    }




    private void complete(long tableId){
        String shop_id = mPrefs.getUserShopId();
        showProgressDialog("waiting");
        Call<ResTableTransaction> call = ApiClient.getApiClient(this).completeAPI(tableId, shop_id);
        call.enqueue(new Callback<ResTableTransaction>() {
            @Override
            public void onResponse(Call<ResTableTransaction> call, Response<ResTableTransaction> response) {
                hideProgressDialog();
                ResTableTransaction result = response.body();
                if (result != null){
                    if (result.errorCode == 0){
                        discountValueArray[tablePosition] = 0;
                        mTransactionListAdapter.removeAll();
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




    private void confirmComplete(){
        List<Transaction> transactionList = mTransactionListAdapter.getAllTransactions();
        for (Transaction item: transactionList) {
            if (item.status.equals("Open")){
                Toast.makeText(this, "Opened product exists", Toast.LENGTH_LONG).show();
                return;
            }

            if (item.status.equals("Cancel")){
                Toast.makeText(this, "Canceled product exists, please remove it.", Toast.LENGTH_LONG).show();
                return;
            }
        }


        AlertDialog.Builder dialog = new AlertDialog.Builder(OrderActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Confirm!");
        dialog.setMessage("Are you sure you want to complete this order?" );
        dialog.setPositiveButton("Complete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //Action for "Delete".
                complete(selectedTableID);
            }
        })
                .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Action for "Cancel".
                    }
                });
        final AlertDialog alert = dialog.create();
        alert.show();
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_bill:
                billAPI(selectedTableID);
                break;
            case R.id.btn_complete:
                confirmComplete();
                break;
                default:
        }
    }
}
