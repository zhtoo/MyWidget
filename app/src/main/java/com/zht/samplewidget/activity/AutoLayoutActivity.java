package com.zht.samplewidget.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zht.samplewidget.R;
import com.zht.samplewidget.myView.AutoLayoutView;

/**
 * Created by ZhangHaitao on 2019/11/25
 */
public class AutoLayoutActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private AutoLayoutView autoLayoutView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_layout);


        autoLayoutView = findViewById(R.id.AutoLayoutView);



        // handler.sendEmptyMessageDelayed(1000 ,2000);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };



}
