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
import java.util.concurrent.TimeUnit;

/**
 * bad example
 */
public class ScheduledExecutorServiceDemo2Activity extends AppCompatActivity {
    private static final String TAG = "ScheduledExecutorServiceDemo2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scheduled_executor_service_demo2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        var scheduler = Executors.newScheduledThreadPool(5);
        for (var i = 0; i < 10; i++) {
            Runnable beeper = () -> {
                // ダミーで重たい処理を行う - START
                for (var j = 0; j < 1000000; j++) {
                    Math.pow(j, 2);
                }
                // ダミーで重たい処理を行う - END
                Log.d(TAG, "beep thread name: " + Thread.currentThread().getName());
            };
            var beeperHandle = scheduler.scheduleWithFixedDelay(beeper, 3, 5, TimeUnit.SECONDS);
            Runnable canceller = () -> {
                Log.d(TAG, "cancel!");
                beeperHandle.cancel(false);
            };
            scheduler.schedule(canceller, 20, TimeUnit.SECONDS); // 20秒で定期実行を終了する
        }
    }

    /**
     * ここでshutdown()をしていないため、画面を閉じた後も作成したスレッドが実行中のままとなっていることを確認。
     * u0_a232      26494 26522   326   16664056 145468 0                   0 S pool-2-thread-1
     * u0_a232      26494 26523   326   16664056 145468 0                   0 S pool-2-thread-2
     * u0_a232      26494 26524   326   16664056 145468 0                   0 S pool-2-thread-3
     * u0_a232      26494 26525   326   16664056 145468 0                   0 S pool-2-thread-4
     * u0_a232      26494 26526   326   16664056 145468 0                   0 S pool-2-thread-5
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: IN");
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ScheduledExecutorServiceDemo2Activity.class);
        context.startActivity(starter);
    }
}