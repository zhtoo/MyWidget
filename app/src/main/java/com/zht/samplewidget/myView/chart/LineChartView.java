package com.zht.samplewidget.myView.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import com.zht.samplewidget.myView.BaseCustomizeView;

import java.util.List;

/**
 * Created by ZhangHaitao on 2019/11/22
 */
public class LineChartView extends BaseCustomizeView {

    private static final String TAG = "LineChartView";

    private Params params;
    private PaintFlagsDrawFilter mDrawFilter;

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCofig(attrs);
    }

    private void initCofig(AttributeSet attrs) {
        Log.e(TAG, "initCofig");
//        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HstogramView);
//
//        typedArray.recycle();
        PaintFlagsDrawFilter pfd= new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);

        initPaint();
    }

    private void initPaint() {
        //是用来防止边缘的锯齿，
//        paint.setAntiAlias(true);
        //函数是用来对位图进行滤波处理
//        paint.setFilterBitmap(true);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e(TAG, "onMeasure: ");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.e(TAG, "onLayout: ");
        Log.e(TAG, "changed: " + changed);

        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
       super.onDraw(canvas);
        Log.e(TAG, "onDraw");
    }

    @Override
    public int getDefaultWidth() {
        return 300;
    }

    @Override
    public int getDefaultHeight() {
        return 300;
    }


    public static class Params {

        private xAxis xAxis;

        private yAxis yAxis;

        private List<SeriesBean> series;

        public LineChartView.xAxis getXAxis() {
            return xAxis;
        }

        public void setXAxis(LineChartView.xAxis xAxis) {
            this.xAxis = xAxis;
        }

        public LineChartView.yAxis getYAxis() {
            return yAxis;
        }

        public void setYAxis(LineChartView.yAxis yAxis) {
            this.yAxis = yAxis;
        }

        public List<SeriesBean> getSeries() {
            return series;
        }

        public void setSeries(List<SeriesBean> series) {
            this.series = series;
        }
    }

    public static class SeriesBean {
        //描述
        private String des;
        //绘制的颜色
        private int drawColor = Color.RED;
        //绘制点
        private List<Integer> data;

    }

    public static class xAxis {
        //描述
        private String xDes;
        private int drawColor = Color.RED;
        //绘制X轴数据
        private List<String> data;

    }

    public static class yAxis {
        //描述
        private String yDes;

        private int drawColor = Color.RED;
        //绘制Y轴数据
        private List<Integer> data;
    }


}