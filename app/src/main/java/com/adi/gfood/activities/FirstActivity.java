package com.adi.gfood.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.adi.gfood.R;

public class FirstActivity extends AppCompatActivity {

    public static final String SHARED_PREF_NAME = "com.adi.gfood.Main";
    public static final String HAS_BOARDED_KEY = "has_boarded";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firest_screen);

        findViewById(R.id.logo_first_screen).animate().translationYBy(-100f).setDuration(2000).start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sh = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

                boolean hasBoarded = sh.getBoolean(HAS_BOARDED_KEY, false);

                if (hasBoarded){
                    startActivity(new Intent(FirstActivity.this, LoginActivity.class));
                    FirstActivity.this.finish();
                }else {
                    startActivity(new Intent(FirstActivity.this, OnBoardActivity.class));
                    FirstActivity.this.finish();
                }

            }
        }, 2500);

    }

}