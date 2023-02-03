package com.example.rexpos;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rexpos.common.GlobalConst;
import com.example.rexpos.utils.AppPrefs;
import com.example.rexpos.utils.widgets.CustomProgressDialog;

public class BaseActivity extends AppCompatActivity {

    public static final String TAG = "BaseActivity";

    public AppPrefs mPrefs;
    private CustomProgressDialog customProgressDialog;
    private ProgressDialog progressDialog;


    private Boolean doubleBackToExitPressedOnce = false;


    private void backPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }


    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            backPressed();
        } else {
            super.onBackPressed();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPrefs = AppPrefs.create(this);
        getScreenSize();

    }

    public void showCustomProgressDialog(String message) {
        customProgressDialog = new CustomProgressDialog(this, message);
        customProgressDialog.setCancelable(false);
        customProgressDialog.show();
    }


    public void showProgressDialog(String msg) {
        showProgressDialog("", msg);
    }

    public void showProgressDialog(String title, String message) {
        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }


    public void hideProgressDialog() {
        if (customProgressDialog != null) customProgressDialog.dismiss();
        if (progressDialog != null) progressDialog.dismiss();
    }



    public  class Out<T> {
        private T s = null;

        public void set(T value) {
            this.s = value;
        }

        public T get() {
            return this.s;
        }
    }


    public Boolean checkEditText(EditText edit, Out<String> value) {
        return checkEditText(edit, value, getString(R.string.required_field));
    }

    public Boolean checkEditText(EditText edit, Out<String> value, String error) {

        edit.setError(null);
        String string = edit.getText().toString();
        if (TextUtils.isEmpty(string)) {
            edit.setError(error);
            edit.requestFocus();
            return false;
        }
        value.set(string);
        return true;
    }


    public Boolean checkEmail(EditText edit, Out<String> value) {
        if (!checkEditText(edit, value)) return false;
        String string = value.get();
        Boolean res = !TextUtils.isEmpty(string) && android.util.Patterns.EMAIL_ADDRESS.matcher(string).matches();
        if (!res) {
            edit.setError(getString(R.string.string_msg_email_invalid));
            edit.requestFocus();
            return false;
        }
        return true;
    }


    private void getScreenSize(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        double wi=(double)width/(double)dm.xdpi;
        double hi=(double)height/(double)dm.ydpi;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi,2);
        double screenInches = Math.sqrt(x+y);

        GlobalConst.WITH_SCREEN = width;
        GlobalConst.HEIGHT_SCREEN = height;
        GlobalConst.PRODUCT_ROW_WIDTH = width * 6 / 11;



    }


}
