package com.zht.samplewidget.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.zht.samplewidget.R;
import com.zht.samplewidget.myView.chart.LineChartParams;
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


        // handler.sendEmptyMessageDelayed(1000 ,2000);
    }
//
//    Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//
//        }
//    };

    String[] oneDate = {"12-01", "12-02", "12-03", "12-04", "12-05", "12-06", "12-07"};
    Integer[] oneData1 = {0, 0, 0, 0, 0, 0, 0};
    Integer[] oneData2 = {0, 0, 0, 0, 0, 0, 0};
    Integer[] oneData3 = {0, 0, 0, 0, 0, 0, 0};

    public void one(View view) {
//        for (int i = 0; i < oneDate.length; i++) {
//            oneData1[i]= (int) (Math.random()*500);
//            oneData2[i]= (int) (Math.random()*500);
//            oneData3[i]= (int) (Math.random()*500);
//        }

        createData(oneDate, oneData1, oneData2, oneData3);
    }

    String[] twoDate = {
            "12-01", "12-02", "12-03", "12-04", "12-05", "12-06", "12-07",
            "12-05", "12-06", "12-07", "12-08", "12-09", "12-06", "12-07",
            "12-01", "12-02", "12-03", "12-04", "12-05", "12-06", "12-07",
            "12-01", "12-02", "12-03", "12-04", "12-05", "12-06", "12-07",
            "12-01", "12-02"};
    Integer[] twoData1 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    Integer[] twoData2 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    Integer[] twoData3 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public void two(View view) {
//        for (int i = 0; i < twoDate.length; i++) {
//            twoData1[i]= (int) (Math.random()*500);
//            twoData2[i]= (int) (Math.random()*500);
//            twoData3[i]= (int) (Math.random()*500);
//        }

        createData(twoDate, twoData1, twoData2, twoData3);
    }

    String[] thirdDate = {"2018-11", "2018-12", "2019-01", "2019-02"};
    Integer[] thirdData1 = {0, 0, 0, 0};
    Integer[] thirdData2 = {0, 0, 0, 0};
    Integer[] thirdData3 = {0, 0, 0, 0};

    public void third(View view) {
//        for (int i = 0; i < thirdDate.length; i++) {
//            thirdData1[i]= (int) (Math.random()*500);
//            thirdData2[i]= (int) (Math.random()*500);
//            thirdData3[i]= (int) (Math.random()*500);
//        }
        createData(thirdDate, thirdData1, thirdData2, thirdData3);
    }


    int[] colors = {0xFFFFB000, 0xFF3379F8, 0xFFFC4A5B};

    private void createData(
            String[] data, Integer[]... integers
    ) {
        LineChartParams lineChartBean = new LineChartParams();
        lineChartBean.setyAxis(new LineChartParams.YAxisBean());
        lineChartBean.setxAxis(new LineChartParams.XAxisBean());
        lineChartBean.setSeries(new ArrayList<LineChartParams.SeriesBean>());

        LineChartParams.XAxisBean xAxisBean = lineChartBean.getxAxis();
        xAxisBean.setDrawColor(0xFF555555);
        xAxisBean.setData(new ArrayList<String>());
        xAxisBean.getData().addAll(Arrays
                .asList(data));

        List<LineChartParams.SeriesBean> series = lineChartBean.getSeries();
        for (int i = 0; i < integers.length; i++) {
            LineChartParams.SeriesBean seriesBean = new LineChartParams.SeriesBean();
            seriesBean.setDrawColor(colors[i]);
            seriesBean.setData(Arrays.asList(integers[i]));
            series.add(seriesBean);
        }

        LineChartParams.YAxisBean yAxisBean = lineChartBean.getyAxis();
        yAxisBean.setDrawColor(0xFFCCCCCC);
        lineChartView.setParams(lineChartBean);
    }


}
