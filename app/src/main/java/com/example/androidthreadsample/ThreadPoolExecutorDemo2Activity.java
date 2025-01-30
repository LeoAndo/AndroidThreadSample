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

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * bad example.
 */
public class ThreadPoolExecutorDemo2Activity extends AppCompatActivity {
    private static final String TAG = "ThreadPoolExecutorDemo2Activity";


    // java.util.concurrent.ScheduledThreadPoolExecutor.DEFAULT_KEEPALIVE_MILLISを参考にした。
    private static final long DEFAULT_KEEPALIVE_MILLIS = 10L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thread_pool_executor_demo2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        var threadPoolExecutor = new ThreadPoolExecutor(
                5,
                Integer.MAX_VALUE, // maximum pool size
                DEFAULT_KEEPALIVE_MILLIS, // keep-alive time
                TimeUnit.MILLISECONDS, // time unit for keep-alive
                new LinkedBlockingQueue<>() // work queue
        );

        for (var i = 0; i < 10; i++) {
            Runnable beeper = () -> Log.d(TAG, "beep thread name: " + Thread.currentThread().getName());
            // 即時実行する
            threadPoolExecutor.execute(beeper);
        }
    }

    /**
     * ここでshutdown()をしていないため、画面を閉じた後も作成したスレッドが実行中のままとなっていることを確認。
     * u0_a232      26975 27003   326   16863420 145020 0                   0 S pool-2-thread-1
     * u0_a232      26975 27004   326   16863420 145020 0                   0 S pool-2-thread-2
     * u0_a232      26975 27005   326   16863420 145020 0                   0 S pool-2-thread-3
     * u0_a232      26975 27006   326   16863420 145020 0                   0 S pool-2-thread-4
     * u0_a232      26975 27007   326   16863420 145020 0                   0 S pool-2-thread-5
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: IN");
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ThreadPoolExecutorDemo2Activity.class);
        context.startActivity(starter);
    }
}