package com.hvdcreations.tagit;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;


public class LoginActivity extends AppCompatActivity {

    TextView TxtLogin;
    CountryCodePicker ccp;
    EditText ETPhone;
    ProgressBar progressBar;

    ConstraintLayout loginscreen, verifyscreen;

    String code = String.valueOf(91), phone = String.valueOf(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TxtLogin = findViewById(R.id.tv_login);
        ccp = findViewById(R.id.countryCodePicker);
        ETPhone = findViewById(R.id.et_phone);

        loginscreen = findViewById(R.id.layout_login);
        verifyscreen = findViewById(R.id.layout_verify);
        progressBar = findViewById(R.id.progressBar);

        ccp.registerPhoneNumberTextView(ETPhone);

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                code = selectedCountry.getPhoneCode();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = ETPhone.getText().toString();
                if (phone == null) {
                    Snackbar.make(view, "Enter your Phone Number", Snackbar.LENGTH_SHORT).show();
                } else {
                Toast.makeText(getBaseContext(), "+" + code + phone, Toast.LENGTH_SHORT).show();

                progressBar.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        loginscreen.setVisibility(View.INVISIBLE);
                        verifyscreen.setVisibility(View.VISIBLE);
                    }

                }, 2000);
            }


            }
        });
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
