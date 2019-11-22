package com.zht.samplewidget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

/**
 * SweepGradient经典的应用场景就是实现雷达扫描效果。
 *
 * 作者：zhanghaitao on 2017/8/17 14:28
 * 邮箱：820159571@qq.com
 */

public class SweepGradientView extends View {

    private int[] mColors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
    private Paint paint;


    public SweepGradientView(Context context) {
        this(context, null);
        initView();
    }

    public SweepGradientView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initView();
    }


    public SweepGradientView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    private void initView() {
        //画笔
        paint = new Paint();
        paint.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        LinearGradientTest(canvas);
    }

    /**
     * SweepGradient叫渐变渲染或者梯度渲染，其效果与用法都和LinearGradient类似，还是先介绍基本用法
     * x,y 渐变的中心点
     *
     */
    private void LinearGradientTest(Canvas canvas) {
        SweepGradient mSweepGradient = new SweepGradient(400, 400, mColors, null);
        paint.setShader(mSweepGradient);
        canvas.drawRect(0, 0, 800,800, paint);
    }



}
