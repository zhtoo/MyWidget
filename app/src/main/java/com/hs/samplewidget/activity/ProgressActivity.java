package com.hs.samplewidget.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hs.samplewidget.R;
import com.hs.samplewidget.myView.MyProgressView;

/**
 * 作者：zhanghaitao on 2017/11/28 10:05
 * 邮箱：820159571@qq.com
 *
 * @describe:
 */

public class ProgressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        MyProgressView progressView = (MyProgressView) findViewById(R.id.my_progress_view1);
//        progressView.setmProgressMax(10f);
//        progressView.setmProgress(9f);

    }

}
