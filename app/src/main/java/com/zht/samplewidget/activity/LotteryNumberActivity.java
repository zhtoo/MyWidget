package com.zht.samplewidget.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zht.samplewidget.R;
import com.zht.samplewidget.myView.LotteryNumberView;

/**
 * Created by zhanghaitao on 2018/3/27.
 */

public class LotteryNumberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_number);
        LotteryNumberView view = (LotteryNumberView) findViewById(R.id.mLotteryNumber);
        LinearLayout item = (LinearLayout) findViewById(R.id.item);

        item.removeAllViews();
        for (int i = 0 ; i< 3;i++){
            LotteryNumberView numberView = new LotteryNumberView(this);
            LinearLayout.LayoutParams layoutParams
                    = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            numberView.setLayoutParams(layoutParams);
            numberView.setRedNuber(new String[]{"02", "12", "03", "15", "23", "02", "12", "03", "15", "23", "02", "12", "03", "15", "23"});
            numberView.setBuleNuber(new String[]{"01","12","01","12","01","12","01","12","01","12"});
            item.addView(numberView,i);
        }

    }
}
