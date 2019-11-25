package com.zht.samplewidget.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zht.samplewidget.R;
import com.zht.samplewidget.myView.chart.LineChartBean;
import com.zht.samplewidget.myView.chart.LineChartView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        LineChartBean   lineChartBean = new LineChartBean();
        lineChartBean.setyAxis(new LineChartBean.YAxisBean());
        lineChartBean.setxAxis(new LineChartBean.XAxisBean());
        lineChartBean.setSeries(new ArrayList<LineChartBean.SeriesBean>());

        LineChartBean.XAxisBean xAxisBean = lineChartBean.getxAxis();
        xAxisBean.setDrawColor(0xFF555555);
        xAxisBean.setData(new ArrayList<String>());
        xAxisBean.getData().addAll(Arrays
                .asList(new String[]{
                        "周一", "周二", "周三", "周四", "周五", "周六", "周日"
                }));

        List<LineChartBean.SeriesBean> series = lineChartBean.getSeries();
        LineChartBean.SeriesBean seriesBean1 = new LineChartBean.SeriesBean();
        seriesBean1.setDrawColor(0xFFFFB000);
        seriesBean1.setData(Arrays.asList(new Integer[]{
                150, 232, 201, 154, 190, 330, 410}));

        LineChartBean.SeriesBean seriesBean2 = new LineChartBean.SeriesBean();
        seriesBean2.setDrawColor(0xFF3379F8);
        seriesBean2.setData(Arrays.asList(new Integer[]{
                320, 332, 301, 334, 390, 330, 320}));

        LineChartBean.SeriesBean seriesBean3 = new LineChartBean.SeriesBean();
        seriesBean3.setDrawColor(0xFFFC4A5B);
        seriesBean3.setData(Arrays.asList(new Integer[]{
                820, 932, 901, 934, 1290, 1330, 1320}));
        series.add(seriesBean1);
        series.add(seriesBean2);
        series.add(seriesBean3);

        LineChartBean.YAxisBean yAxisBean = lineChartBean.getyAxis();
        yAxisBean.setDrawColor(0xFFCCCCCC);
        lineChartView.setParams(lineChartBean);

       // handler.sendEmptyMessageDelayed(1000 ,2000);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };



}
