package com.hs.samplewidget.activity.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hs.samplewidget.R;
import com.hs.samplewidget.myView.Download2View;

/**
 * 作者：zhanghaitao on 2017/11/14 16:54
 * 邮箱：820159571@qq.com
 *
 * @describe:
 */

public class TestActivity extends AppCompatActivity {

    private Download2View download2View;

    private Runnable r;
    private Handler handler = new Handler();
    private int mProgress = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        download2View = (Download2View) findViewById(R.id.download2_view);

        r = new Runnable() {
            @Override
            public void run() {
                download2View.setProgress(mProgress);//设置当前进度
                if (mProgress < 100) {
                    mProgress++;
                    setProgresss();
                }
            }
        };

        setProgresss();

    }

    private void setProgresss() {
        handler.postDelayed(r,50/* (int) (Math.random() * 100 + 50d)*/);//随机选择延迟时间，范围50-150ms
    }

}
