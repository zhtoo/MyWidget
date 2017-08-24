package com.hs.samplewidget;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hs.samplewidget.myView.CountDownView;
import com.hs.samplewidget.myView.DownloadView;

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
    private Button mReset;
    private boolean reset;
    private Runnable r;
    private Button mStop;
    private boolean download;
    private CountDownView mCountDownView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCountDownView = (CountDownView) findViewById(R.id.my_countdown);
        progress = (DownloadView) findViewById(R.id.my_circleprogress);
        mButton = (Button) findViewById(R.id.my_button);
        mStop = (Button) findViewById(R.id.my_stop);
        mReset = (Button) findViewById(R.id.my_reset);
        r = new Runnable() {
            @Override
            public void run() {
                progress.setProgress(mProgress);//设置当前进度
                if (mProgress < 100) {
                    mProgress++;
                    setProgresss();
                }
            }
        };
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  //模拟下载过程
                reset = true;
                download = true;
                mProgress = 15;
                setProgresss();*/
                mCountDownView.setTime("1");
            }
        });

        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset = false;
                handler.removeCallbacks(r);
                progress.initProgress();
            }
        });

        mStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(download){
                    handler.removeCallbacks(r);
                    mStop.setText("开始");
                    progress.setProgress(mProgress);
                    download = false;
                }else {
                    reset = true;
                    download = true;
                    mStop.setText("暂停");
                    setProgresss();
                }
            }
        });

    }

    private void setProgresss() {
        if (reset) {
            handler.postDelayed(r, (int) (Math.random() * 100 + 50d));//随机选择延迟时间，范围50-150ms
        }
    }
}
