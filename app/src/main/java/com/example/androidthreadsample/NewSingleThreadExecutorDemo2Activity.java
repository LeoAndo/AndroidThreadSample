package com.example.androidthreadsample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.Executors;

/**
 * bad example
 */
public class NewSingleThreadExecutorDemo2Activity extends AppCompatActivity {
    private static final String TAG = "NewSingleThreadExecutorDemo2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_single_thread_executor_demo1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        var executorService = Executors.newSingleThreadExecutor(); // 1つのスレッドを持つスレッドプールを作成する

        Runnable beeper = () -> Log.d(TAG, "beep thread name: " + Thread.currentThread().getName());
        for (var i = 0; i < 10; i++) {
            executorService.execute(beeper);
        }
    }

    /**
     * ここでshutdown()をしていないため、画面を閉じた後も作成したスレッドが実行中のままとなっていることを確認。
     * u0_a232      26348 26434   326   16455892 146548 0                   0 S pool-4-thread-1
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: IN");
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, NewSingleThreadExecutorDemo2Activity.class);
        context.startActivity(starter);
    }
}