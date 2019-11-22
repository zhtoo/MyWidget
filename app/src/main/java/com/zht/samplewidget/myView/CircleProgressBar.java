package com.zht.samplewidget.myView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.hs.samplewidget.R;

/**
 * 作者：zhanghaitao on 2017/8/17 10:40
 * 邮箱：820159571@qq.com
 * 圆形进度条
 */

public class CircleProgressBar extends View {

    private int mProgressBackgroundColor;
    private int mProgressColor;
    private float mProgressMax = 100;
    private float mCircleWidth;
    private float mTextSize;
    private int mTextColor;
    private Paint mPaint;
    private float mProgress = 33;
    private ValueAnimator progressAnimator;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initCofig(context, attrs);
    }

    private void initCofig(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        mProgressBackgroundColor = typedArray.getColor(R.styleable.CircleProgressBar_progressBackgroundColor, Color.GRAY);
        mProgressColor = typedArray.getColor(R.styleable.CircleProgressBar_progressColor, Color.BLUE);
        mProgressMax = typedArray.getFloat(R.styleable.CircleProgressBar_progressMax, 100);
        mCircleWidth = typedArray.getDimension(R.styleable.CircleProgressBar_circleWidth, 20);
        mTextSize = typedArray.getDimension(R.styleable.CircleProgressBar_textSize, 60);
        mTextColor = typedArray.getColor(R.styleable.CircleProgressBar_textColor, Color.RED);
        typedArray.recycle();
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCofig(context, attrs);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /*
        重写onMeasure()是为了解决wrap_content的问题。
        如果没有加上这一段，那么使用wrap_content与match_parent就没有区别。
         */
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(300, 300);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(300, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, 300);
        }

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();


        //画圆环
        mPaint = new Paint();
        mPaint.setColor(mProgressBackgroundColor);
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        int center = getWidth() / 2;
        float radius = center - mCircleWidth / 2;
        canvas.drawCircle(center, center, radius, mPaint);
        //画文字
        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);
        mPaint.setStrokeWidth(0);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        int percent = (int) (mProgress / mProgressMax * 100);
        String percentStr = percent + "%";
        Paint.FontMetricsInt fm = mPaint.getFontMetricsInt();
        canvas.drawText(percentStr,
                center - mPaint.measureText(percentStr) / 2,
                center + (fm.bottom - fm.top) / 2 - fm.bottom,
                mPaint);
        //画圆弧
        mPaint = new Paint();
        mPaint.setColor(mProgressColor);
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);
        canvas.drawArc(oval, 270, -mProgress / mProgressMax * 360, false, mPaint);
        invalidate();
    }

    public void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("进度不能小于0！");
        }
        if (progress > mProgressMax) {
            mProgress = (int) mProgressMax;
        }
        if (progress < mProgressMax) {
            mProgress = progress;
            //不知道应用层在哪个线程调用
            postInvalidate();
        }
        setAnimation();
    }


    /**
     * 为进度设置动画
     */
    private void setAnimation() {
        progressAnimator = ValueAnimator.ofFloat(0, mProgress);
        progressAnimator.setDuration(2000);
        progressAnimator.setTarget(mProgress);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //不能是int （具体原因不知道）
                mProgress = (float) animation.getAnimatedValue();
            }
        });
        progressAnimator.start();
    }


}
