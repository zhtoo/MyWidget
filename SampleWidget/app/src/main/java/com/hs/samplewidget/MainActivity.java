package com.hs.samplewidget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hs.samplewidget.view.CircleProgressBar;

/**
 * http://blog.csdn.net/lmj623565791/article/details/41967509
 */
public class MainActivity extends AppCompatActivity {

    private CircleProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*progress = (CircleProgressBar) findViewById(R.id.my_circleprogress);
        progress.setProgress(80);*/
    }
}
