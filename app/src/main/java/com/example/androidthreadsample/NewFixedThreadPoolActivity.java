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

public class NewFixedThreadPoolActivity extends AppCompatActivity {
    private static final String TAG = "NewFixedThreadPoolActivity";
    /**
     * 4個のスレッドを持つスレッドプールを作成する.
     * android Frameworkやjetpackライブラリの内部実装を参考にすると、3, 4の設定が多いのでそれに合わせる
     */
    @NonNull
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_fixed_thread_pool);
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

    static void start(Context context) {
        Intent starter = new Intent(context, NewFixedThreadPoolActivity.class);
        context.startActivity(starter);
    }
}
