package com.hvdcreations.tagit;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.io.IOError;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class VerifyActivtiy extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Pinview pinview;
    FloatingActionButton fabVerify;

    String verificationID;
    String codeOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        pinview = findViewById(R.id.OTPpincode);

        mAuth = FirebaseAuth.getInstance();

        fabVerify = findViewById(R.id.fab_verify);

        Intent intent = getIntent();
        String mobile = intent.getStringExtra("mobile");
        sendVerificationCode(mobile);

        fabVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = pinview.getValue();

                try{
                    if (pin.isEmpty() || pin.length()<6){
                        Snackbar.make(v, "Nice Try, Wait for OTP !", Snackbar.LENGTH_LONG).show();
                    }
                    verifyCode(pin);
                }catch (Exception e){
                    Log.d("snack failure ---->",e.getMessage());
                }
               /* if (pin.isEmpty() || pin.length()<6){
                    Snackbar.make(v, "Nice Try, Wait for OTP !", Snackbar.LENGTH_LONG).show();
                }
                verifyCode(pin);*/

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
                    Intent intent = new Intent(VerifyActivtiy.this, MainActivity.class);
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
            codeOTP = phoneAuthCredential.getSmsCode();
            if (codeOTP!=null){
                verifyCode(codeOTP);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(VerifyActivtiy.this);
            alertDialogBuilder.setMessage("Verification Failed !");
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    };
}
