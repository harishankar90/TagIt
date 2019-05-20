package com.hvdcreations.tagit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText ETname,ETphone,ETmail;
    Button register;

    String name,phone,mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ETname = findViewById(R.id.et_RegName);
        ETphone = findViewById(R.id.et_RegPhone);
        ETmail = findViewById(R.id.et_RegMail);

        register = findViewById(R.id.btn_register);

        name = ETname.getText().toString().trim();
        phone = ETphone.getText().toString().trim();
        mail = ETmail.getText().toString().trim();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegisterActivity.this,VerifyActivtiy.class);
                intent.putExtra("name",name);
                intent.putExtra("mobile",phone);
                intent.putExtra("mail",mail);
                startActivity(intent);

                Toast.makeText(RegisterActivity.this, name+phone+mail, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
