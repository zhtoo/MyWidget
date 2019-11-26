package com.zht.samplewidget.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zht.samplewidget.R;
import com.zht.samplewidget.myView.AutoLayoutView;
import com.zht.samplewidget.myView.HorizontalViewPager.VerticalViewPager;

/**
 * Created by ZhangHaitao on 2019/11/25
 */
public class AutoLayoutActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private AutoLayoutView autoLayoutView;

    String[] mTexts = {
            "王者内训师",
            "偶像外表实力内在",
            "于是就找解决方案，很显然最好的方案就是监听动画结束",
            "解决办法：主要利用",
            "士大夫",
            "古代中国对于社会上的士",
            "政治是绝大多数“士大夫”人生的第一要务",
            "经常缺勤"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_layout);

        autoLayoutView = findViewById(R.id.AutoLayoutView);

        autoLayoutView.removeAllViews();
        for (int i = 0; i < mTexts.length; i++) {
            ViewGroup.MarginLayoutParams lp
                    = new ViewGroup.MarginLayoutParams(
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                    dp2px(25));
            lp.leftMargin = dp2px(5);
            lp.rightMargin = dp2px(5);
            lp.topMargin = dp2px(5);
            lp.bottomMargin = dp2px(5);
            TextView textView = new TextView(this);
            textView.setLayoutParams(lp);
            textView.setBackgroundResource(R.drawable.zb_bg_hot_tags);
            textView.setPadding(dp2px(5), 0, dp2px(5), 0);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(0xFF555555);
            textView.setTextSize(14);
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setText(mTexts[i]);
            autoLayoutView.addView(textView);
        }
        autoLayoutView.requestLayout();
        // handler.sendEmptyMessageDelayed(1000 ,2000);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };

    public int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

}
