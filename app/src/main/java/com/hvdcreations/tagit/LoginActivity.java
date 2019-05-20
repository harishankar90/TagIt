package com.hvdcreations.tagit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.core.executor.TaskExecutor;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;

import java.util.concurrent.TimeUnit;


public class LoginActivity extends AppCompatActivity {

    private ConnectivityManager connectivityManager;

    private FirebaseAuth mAuth;

    TextView TxtLogin;
    CountryCodePicker ccp;
    EditText ETPhone;
    ProgressBar progressBar;
    FloatingActionButton fabLogin, fabVerify;

    ConstraintLayout loginscreen, verifyscreen;
    Pinview pinview;

    String code = String.valueOf(91), phone = String.valueOf(0);

    String verificationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        mAuth = FirebaseAuth.getInstance();

        TxtLogin = findViewById(R.id.tv_login);
        ccp = findViewById(R.id.countryCodePicker);
        ETPhone = findViewById(R.id.et_phone);

        loginscreen = findViewById(R.id.layout_login);
        verifyscreen = findViewById(R.id.layout_verify);
        progressBar = findViewById(R.id.progressBar);

        pinview = findViewById(R.id.OTPpincode);

        //ccp.registerPhoneNumberTextView(ETPhone);

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                code = selectedCountry.getPhoneCode();
            }
        });

        fabLogin = findViewById(R.id.fab);
        fabVerify = findViewById(R.id.fab_verify);

        NetworkInfo networkInfo =   connectivityManager.getActiveNetworkInfo();
        if (networkInfo==null){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("You SmartPhone is useless, if you are NOT connected to the INTERNET !");
                    alertDialogBuilder.setPositiveButton("Settings",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                                    startActivity(intent);                                }
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
                        loginscreen.setVisibility(View.INVISIBLE);
                        verifyscreen.setVisibility(View.VISIBLE);
                        fabLogin.setVisibility(View.INVISIBLE);
                        fabVerify.setVisibility(View.VISIBLE);
                    }

                }, 2000);

                String number = "+" + code + phone;
                sendVerificationCode(number);

            }

            }
        });

        fabVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = pinview.getValue();

                if (pin.isEmpty() || pin.length()<6){
                    Snackbar.make(v, "Nice Try, Wait for OTP !", Snackbar.LENGTH_LONG).show();
                }
                verifyCode(code);

            }
        });

    }

    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    private void sendVerificationCode(String number) {
        Toast.makeText(this, "Your number is "+number, Toast.LENGTH_SHORT).show();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(number,60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mCallBack);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationID = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code!=null){
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
            alertDialogBuilder.setMessage("Verification Failed !");
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    };

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
