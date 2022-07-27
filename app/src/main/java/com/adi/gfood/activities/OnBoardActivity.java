package com.adi.gfood.activities;

import static com.adi.gfood.activities.FirstActivity.HAS_BOARDED_KEY;
import static com.adi.gfood.activities.FirstActivity.SHARED_PREF_NAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.adi.gfood.R;
import com.adi.gfood.adapters.OnBoardAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class OnBoardActivity extends AppCompatActivity {

    ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);

        saveToSharedPref();

        viewPager = findViewById(R.id.viewPager_onboard);

        OnBoardAdapter adapter = new OnBoardAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);

        Button finishBtn = findViewById(R.id.finishBtn);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position == 2){
                    finishBtn.setVisibility(View.VISIBLE);
                }else{
                    finishBtn.setVisibility(View.GONE);
                }

            }
        });

        finishBtn.setOnClickListener(v -> gotoMain());

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(
                tabLayout,
                viewPager,
                (tab, position) -> {}
        ).attach();

        TextView skipBtn = findViewById(R.id.skipBtn);
        skipBtn.setOnClickListener(v -> gotoMain());
    }

    private void saveToSharedPref() {
        SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(HAS_BOARDED_KEY, true);
        editor.apply();
    }

    public void gotoMain(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}