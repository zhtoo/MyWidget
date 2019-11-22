package com.zht.samplewidget.myView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.zht.samplewidget.R;


/**
 * 作者：zhanghaitao on 2017/11/28 10:06
 * 邮箱：820159571@qq.com
 *
 * @describe:弧线进度条
 */

public class MyProgressView extends View {

    private int wrap_content = 300;//默认的尺寸（px）
    private int viewWidth;
    private int viewHeight;
    private Paint backgroundPaint;
    private Paint progressPaint;
    private DrawFilter mDrawFilter;  //开启抗锯齿

    private int center;
    private float radius;

    private float mCircleWidth = 20;
    private float mProgress = 60;
    private float mProgressMax = 100;
    private float startAngle = 135;
    private float maxAngle = 270;
    private int backgroundColor = Color.GRAY;
    private int progressColor = Color.RED;

    public MyProgressView(Context context) {
        this(context, null);
    }

    public MyProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //TODO:在这里做初始化
        initCofig(context, attrs);
    }

    /**
     * 初始化配置
     *
     * @param context
     * @param attrs
     */
    private void initCofig(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyProgressView);
        mCircleWidth = typedArray.getDimension(R.styleable.MyProgressView_mCircleWidth, 20);
        mProgress = typedArray.getFloat(R.styleable.MyProgressView_mProgress, 60);
        mProgressMax = typedArray.getFloat(R.styleable.MyProgressView_mProgressMax, 100);
        startAngle = typedArray.getFloat(R.styleable.MyProgressView_startAngle, 135);
        maxAngle = typedArray.getFloat(R.styleable.MyProgressView_maxAngle, 270);
        backgroundColor = typedArray.getColor(R.styleable.MyProgressView_backgroundColor, Color.GRAY);
        progressColor = typedArray.getColor(R.styleable.MyProgressView_progressColor1, Color.BLUE);
        typedArray.recycle();
        initPaint();
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);//去锯齿
        backgroundPaint.setStyle(Paint.Style.STROKE);//设置样式
        backgroundPaint.setStrokeCap(Paint.Cap.ROUND);// 圆形线帽
        backgroundPaint.setStrokeWidth(mCircleWidth);//设置宽度
        backgroundPaint.setColor(backgroundColor);//设置颜色

        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);//去锯齿
        progressPaint.setStyle(Paint.Style.STROKE);//设置样式
        progressPaint.setStrokeCap(Paint.Cap.ROUND);// 圆形线帽
        progressPaint.setStrokeWidth(mCircleWidth);//设置宽度

        progressPaint.setColor(progressColor);//设置颜色
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**在这里可以设置数据+获取控件的宽高*/
        viewWidth = measureWidth(widthMeasureSpec);
        viewHeight = measureHeight(heightMeasureSpec);
        /**设置中心点*/
        center = getWidth() / 2;
        //设置宽高
        setMeasuredDimension(viewWidth, viewHeight);
    }

    /**
     * 测量宽度
     *
     * @param widthMeasureSpec
     * @return
     */
    private int measureWidth(int widthMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST || widthSpecMode == MeasureSpec.UNSPECIFIED) {
            //默认wrap_content为自己定义的高度
            return wrap_content;
        } else {
            return widthSpecSize;
        }
    }

    /**
     * 测量高度
     *
     * @param heightMeasureSpec
     * @return
     */
    private int measureHeight(int heightMeasureSpec) {
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightSpecMode == MeasureSpec.AT_MOST ||
                heightSpecMode == MeasureSpec.UNSPECIFIED//这里是为了解决在Listview和ScrollView的嵌套
                ) {
            //默认wrap_content为自己定义的高度
            return wrap_content;
        } else {
            return heightSpecSize;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 从canvas层面去除绘制时锯齿
        canvas.setDrawFilter(mDrawFilter);

        if (center == 0) {
            center = getWidth() / 2;
        }

        radius = center - mCircleWidth / 2;

        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);
        canvas.drawArc(oval, startAngle, maxAngle, false, backgroundPaint);
        canvas.drawArc(oval, startAngle, mProgress * maxAngle / mProgressMax, false, progressPaint);
    }


    public float getmCircleWidth() {
        return mCircleWidth;
    }

    public void setmCircleWidth(float mCircleWidth) {
        this.mCircleWidth = mCircleWidth;
    }

    public float getmProgress() {
        return mProgress;
    }

    public void setmProgress(float mProgress) {
        this.mProgress = mProgress;
        invalidate();//重新绘制
    }

    public float getmProgressMax() {
        return mProgressMax;
    }

    public void setmProgressMax(float mProgressMax) {
        this.mProgressMax = mProgressMax;
    }

    public float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
    }

    public float getMaxAngle() {
        return maxAngle;
    }

    public void setMaxAngle(float maxAngle) {
        this.maxAngle = maxAngle;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }
}
