package com.vince.countdown;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DemoCountDownView countDownView = findViewById(R.id.count_down);

        countDownView.startCountDown(3600 * 1000 * 42, 1000);

    }

}
