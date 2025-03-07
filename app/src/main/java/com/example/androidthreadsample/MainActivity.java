package com.example.androidthreadsample;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btn_1).setOnClickListener(v -> {
            NewSingleThreadExecutorDemo1Activity.start(this);
        });

        findViewById(R.id.btn_2).setOnClickListener(v -> {
            // ここに処理を記述
            NewSingleThreadExecutorDemo2Activity.start(this);
        });

        findViewById(R.id.btn_3).setOnClickListener(v -> {
            // ここに処理を記述
            ScheduledExecutorServiceDemo1Activity.start(this);
        });

        findViewById(R.id.btn_4).setOnClickListener(v -> {
            // ここに処理を記述
            ScheduledExecutorServiceDemo2Activity.start(this);
        });

        findViewById(R.id.btn_5).setOnClickListener(v -> {
            // ここに処理を記述
            ThreadPoolExecutorDemo1Activity.start(this);
        });

        findViewById(R.id.btn_6).setOnClickListener(v -> {
            // ここに処理を記述
            ThreadPoolExecutorDemo2Activity.start(this);
        });

        findViewById(R.id.btn_7).setOnClickListener(v -> {
            // ここに処理を記述
            NewFixedThreadPoolActivity.start(this);
        });

        findViewById(R.id.btn_8).setOnClickListener(v -> {
            // ここに処理を記述
            MainExecutorActivity.start(this);
        });
    }
}