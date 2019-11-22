package com.zht.samplewidget.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zht.samplewidget.R;
import com.zht.samplewidget.myView.chart.LineChartView;

/**
 * Created by ZhangHaitao on 2019/11/22
 */
public class LineChartActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private LineChartView lineChartView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        Log.e(TAG, "setContentView");
        setContentView(R.layout.activity_line_chart);
        Log.e(TAG, "findViewById");
        lineChartView = findViewById(R.id.lineChartView);



        /**
         * setContentView
         * initCofig
         * findViewById
         * setParams
         * onMeasure
         * onMeasure
         * onDraw
         */
        handler.sendEmptyMessageDelayed(1000 ,2000);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            lineChartView.setParams(null);
        }
    };



}
