package com.eatd.baou.eatd_hackathon;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    // 參照生命週期
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(WelcomeActivity.this, MapsActivity.class));
                finish();
            }
        }, 2000L);
    }
}
