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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * good example
 */
public class NewSingleThreadExecutorDemo1Activity extends AppCompatActivity {
    private static final String TAG = "NewSingleThreadExecutorDemo1Activity";
    /**
     * 1つのスレッドを持つスレッドプールを作成する
     */
    @NonNull
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

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

        Runnable beeper = () -> Log.d(TAG, "beep thread name: " + Thread.currentThread().getName());
        for (var i = 0; i < 10; i++) {
            executorService.execute(beeper);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: IN");
        // ここでshutdown()を明示的に呼び出さないと、
        // ExecutorServiceで作成したスレッドはアクティビティが終了しても実行し続ける。
        // これにより、不要なシステムリソースの消費やメモリリークが発生する可能性がある。
        executorService.shutdown();
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, NewSingleThreadExecutorDemo1Activity.class);
        context.startActivity(starter);
    }
}