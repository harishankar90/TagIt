package com.hvdcreations.tagit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.goodiebag.pinview.Pinview;
import com.google.firebase.auth.FirebaseAuth;
import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;

public class LoginActivity extends AppCompatActivity {

    private ConnectivityManager connectivityManager;

    TextView TxtLogin;
    CountryCodePicker ccp;
    EditText ETPhone;
    ProgressBar progressBar;
    FloatingActionButton fabLogin;
    Button register;

    String code = String.valueOf(91), phone = String.valueOf(0);

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        TxtLogin = findViewById(R.id.tv_login);
        ccp = findViewById(R.id.countryCodePicker);
        ETPhone = findViewById(R.id.et_phone);

        progressBar = findViewById(R.id.progressBar);

        fabLogin = findViewById(R.id.fab);
        register = findViewById(R.id.btn_LogRegister);


        //ccp.registerPhoneNumberTextView(ETPhone);
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                code = selectedCountry.getPhoneCode();
            }
        });


        NetworkInfo networkInfo =   connectivityManager.getActiveNetworkInfo();
        if (networkInfo==null){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("You SmartPhone is useless, if you are NOT connected to the INTERNET !");
            alertDialogBuilder.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                            startActivity(intent);
                            finish();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        fabLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                phone = ETPhone.getText().toString();
                if (phone.isEmpty()) {
                    Snackbar.make(view, "Nice Try, Enter your Phone no.", Snackbar.LENGTH_LONG).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {

                        @SuppressLint("RestrictedApi")
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                            String number = "+" + code + phone;
                            Intent intent = new Intent(LoginActivity.this,VerifyActivtiy.class);
                            intent.putExtra("mobile",number);

                            sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                            sharedPreferences.edit().putString("mobile",number).commit();

                            startActivity(intent);
                        }
                    }, 2000);

                }
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });



    }
}
