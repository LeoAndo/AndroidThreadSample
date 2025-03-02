package com.example.androidthreadsample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.Executor;

public class MainExecutorActivity extends AppCompatActivity {
    private static final String TAG = "MainExecutorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_executor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Runnable beeper = () -> {
            // ダミーで重たい処理を行う - START
            for (var j = 0; j < 1000000; j++) {
                Math.pow(j, 2);
            }
            // ダミーで重たい処理を行う - END
            Log.d(TAG, "beep thread name: " + Thread.currentThread().getName());
        };

        // main threadを取得しているため、新しくスレッドを生成することはない
        var mainExecutor = ContextCompat.getMainExecutor(this);

        for (var i = 0; i < 10; i++) {
            mainExecutor.execute(beeper);
        }

        //  mainExecutor.shutdown(); // shutdownは存在しない
    }

    static void start(Context context) {
        var starter = new Intent(context, MainExecutorActivity.class);
        context.startActivity(starter);
    }
}