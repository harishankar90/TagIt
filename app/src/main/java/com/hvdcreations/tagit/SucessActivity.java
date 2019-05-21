package com.hvdcreations.tagit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;

public class SucessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucess);

        try {
            InputStream stream = null;
            stream = getAssets().open("piggy.gif");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
