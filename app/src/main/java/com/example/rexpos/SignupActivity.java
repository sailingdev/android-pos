package com.example.rexpos;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rexpos.api.ApiClient;
import com.example.rexpos.models.Login;
import com.example.rexpos.models.User;
import com.example.rexpos.models.response.ResLogin;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends BaseActivity implements View.OnClickListener {



    TextView btnSubmit;
    ImageView btnBack;
    EditText edtUsername, edtEmail, editShopId ,edtPassword, edtConfirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);
        btnBack = findViewById(R.id.img_back);
        btnBack.setOnClickListener(this);
        edtUsername = findViewById(R.id.edit_username);
        edtEmail = findViewById(R.id.edit_email);
        edtConfirmPassword = findViewById(R.id.edit_confirm_password);
        edtPassword = findViewById(R.id.edit_password);
        editShopId = findViewById(R.id.edit_shop_id);
        editShopId.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                register();
                break;
            case R.id.img_back:
                finish();
                break;
        }
    }


    public void register(){
        if (!checkEditTexts()) { return; }

        final User user = new User();
        user.username = edtUsername.getText().toString().trim();
        user.email = edtEmail.getText().toString().trim();
        user.password = edtPassword.getText().toString().trim();
        user.shop_id = editShopId.getText().toString().trim();

        showCustomProgressDialog("register...");

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("username", user.username);
        map.put("password", user.password);
        map.put("email", user.email);
        map.put("shop_id", user.shop_id);


        Call<ResLogin> call = ApiClient.getApiClient(this).signUpAPI(map);
        call.enqueue(new Callback<ResLogin>() {
            @Override
            public void onResponse(Call<ResLogin> call, Response<ResLogin> response) {
                hideProgressDialog();
                ResLogin result = response.body();

                if (result != null){
                    if (result.errorCode == 0){
                        Login signInResModel = result.results;

                        mPrefs.setUserID(signInResModel.user.id);
                        mPrefs.setUserEmail(user.email);
                        mPrefs.setUserPassword(user.password);
                        mPrefs.setUserShopId(user.shop_id);

                        Toast.makeText(getBaseContext(), result.errMsg, Toast.LENGTH_LONG).show();
                        finish();
                    }else {
                        Toast.makeText(getBaseContext(), result.errMsg, Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getBaseContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResLogin> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(getBaseContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();

            }
        });
    }





    private Boolean checkEditTexts(){
        Out<String> out = new Out<String>();
        if (!checkEditText(edtUsername, out)) {
            return false;
        }

        if (!checkEmail(edtEmail, out)) {
            return false;
        }

        if (!checkEditText(edtPassword, out)) {
            return false;
        }

        if (!checkEditText(edtConfirmPassword, out)) {
            return false;
        }

        if (!edtPassword.getText().toString().equals( edtConfirmPassword.getText().toString())) {
            edtConfirmPassword.setError("Password is not matched");
            edtConfirmPassword.requestFocus();
            return false;
        }
        return true;
    }

}
