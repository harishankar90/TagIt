package com.hvdcreations.tagit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class SucessActivity extends AppCompatActivity {

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucess);

        try {
            GifDrawable planetgif = new GifDrawable( getResources(), R.drawable.compreso);

            final MediaController mc = new MediaController(this);
            mc.setMediaPlayer(planetgif);
            mc.show();

            new Handler().postDelayed(new Runnable() {

                @SuppressLint("RestrictedApi")
                @Override
                public void run() {
                   Intent intent = new Intent(SucessActivity.this,MainActivity.class);
                   startActivity(intent);
                }
            }, 5500);


        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }
}
