package com.hs.samplewidget;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hs.samplewidget.myView.DownloadView;
import com.hs.samplewidget.view.CircleProgressBar;

/**
 * http://blog.csdn.net/lmj623565791/article/details/41967509
 */
public class MainActivity extends AppCompatActivity {


    private DownloadView progress;
    private int mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress = (DownloadView) findViewById(R.id.my_circleprogress);
        mProgress = 0;
        setProgresss();


    }

    private void setProgresss() {
        new Handler().postDelayed(new Runnable() {

            public void run() {
                progress.setProgress(mProgress);
                if(mProgress <100){
                    mProgress++;
                    setProgresss();
                }
            }
        }, 50);
    }
}
