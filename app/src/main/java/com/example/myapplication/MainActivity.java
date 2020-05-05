package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Jeffery";

    @SuppressLint("HandlerLeak")
    private Handler mUIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "mUIHandler----handleMessage----" + Thread.currentThread().getName() +"---"+ new Date().toLocaleString());
        }
    };
    private Handler handlerJ;
    private OkHttpClient client;
    private Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
//        handleThreadDeom();

//        AsyncTask<Void,Void,Void> asyncTask;
//        asyncTask.execute();

        @SuppressLint("HandlerLeak")
        Handler handler=new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(@NonNull Message msg) {
                System.out.println("111111111");
                return false;
            }
        }){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                System.out.println("22222222222");
            }
        };
        handler.sendEmptyMessage(0);
        asyncTaskDeom();
        initOkhttp();
    }

    private void asycOkhttp(){
        //这才是异步的，晕死
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: ----"+Thread.currentThread().getName());
                System.out.println(response.body().string());
            }
        });
    }

    private void initOkhttp() {
        client = new OkHttpClient();
        request = new Request.Builder()
                .url("http://www.baidu.com")
                .build();
        handlerJ.post(() -> {
            Response response = null;
            try {
                //这尼玛的是同步的请求
                response = client.newCall(request).execute();
                String string = response.body().string();
                Log.i(TAG, "initOkhttp: -----data---\n"+string);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void asyncTaskDeom() {
        new ExecutorsDemo().executorTask();
    }

    private void initView() {
        findViewById(R.id.btIS).setOnClickListener(v->startActivity(new Intent(MainActivity.this,IntentServiceActivity.class)));
        findViewById(R.id.bt2).setOnClickListener(v->{
            //同一线程Looper.prepare只能执行一次
//            Looper.prepareMainLooper();

//            initOkhttp();
            asycOkhttp();

        });

        HandlerThread handlerThread = new HandlerThread("Thread_j");
        handlerThread.start();

        handlerJ = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

            }
        };
    }


    /**
     * 该callback运行于子线程
     */
    class ChildCallback implements Handler.Callback {
        @Override
        public boolean handleMessage(Message msg) {
            //在子线程中进行网络请求
            Log.i(TAG, "ChildCallback----handleMessage: ----" + Thread.currentThread().getName());
            SystemClock.sleep(1000);
            //通知主线程去更新UI
            mUIHandler.sendEmptyMessage(msg.what + 10);
            return false;
        }
    }

    private void handleThreadDeom() {
        //创建异步HandlerThread
        HandlerThread handlerThread = new HandlerThread("downloadImage");
        //必须先开启线程
        handlerThread.start();
        //子线程Handler
        Handler childHandler = new Handler(handlerThread.getLooper(), new ChildCallback());
        for (int i = 0; i < 7; i++) {
            //每个1秒去更新图片
            childHandler.sendEmptyMessageDelayed(i, 1000 * i);
        }
    }
}
