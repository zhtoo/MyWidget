package com.hs.samplewidget.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hs.samplewidget.R;
import com.hs.samplewidget.myView.Download2View;
import com.hs.samplewidget.myView.DownloadView;

/**
 * 作者：zhanghaitao on 2017/9/28 10:42
 * 邮箱：820159571@qq.com
 *
 * @describe:
 */

public class DownloadAvtivity extends AppCompatActivity {

    private DownloadView downloadView;
    private Button mButton;
    private Button mStop;
    private Button mReset;
    private Runnable r;
    private Handler handler = new Handler();

    private boolean reset;
    private boolean download;
    private int mProgress;
    private Download2View download2View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        downloadView = (DownloadView) findViewById(R.id.download_view);
        mButton = (Button) findViewById(R.id.download_button);
        mStop = (Button) findViewById(R.id.stop_button);
        mReset = (Button) findViewById(R.id.reset_button);

        download2View = (Download2View) findViewById(R.id.download2_view);


        r = new Runnable() {
            @Override
            public void run() {
                downloadView.setProgress(mProgress);//设置当前进度
                download2View.setProgress(mProgress);//设置当前进度
                if (mProgress < 100) {
                    mProgress++;
                    setProgresss();
                }
            }
        };
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //模拟下载过程
                reset = true;
                download = true;
                mProgress = (int) (Math.random() * 30 + 1d);
                setProgresss();
            }
        });

        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset = false;
                mProgress = 0 ;
                handler.removeCallbacks(r);
                downloadView.initProgress();
                download2View.initProgress();
            }
        });

        mStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(download){
                    handler.removeCallbacks(r);
                    mStop.setText("开始");
                    downloadView.setProgress(mProgress);
                    download2View.setProgress(mProgress);
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
