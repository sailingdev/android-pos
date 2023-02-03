package com.example.rexpos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class LoginActivity extends BaseActivity implements View.OnClickListener {


    TextView btnLogin, btnSignup;
    EditText edtEmail ,edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        btnSignup = findViewById(R.id.btn_sign_up);
        btnSignup.setOnClickListener(this);
        edtEmail = findViewById(R.id.edit_email);
        edtPassword = findViewById(R.id.edit_password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                signin();
                break;
            case R.id.btn_sign_up:
                Intent intent1 = new Intent(this, SignupActivity.class);
                startActivity(intent1);
            default:
        }
    }



    private void signin(){
        if (!checkEditTexts()) { return; }

        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String fcmToken = mPrefs.getFcmToken();

        showCustomProgressDialog("signIn...");



        Call<ResLogin> call = ApiClient.getApiClient(this).signInAPI(email, password, fcmToken);
        call.enqueue(new Callback<ResLogin>() {
            @Override
            public void onResponse(Call<ResLogin> call, Response<ResLogin> response) {
                hideProgressDialog();
                ResLogin result = response.body();

                if (result != null){
                    if (result.errorCode == 0){
                        Login signInResModel = result.results;

                        mPrefs.setUserID(signInResModel.user.id);
                        mPrefs.setUserEmail(email);
                        mPrefs.setUserPassword(password);
                        mPrefs.setUserShopId(signInResModel.user.shop_id);
                        mPrefs.setUserShopName(signInResModel.user.shop_name);
                        mPrefs.setUserPhone(signInResModel.user.phone);
                        mPrefs.setUserAddress(signInResModel.user.address);

                        startActivity(new Intent(LoginActivity.this, RoleActivity.class));
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
        BaseActivity.Out<String> out = new BaseActivity.Out<String>();


        if (!checkEmail(edtEmail, out)) {
            return false;
        }

        return checkEditText(edtPassword, out);
    }


    @Override
    protected void onResume() {
        super.onResume();

        String email = mPrefs.getUserEmail();
        String password = mPrefs.getUserPassword();
        if ((!email.equals("")) && (!password.equals(""))) {
            edtEmail.setText(email);
            edtPassword.setText(password);
            signin();
        }
    }
}
