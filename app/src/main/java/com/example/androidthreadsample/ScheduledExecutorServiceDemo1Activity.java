package com.example.androidthreadsample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * good example
 */
public class ScheduledExecutorServiceDemo1Activity extends AppCompatActivity {
    private static final String TAG = "ScheduledExecutorServiceDemo1Activity";
    @NonNull
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scheduled_executor_service_demo1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ScheduledExecutorServiceは、
        // スレッドプール内の空いているスレッドを使用して、登録されたタスク（この場合はbeeper）を処理する。
        // スレッドプール内のスレッドが空いていない場合は、スレッドが空くまで待機する。
        for (var i = 0; i < 10; i++) {
            Runnable beeper = () -> {
                Log.d(TAG, "beep thread name: " + Thread.currentThread().getName());
                // ここでUIの処理を行なっても問題は発生しなかった
                findViewById(R.id.main).setVisibility(View.INVISIBLE);
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
     * 定期実行が完了前に、画面を閉じると、beeperタスクは呼ばれないが、
     * 定期実行完了後にcancellerが呼ばれ、その後直ちにスレッドは終了する
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: IN");
        // ここでshutdown()を明示的に呼び出さないと、
        // ScheduledExecutorServiceのスレッドはアクティビティが終了しても実行し続ける。
        // これにより、不要なシステムリソースの消費やメモリリークが発生する可能性がある。
        scheduler.shutdown();
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ScheduledExecutorServiceDemo1Activity.class);
        context.startActivity(starter);
    }
}