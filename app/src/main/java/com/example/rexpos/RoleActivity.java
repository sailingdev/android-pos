package com.example.rexpos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class
RoleActivity extends BaseActivity implements View.OnClickListener {


    TextView btnCashier, btnKitchen, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);
        btnCashier = findViewById(R.id.btn_cashier);
        btnCashier.setOnClickListener(this);
        btnKitchen = findViewById(R.id.btn_kitchen);
        btnKitchen.setOnClickListener(this);

        btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cashier:
                Intent intent = new Intent(this, OrderActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_kitchen:
                Intent intent1 = new Intent(this, DemoActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_logout:
                logout();
                break;
                default:
        }
    }


    private void logout(){
        mPrefs.setUserID(0);
        mPrefs.setUserShopName("");
        mPrefs.setUserShopId("");
        mPrefs.setUserEmail("");
        mPrefs.setUserPassword("");
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
