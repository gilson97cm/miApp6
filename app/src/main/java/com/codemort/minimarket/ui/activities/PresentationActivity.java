package com.codemort.minimarket.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.codemort.minimarket.R;

public class PresentationActivity extends AppCompatActivity {
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("dataLogin", Context.MODE_PRIVATE);
                Boolean session = preferences.getBoolean("session",false);
               String name = preferences.getString("name",null);

                if(session){
                    Intent intent = new Intent(PresentationActivity.this, Home.class);
                    intent.putExtra("name",name);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(PresentationActivity.this, Login.class);
                    startActivity(intent);
                    finish();
                }

            }
        },1000);
    }
}
