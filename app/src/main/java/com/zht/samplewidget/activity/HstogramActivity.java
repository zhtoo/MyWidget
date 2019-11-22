package com.zht.samplewidget.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hs.samplewidget.R;
import com.zht.samplewidget.myView.chart.HstogramView;

/**
 * 作者：zhanghaitao on 2017/11/6 10:33
 * 邮箱：820159571@qq.com
 *
 * @describe:
 */

public class HstogramActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hstogram);
        HstogramView view = (HstogramView) findViewById(R.id.my_hstogram);
        view.setOnItemClickListener(new HstogramView.onItemClickListener() {
            @Override
            public void onItemClick(int postion) {
               // Toast.makeText(HstogramActivity.this, "点击了条目" + postion, Toast.LENGTH_SHORT).show();
            }
        });
        view.setDate(new String[]{"10-01", "10-02", "10-03", "10-04", "10-05", "10-06", "10-07"});
        view.setValues(new int[]{10, 20, 30, 40, 99, 58,65});
    }
}
