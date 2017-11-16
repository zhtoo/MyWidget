package com.hs.samplewidget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.SumPathEffect;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

/**
 * 作者：zhanghaitao on 2017/8/17 09:54
 * 邮箱：820159571@qq.com
 */

public class PaintView extends View {

    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //drawStrokeCap(canvas);
        //drawStrokeJoin(canvas);
        drawComposePathEffectDemo(canvas);

    }

    /**
     * setStyle(Paint.Style style):设置画笔样式
     */
    public void initPaint() {
        Paint paint = new Paint();
        /*
        Paint.Style.FILL :填充内部
        Paint.Style.FILL_AND_STROKE ：填充内部和描边
        Paint.Style.STROKE ：仅描边
        */
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    /**
     * 设置线冒样式
     * Paint.Cap.ROUND：圆形线冒
     * Paint.Cap.SQUARE：方形线冒
     * Paint.Cap.BUTT：无线冒)
     *
     * @param canvas
     */
    private void drawStrokeCap(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStrokeWidth(80);
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);

        paint.setStrokeCap(Paint.Cap.BUTT);// 无线帽
        canvas.drawLine(100, 200, 400, 200, paint);

        paint.setStrokeCap(Paint.Cap.SQUARE);// 方形线帽
        canvas.drawLine(100, 400, 400, 400, paint);

        paint.setStrokeCap(Paint.Cap.ROUND);// 圆形线帽
        canvas.drawLine(100, 600, 400, 600, paint);

    }

    /**
     * 设置线段连接处样式
     * Join.MITER：结合处为锐角
     * Join.ROUND：结合处为圆弧
     * Join.BEVEL：结合处为直线
     *
     * @param canvas
     */
    private void drawStrokeJoin(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStrokeWidth(50);
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);

        Path path = new Path();

        path.moveTo(100, 100);
        path.lineTo(450, 100);
        path.lineTo(100, 300);
        paint.setStrokeJoin(Paint.Join.MITER);//锐角
        canvas.drawPath(path, paint);

        path.moveTo(100, 400);
        path.lineTo(450, 400);
        path.lineTo(100, 600);
        paint.setStrokeJoin(Paint.Join.BEVEL);//直线
        canvas.drawPath(path, paint);

        path.moveTo(100, 700);
        path.lineTo(450, 700);
        path.lineTo(100, 900);
        paint.setStrokeJoin(Paint.Join.ROUND);//圆弧
        canvas.drawPath(path, paint);
    }

    /**
     * 1.CornerPathEffect：圆形拐角效果
     * paint.setPathEffect(new CornerPathEffect(100));
     * 利用半径R=50的圆来代替原来两条直线间的夹角
     * 2.DashPathEffect：虚线效果
     * paint.setPathEffect(new DashPathEffect(new float[]{20,10,50,100},15));
     * intervals[]：表示组成虚线的各个线段的长度；整条虚线就是由intervals[]中这些基本线段循环组成的。
     * phase：表示开始绘制的偏移值
     * 5.setXfermode(Xfermode xfermode)：设置图形重叠时的处理方式
     * 如合并，取交集或并集，经常用来制作橡皮的擦除效果
     * 6.setMaskFilter(MaskFilter maskfilter)：实现滤镜的效果
     * 如滤化，立体等
     * 7.setColorFilter(ColorFilter colorfilter)：设置颜色过滤器
     * 可以在绘制颜色时实现不用颜色的变换效果
     * <p>
     * 8.setShader(Shader shader)：设置图像效果
     * 使用Shader可以绘制出各种渐变效果
     */
    private void drawComposePathEffectDemo(Canvas canvas) {
        //画原始路径
        Paint paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);

        Path path = new Path();
        int Y = 100;
        int X = 30;
        path.moveTo(0, Y / 2);
        for (int i = 0; i < 40; i++) {
            path.lineTo(new Random().nextInt(X) + i * X, new Random().nextInt(100));
        }
        canvas.drawPath(path, paint);

        //仅应用圆角特效的路径
        canvas.translate(0, 300);
        CornerPathEffect cornerPathEffect = new CornerPathEffect(100);
        paint.setPathEffect(cornerPathEffect);
        canvas.drawPath(path, paint);

        //仅应用虚线特效的路径
        canvas.translate(0, 300);
        DashPathEffect dashPathEffect = new DashPathEffect(new float[]{2, 5, 10, 10}, 0);
        paint.setPathEffect(dashPathEffect);
        canvas.drawPath(path, paint);

        //利用ComposePathEffect先应用圆角特效,再应用虚线特效
        canvas.translate(0, 300);
        ComposePathEffect composePathEffect = new ComposePathEffect(dashPathEffect, cornerPathEffect);
        paint.setPathEffect(composePathEffect);
        canvas.drawPath(path, paint);

        //利用SumPathEffect,分别将圆角特效应用于原始路径,然后将生成的两条特效路径合并
        canvas.translate(0, 300);
        paint.setStyle(Paint.Style.STROKE);
        SumPathEffect sumPathEffect = new SumPathEffect(cornerPathEffect, dashPathEffect);
        paint.setPathEffect(sumPathEffect);
        canvas.drawPath(path, paint);
    }


}
