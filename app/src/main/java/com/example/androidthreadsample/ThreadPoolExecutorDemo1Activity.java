package com.example.androidthreadsample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * good example.
 */
public class ThreadPoolExecutorDemo1Activity extends AppCompatActivity {
    private static final String TAG = "ThreadPoolExecutorDemo1Activity";

    /**
     * java.util.concurrent.ScheduledThreadPoolExecutor.DEFAULT_KEEPALIVE_MILLISを参考にした。
     */
    private static final long DEFAULT_KEEPALIVE_MILLIS = 10L;
    /**
     * コアプールサイズ (core pool size): スレッドプールが常に保持するスレッドの最小数です。タスクが追加されると、まずこの数までスレッドが作成されます。コアプールサイズに達していない場合、新しいタスクは新しいスレッドで実行されます。
     * 最大プールサイズ (maximum pool size): 重たい処理を入れても、コアプールサイズを超えることはなかった.java.util.concurrentのnewSingleThreadExecutorやnewScheduledThreadPoolの内部実装と合わせておく
     * キープアライブ時間 (keep-alive time): コアプールサイズを超えるアイドル状態のスレッドが終了するまでの時間。
     * ThreadPoolExecutorの各引数の設定値はjava.util.concurrent.Executors#newSingleThreadExecutor()と、java.util.concurrent.Executors.newScheduledThreadPool(int)の内部実装を参考にしている。
     */
    @NonNull
    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            5, // core pool size
            Integer.MAX_VALUE, // maximum pool size
            DEFAULT_KEEPALIVE_MILLIS, // keep-alive time
            TimeUnit.MILLISECONDS, // time unit for keep-alive
            new LinkedBlockingQueue<>() // work queue
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thread_pool_executor_demo1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        for (var i = 0; i < 100; i++) {
            Runnable beeper = () -> {
                // ダミーで重たい処理を行う - START
                for (var j = 0; j < 1000000; j++) {
                    Math.pow(j, 2);
                }
                // ダミーで重たい処理を行う - END
                Log.d(TAG, "beep thread name: " + Thread.currentThread().getName());
            };
            // 即時実行する
            threadPoolExecutor.execute(beeper);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: IN");
        threadPoolExecutor.shutdown();
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ThreadPoolExecutorDemo1Activity.class);
        context.startActivity(starter);
    }
}
