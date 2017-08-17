package com.hs.samplewidget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

/**
 * SweepGradient经典的应用场景就是实现雷达扫描效果。
 * <p>
 * 作者：zhanghaitao on 2017/8/17 14:28
 * 邮箱：820159571@qq.com
 */

public class RadarView extends View {

    private int[] mColors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
    private Paint mRadarBg;
    private Paint mRadarPaint;
    private SweepGradient mRadarShader;
    private Matrix mMatrix;
    private int mRadarBgColor = Color.BLACK;
    private int mCircleColor = Color.BLACK;
    private int[] mStartColor = {Color.BLACK, Color.GREEN, Color.BLACK};
    private float[] mEndColor = {0,90,180};


    int mRadarRadius = 400;
    //圆圈数目
    int mCircleNum = 5;

    private float mRotate = 0;
    private Point mPoint;
    private Paint mRadarLinePaint;
    private int mLineColor = Color.BLACK;
    private int MAG_WHAT = 88;
    private long DELAY_TIME = 10;


    public RadarView(Context context) {
        this(context, null);
        initView(context);
    }

    public RadarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initView(context);
    }


    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    private void initView(Context context) {
        //获取屏幕宽度
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mPoint = new Point();
        windowManager.getDefaultDisplay().getSize(mPoint);
        mRadarRadius = mPoint.x / 2;


        //画笔
        mRadarBg = new Paint(Paint.ANTI_ALIAS_FLAG);     //设置抗锯齿
        mRadarBg.setColor(mRadarBgColor);                  //画笔颜色
        mRadarBg.setStyle(Paint.Style.FILL);           //画实心圆

        mRadarShader = new SweepGradient(0, 0, mStartColor, mEndColor);
        mMatrix = new Matrix();

        mRadarLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);     //设置抗锯齿
        mRadarLinePaint.setColor(mLineColor);                  //画笔颜色
        mRadarLinePaint.setStyle(Paint.Style.STROKE);           //设置空心的画笔，只画圆边
        mRadarLinePaint.setStrokeWidth(5);                      //画笔宽度

        mHandler.sendEmptyMessageDelayed(MAG_WHAT, DELAY_TIME);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRadarBg.setShader(null);
        //将画板移动到屏幕的中心点
        canvas.translate(mRadarRadius, mRadarRadius);
        //绘制底色，让雷达的线看起来更清晰
        canvas.drawCircle(0, 0, mRadarRadius, mRadarBg);

        //绘制雷达基线 x轴
        canvas.drawLine(-mRadarRadius, 0, mRadarRadius, 0, mRadarLinePaint);
        //绘制雷达基线 y轴
        canvas.drawLine(0, mRadarRadius, 0, -mRadarRadius, mRadarLinePaint);


        //设置颜色渐变从透明到不透明
        mRadarBg.setShader(mRadarShader);
        canvas.concat(mMatrix);
        canvas.drawCircle(0, 0, mRadarRadius, mRadarBg);

        //画圆圈
        for (int i = 1; i <= mCircleNum; i++) {
            canvas.drawCircle(200, 200, (float) (i * (mRadarRadius / mCircleNum)), mRadarLinePaint);
        }


    }

    /**
     * SweepGradient叫渐变渲染或者梯度渲染，其效果与用法都和LinearGradient类似，还是先介绍基本用法
     * x,y 渐变的中心点
     */
    private void LinearGradientTest(Canvas canvas) {
        SweepGradient mSweepGradient = new SweepGradient(400, 400, mColors, null);
        mRadarPaint.setShader(mSweepGradient);
        canvas.drawRect(0, 0, 800, 800, mRadarPaint);
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mRotate += 0.5;
            postInvalidate();
            mMatrix.reset();
            mMatrix.preRotate(mRotate, 0, 0);
            mHandler.sendEmptyMessageDelayed(MAG_WHAT, DELAY_TIME);
        }
    };


}
