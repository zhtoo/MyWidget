package com.hs.samplewidget;

import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hs.samplewidget.myView.DownloadView;
import com.hs.samplewidget.view.CircleProgressBar;

/**
 * http://blog.csdn.net/lmj623565791/article/details/41967509
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private DownloadView progress;
    private int mProgress;
    private Button mButton;
    private long startTime;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress = (DownloadView) findViewById(R.id.my_circleprogress);
        mButton = (Button) findViewById(R.id.my_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //模拟下载过程
                mProgress = 5;
                setProgresss();
            }
        });

    }

    private void setProgresss() {
        handler.postDelayed(new Runnable() {
            public void run() {
                progress.setProgress(mProgress);//设置当前进度
                if (mProgress < 100) {
                    mProgress++;
                    setProgresss();
                }
            }
        }, (int) (Math.random() * 100 + 50d));//随机选择延迟时间，范围50-150ms
    }
}
