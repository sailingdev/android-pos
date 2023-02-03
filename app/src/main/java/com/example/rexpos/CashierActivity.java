package com.example.rexpos;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.rexpos.common.GlobalConst;

public class CashierActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {



    private WebView mWebView;
    WebView webView;
    SwipeRefreshLayout swipeRefreshLayout;
    private String currentURL;

    TextView btnPrint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);


        btnPrint = findViewById(R.id.btn_print);
        btnPrint.setOnClickListener(this);


        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setEnabled(false);

        webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
//        webView.loadUrl("http://erpmessenger.com/downtime_katolec/");
        //webView.loadUrl(GlobalConst.HOST_NAME);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                swipeRefreshLayout.setRefreshing(false);

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                currentURL = String.valueOf(request.getUrl());
                return super.shouldOverrideUrlLoading(view, request);

            }
        });
    }




    private void doWebViewPrint() {
        // Create a WebView object specifically for printing
        WebView webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "page finished loading " + url);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    createWebPrintJob(view);
                }
                mWebView = null;
            }
        });

        // Generate an HTML document on the fly:
        String htmlDocument = "<html><body><h1>Test Content</h1><p>Testing, " +
                "testing, testing...</p></body></html>";
        webView.loadDataWithBaseURL(GlobalConst.HOST_NAME, htmlDocument, "text/HTML", "UTF-8", null);
        // Keep a reference to WebView object until you pass the PrintDocumentAdapter
        // to the PrintManager
        mWebView = webView;
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createWebPrintJob(WebView webView) {

        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        String jobName = getString(R.string.app_name) + " Document";

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            printAdapter = webView.createPrintDocumentAdapter(jobName);
        }

        // Create a print job with name and adapter instance
        PrintJob printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());

        // Save the job object for later status checking
        //printJobs.add(printJob);
    }





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ReLoadWebView(GlobalConst.HOST_NAME);

    }

    @Override
    public void onRefresh() {
        ReLoadWebView(GlobalConst.HOST_NAME);
    }

    private void ReLoadWebView(String currentURL) {
        swipeRefreshLayout.setRefreshing(true);
        webView.loadUrl(currentURL);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_print:
                doWebViewPrint();
                break;
        }
    }
}
