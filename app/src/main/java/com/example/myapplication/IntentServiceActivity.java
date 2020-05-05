package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

public class IntentServiceActivity extends AppCompatActivity implements MyIntentService.UpdateUI{

    private static final String TAG = "IntentServiceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_service);
        Intent intent = new Intent(this,MyIntentService.class);
        for (int i=0;i<2;i++) {//循环启动任务
            intent.putExtra(MyIntentService.INDEX_FLAG,i);
            startService(intent);
        }
        MyIntentService.setUpdateUI(this);
    }

    private static ImageView imageView;
    @SuppressLint("HandlerLeak")
    private static final Handler mUIHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, this.getClass().getSimpleName()+"----handleMessage: -----"+Thread.currentThread().getName());
        }
    };

    //必须通过Handler去更新，该方法为异步方法，不可更新UI
    @Override
    public void updateUI(Message message) {
        mUIHandler.sendMessageDelayed(message,message.what * 1000);
    }
}
