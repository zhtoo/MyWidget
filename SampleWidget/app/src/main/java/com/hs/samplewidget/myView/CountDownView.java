package com.hs.samplewidget.myView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hs.samplewidget.R;

/**
 * HH:MM:SS
 * 作者：zhanghaitao on 2017/8/23 16:59
 * 邮箱：820159571@qq.com
 */

public class CountDownView extends View {

    private int mTextColor;
    private float mTextSize;
    private boolean startRun;
    private long mTime;
    private Context context;
    private Paint mTextPaint;

    private String time;
    private long oldTime;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            long newTime = System.currentTimeMillis();
            if (mTime > 0) {
                //时间自减
                mTime--;
                //重绘，在此会重新调用onDraw（）
                postInvalidate();
                Log.d("onDraw", "间隔" + (System.currentTimeMillis() - oldTime));
                oldTime = newTime;
                mHandler.sendEmptyMessageDelayed(MAG_WHAT, DELAY_TIME);
            }
        }
    };
    private int MAG_WHAT = 66;
    private long DELAY_TIME = 1000;

    public CountDownView(Context context) {
        this(context, null);
        initCofig(context, null);
    }

    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCofig(context, attrs);
    }

    private void initCofig(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CountDownView);

        time = typedArray.getString(R.styleable.CountDownView_time);
        String number = getNumber(time);
        Long aLong = Long.parseLong(number);
        mTime = aLong == null ? 1000000 : aLong;

        startRun = typedArray.getBoolean(R.styleable.CountDownView_start, false);
        mTextSize = typedArray.getDimension(R.styleable.CountDownView_size, 40);
        mTextColor = typedArray.getColor(R.styleable.CountDownView_color, Color.WHITE);
        typedArray.recycle();
        initTool();
    }

    private void initTool() {
        //文字画笔
        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setStrokeWidth(0);
        mTextPaint.setTypeface(Typeface.DEFAULT);

        oldTime = System.currentTimeMillis();
        mHandler.sendEmptyMessageDelayed(MAG_WHAT, DELAY_TIME);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            //默认wrap_content为300px
            setMeasuredDimension(200, 60);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(80, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, 8);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long startTime = System.currentTimeMillis();

        int center = getWidth() / 2;
        String time = getTimeShort(mTime);
        canvas.drawText(time,//待绘制的文字
                center - getFontWidth(mTextPaint, time) / 2,
                getFontHeight(mTextPaint),
                mTextPaint);
       /*
       //这种方法会有十几毫秒的偏差,使用 mHandler.sendEmptyMessageDelayed()的误差在几毫秒内。
       //待解决问题：
       //run()方法会在100ms内重新执行
       //解决方向--->
       //TimerTask开启的线程与UI线程并列
       //可能原因：TimerTask中的run()方法，也被UI线程调用,所以导致在较短时间内执行两次
       if (mTime > 0) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    long newTime = System.currentTimeMillis();
                    if (newTime - oldTime > 500) {
                        Log.d("onDraw", "间隔" + (System.currentTimeMillis() - oldTime));
                        oldTime = newTime;
                        mTime -= 1;
                        postInvalidate();
                    }
                }
            }, 1000);//延迟1s
        }
        */
    }

    /**
     * 设置时间
     *
     * @param time
     */
    public void setTime(String time) {
        //时间不能小于0
        Long aLong = Long.parseLong(getNumber(time));
        if (aLong < 0) {
            throw new IllegalArgumentException("时间不能小于0！");
        } else {
            //清空消息
            mHandler.removeMessages(MAG_WHAT);
            //将时间重新复制
            this.mTime = aLong;
            //重绘
            postInvalidate();
            //发送延迟消息
            mHandler.sendEmptyMessageDelayed(MAG_WHAT, DELAY_TIME);
        }
    }

    public String getTime() {
        return time;
    }

    /**
     * 获取时间 小时:分;秒 HH:mm:ss
     *
     * @return
     */
    public static String getTimeShort(long time) {
        long hourTime = time / 3600;
        long minuteTime = (time % 3600) / 60;
        long secondTime = time % 60;
        String hour = hourTime < 10 ? ("0" + hourTime) : String.valueOf(hourTime);
        String minute = minuteTime < 10 ? ("0" + minuteTime) : String.valueOf(minuteTime);
        String second = secondTime < 10 ? ("0" + secondTime) : String.valueOf(secondTime);
        return hour + ":" + minute + ":" + second;
    }

    /**
     * 返回指定的文字宽度
     *
     * @param paint
     * @param text
     * @return
     */
    public float getFontWidth(Paint paint, String text) {
        return paint.measureText(text);
    }

    /**
     * @return 返回指定的文字高度
     */
    public float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        //文字基准线的下部距离-文字基准线的上部距离 = 文字高度
        return fm.descent - fm.ascent;
    }

    /**
     * 获取到字符串里面的数字
     * @param str
     * @return
     */
    private static String getNumber(String str) {
        String str2 = "0";
        if (!TextUtils.isEmpty(str)) {
            str = str.trim();
            str2 = "";
            for (int i = 0; i < str.length(); i++) {
                if ((str.charAt(i) >= 48 && str.charAt(i) <= 57) || str.charAt(i) == '.') {
                    str2 += str.charAt(i);
                }
            }
        }
        return TextUtils.isEmpty(str2) ? "0" : str2.trim();
    }

}
