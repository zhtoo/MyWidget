package com.hs.samplewidget.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hs.samplewidget.R;
import com.hs.samplewidget.myView.HstogramView;

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
              //  Toast.makeText(HstogramActivity.this, "点击了条目" + postion, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
