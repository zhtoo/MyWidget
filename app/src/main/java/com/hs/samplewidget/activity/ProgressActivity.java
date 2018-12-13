package com.hs.samplewidget.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.hs.samplewidget.R;
import com.hs.samplewidget.myView.MyProgressView;
import com.hs.samplewidget.myView.PieChartView;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：zhanghaitao on 2017/11/28 10:05
 * 邮箱：820159571@qq.com
 *
 * @describe:
 */

public class ProgressActivity extends AppCompatActivity {

    private PieChartView pieChartView;
    private List<PieChartView.Params> mParams;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        pieChartView = (PieChartView) findViewById(R.id.m_PieChart);

        pieChartView.setItemClickListener(new PieChartView.ItemClickListener() {
            @Override
            public void onPieChartItemClick(boolean isSelected,int position) {
                String des = mParams.get(position).getDes();
              //  Log.e("des", "des: " + des);
            }
        });
    }

    public void fillData(View view) {
        mParams = new ArrayList<>();
        //模拟数据
        if (mParams.size() == 0) {
            for (int i = 0; i < 4; i++) {
                PieChartView.Params params = new PieChartView.Params();
                params.setDes("我是" + i);
                params.setScale((float) Math.random() * 1000F + 10);
                int red = (int) (20 + Math.random() * 200);
                int green = (int) (20 + Math.random() * 200);
                int bule = (int) (20 + Math.random() * 200);
                int color = Color.argb(255, red, green, bule);
                params.setDrawColor(color);
                mParams.add(params);
            }
        }

        pieChartView.setParams(mParams);
    }
}
