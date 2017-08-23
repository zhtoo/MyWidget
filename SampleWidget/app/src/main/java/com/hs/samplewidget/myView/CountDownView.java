package com.hs.samplewidget.myView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.hs.samplewidget.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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
    private ValueAnimator mAnimator;
    private float mCurrentTime;

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

        String stringTime = typedArray.getString(R.styleable.CountDownView_time);
        String number = getNumber(stringTime);
        Long aLong = Long.parseLong(number);
        mTime = aLong == null ?1000000:aLong;

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

        mCurrentTime = mTime;

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
            setMeasuredDimension(200,60);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(80, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, 8);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int center = getWidth() / 2;
        String time = getTimeShort(mTime);
        canvas.drawText(time,//待绘制的文字
                center - getFontWidth(mTextPaint, time) / 2,
                getFontHeight(mTextPaint),
                mTextPaint);
        if(mTime >0){
            mTime -= 1000;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    postInvalidate();
                }
            }, 1000);
        }
    }

    /**
     * 设置时间
     *
     * @param time
     */
    public void setTime(String time) {
        //进度值不能小于0

        Long aLong = Long.parseLong(getNumber(time));
        if (aLong < 0) {
            throw new IllegalArgumentException("进度不能小于0！");
        } else  {
            this.mTime = aLong;
        }
        postInvalidate();
    }



    /**
     * 获取时间 小时:分;秒 HH:mm:ss
     *
     * @return
     */
    public static String getTimeShort(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date currentTime = new Date(time);
        String dateString = formatter.format(currentTime);
        return dateString;
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
                if ((str.charAt(i) >= 48 && str.charAt(i) <= 57)||str.charAt(i) == '.') {
                    str2 += str.charAt(i);
                }
            }
        }
        return  TextUtils.isEmpty(str2)?"0":str2.trim();
    }


}
