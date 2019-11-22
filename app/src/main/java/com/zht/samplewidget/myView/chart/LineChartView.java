package com.zht.samplewidget.myView.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;

import com.zht.samplewidget.myView.BaseCustomizeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by ZhangHaitao on 2019/11/22
 */
public class LineChartView extends BaseCustomizeView {

    private static final String TAG = "LineChartView";

    //折线图数据
    private LineChartBean params;

    //默认Y轴5等分
    private int mEqual = 5;
    private int yMaxValue = 100;
    private int mLeaveBlank;

    //画笔
    private Paint xy_axisPaint;
    private Paint lineChartPaint;
    private TextPaint mTextPaint;
    private Paint tickMarkPaint;

    //视图尺寸相关参数
    private int viewWidth;
    private int viewHeight;
    private Rect drawRange;//绘制范围
    private int[] monthSamplingPoint = {0, 4, 9, 14, 19, 24};


    private float xAxisRange;//可变
    private float yAxisRange;//可变化的
    private Point zeroPoint = new Point();
    private int lineChartStartX;
    private int lineChartEndX;
    private int mXAxisEqual = 0;

    @Override
    public int getDefaultWidth() {
        return 300;
    }

    @Override
    public int getDefaultHeight() {
        return 300;
    }

    /**
     * initCofig
     * onMeasure
     * onMeasure
     * onLayout ：changed = true
     * onDraw
     */

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
        drawRange = new Rect();
        initPaint();
        xAxisRange = getFontHeight(mTextPaint) + dp2px(4);
        yAxisRange = getFontWidth(mTextPaint, "100") + dp2px(2);

        mLeaveBlank = (int) (getFontHeight(mTextPaint) / 2 + dp2px(2));
    }

    private void initPaint() {
        //1、画X、Y轴的线
        xy_axisPaint = new Paint();
        xy_axisPaint.setAntiAlias(true);//防止边缘的锯齿，
        xy_axisPaint.setFilterBitmap(true);//对位图进行滤波处理
        xy_axisPaint.setStrokeWidth(2);//2个像素点
        xy_axisPaint.setColor(0xFFCCCCCC);
        xy_axisPaint.setStyle(Paint.Style.STROKE);//描边
        xy_axisPaint.setStrokeCap(Paint.Cap.ROUND);// 圆形线帽

        //绘画X\Y轴坐标单位
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.density = getContext().getResources().getDisplayMetrics().density;
        mTextPaint.setTextSize(sp2px(10));
        mTextPaint.setColor(0xFF555555);

        //画折线画笔
        lineChartPaint = new Paint();
        lineChartPaint.setAntiAlias(true);//防锯齿
        lineChartPaint.setStyle(Paint.Style.STROKE);
        lineChartPaint.setStrokeWidth(2);//2个像素点
        lineChartPaint.setStrokeJoin(Paint.Join.BEVEL);//直线

        //绘制虚线
        tickMarkPaint = new Paint();
        tickMarkPaint.setAntiAlias(true);
        tickMarkPaint.setFilterBitmap(true);
        tickMarkPaint.setStrokeWidth(2);
        tickMarkPaint.setColor(0xFFCCCCCC);
        tickMarkPaint.setStyle(Paint.Style.STROKE);//描边
        tickMarkPaint.setStrokeCap(Paint.Cap.ROUND);// 圆形线帽
        //绘制虚线效果
        PathEffect effects = new DashPathEffect(new float[]{1, 2, 4, 8}, 1);
        tickMarkPaint.setPathEffect(effects);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            viewWidth = right - left;
            viewHeight = bottom - top;
            drawRange.left = getPaddingLeft();
            drawRange.right = viewWidth - getPaddingRight();
            drawRange.top = getPaddingTop();
            drawRange.bottom = viewHeight - getPaddingBottom();

            zeroPoint.x = (int) (drawRange.left + yAxisRange);
            zeroPoint.y = (int) (drawRange.bottom - xAxisRange);

            lineChartStartX = (int) (
                    zeroPoint.x + dp2px(6) + getFontWidth(mTextPaint, "00.00") / 2);
            lineChartEndX = (int) (drawRange.right - dp2px(2) - getFontWidth(mTextPaint, "00.00") / 2);
        }
    }


    public void setParams(LineChartBean lineChartBean) {
        lineChartBean = new LineChartBean();
        lineChartBean.setyAxis(new LineChartBean.YAxisBean());
        lineChartBean.setxAxis(new LineChartBean.XAxisBean());
        lineChartBean.setSeries(new ArrayList<LineChartBean.SeriesBean>());

        LineChartBean.XAxisBean xAxisBean = lineChartBean.getxAxis();
        xAxisBean.setDrawColor(0xFF555555);
        xAxisBean.setData(new ArrayList<String>());
        xAxisBean.getData().addAll(Arrays
                .asList(new String[]{
                        "周一", "周二", "周三", "周四", "周五", "周六", "周日"
                }));


        List<LineChartBean.SeriesBean> series = lineChartBean.getSeries();
        LineChartBean.SeriesBean seriesBean1 = new LineChartBean.SeriesBean();
        seriesBean1.setDrawColor(0xFFFFB000);
        seriesBean1.setData(Arrays.asList(new Integer[]{
                150, 232, 201, 154, 190, 330, 410}));

        LineChartBean.SeriesBean seriesBean2 = new LineChartBean.SeriesBean();
        seriesBean2.setDrawColor(0xFF3379F8);
        seriesBean2.setData(Arrays.asList(new Integer[]{
                320, 332, 301, 334, 390, 330, 320}));

        LineChartBean.SeriesBean seriesBean3 = new LineChartBean.SeriesBean();
        seriesBean3.setDrawColor(0xFFFC4A5B);
        seriesBean3.setData(Arrays.asList(new Integer[]{
                820, 932, 901, 934, 1290, 1330, 1320}));
        series.add(seriesBean1);
        series.add(seriesBean2);
        series.add(seriesBean3);

        LineChartBean.YAxisBean yAxisBean = lineChartBean.getyAxis();
        yAxisBean.setDrawColor(0xFFCCCCCC);

        params = lineChartBean;


        List<Integer> yAxitList = new ArrayList<>();
        for (LineChartBean.SeriesBean seriesBean : params.getSeries()) {
            yAxitList.addAll(seriesBean.getData());
            mXAxisEqual = Math.max(mXAxisEqual, seriesBean.getData().size());
        }

        yMaxValue = getMaxValue(yAxitList);

        xAxisRange = getFontHeight(mTextPaint) + dp2px(4);
        yAxisRange = getFontWidth(mTextPaint, String.valueOf(yMaxValue))
                + dp2px(2);

        zeroPoint.x = (int) (drawRange.left + yAxisRange);
        zeroPoint.y = (int) (drawRange.bottom - xAxisRange);

        String s = params.getxAxis().getData().get(0);

        lineChartStartX = (int) (
                zeroPoint.x + dp2px(6) + getFontWidth(mTextPaint, s) / 2);
        lineChartEndX = (int) (drawRange.right - dp2px(2) - getFontWidth(mTextPaint, s) / 2);


        invalidate();
    }

    private static int getMaxValue(List<Integer> yAxitList) {
        Collections.sort(yAxitList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        int maxValue = yAxitList.get(yAxitList.size() - 1);
        if (maxValue < 10) {
            maxValue = 10;
        } else {
            int digits = 0;
            while (maxValue >= 100) {
                maxValue = maxValue / 10;
                digits++;
            }
            if (maxValue % 5 != 0) {
                maxValue = (maxValue / 5 + 1) * 5;
            }
            if (digits != 0) {
                maxValue = (int) (maxValue * Math.pow(10, digits));
            }
        }
        return maxValue;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Log.e(TAG, "onDraw");
        super.onDraw(canvas);
        drawYAxis(canvas);//绘制Y轴
        drawXAxis(canvas);//绘制X轴
        drawYAxisTickMark(canvas);//绘制y轴刻度虚线、数值
        drawXAxisTickMark(canvas);//绘制x轴数值
        drawLineCharts(canvas);//绘制x轴数值
    }

    private void drawLineCharts(Canvas canvas) {
        double unit = (zeroPoint.y - drawRange.top - mLeaveBlank) / (double)yMaxValue;
        if (params == null || params.getSeries() == null || params.getSeries().size() == 0) {
            return;
        }
        if (mXAxisEqual == 0 || mXAxisEqual == 1) {//单个数据不绘画
            return;
        }

        double mSpacing = (lineChartEndX - lineChartStartX) / (mXAxisEqual - 1);


        for (LineChartBean.SeriesBean series : params.getSeries()) {
            lineChartPaint.setColor(series.getDrawColor());
            Path path = new Path();
            for (int i = 0; i < series.getData().size(); i++) {
                if (i == 0) {
                    path.moveTo((float) (lineChartStartX + i * mSpacing),
                            (float) (zeroPoint.y - unit * series.getData().get(i)));
                } else {
                    path.lineTo((float) (lineChartStartX + i * mSpacing),
                            (float) (zeroPoint.y - unit * series.getData().get(i)));
                }
            }

            canvas.drawPath(path, lineChartPaint);
        }


    }

    private void drawXAxisTickMark(Canvas canvas) {
        if (params == null) {
            //模拟生成日期
            long time = System.currentTimeMillis();
            Date date = new Date();
            String[] mDate = new String[7];
            for (int i = 0; i < 7; i++) {
                date.setTime(time - i * 24 * 60 * 60 * 1000);
                SimpleDateFormat sdf = new SimpleDateFormat("MM.dd");
                mDate[7 - i - 1] = sdf.format(date);
            }
            double mSpacing = (lineChartEndX - lineChartStartX) / (mDate.length - 1);
            //绘制日期
            for (int i = 0; i < mDate.length; i++) {
                canvas.drawText(mDate[i],
                        (float) (lineChartStartX + i * mSpacing - getFontWidth(mTextPaint, mDate[i]) / 2),
                        zeroPoint.y + xAxisRange / 2 + getFontBaseLine(mTextPaint),
                        mTextPaint);
            }
        } else {
            int drawColor = params.getxAxis().getDrawColor();
            List<String> data = params.getxAxis().getData();
            double mSpacing = (lineChartEndX - lineChartStartX) / (data.size() - 1);
            //绘制日期
            mTextPaint.setColor(drawColor);
            for (int i = 0; i < data.size(); i++) {
                canvas.drawText(data.get(i),
                        (float) (lineChartStartX + i * mSpacing - getFontWidth(mTextPaint, data.get(i)) / 2),
                        zeroPoint.y + xAxisRange / 2 + getFontBaseLine(mTextPaint),
                        mTextPaint);
            }

        }
    }

    private void drawYAxisTickMark(Canvas canvas) {
        if (params != null) {
            int drawColor = params.getyAxis().getDrawColor();
            mTextPaint.setColor(drawColor);
        }
        double mSpacing = (zeroPoint.y - drawRange.top - mLeaveBlank) / mEqual;
        double mTickMarkHeight = drawRange.top + mLeaveBlank;
        String mTickMarkValue;
        for (int i = 0; i < mEqual; i++) {
            //绘制虚线刻度效果
            Path path = new Path();
            path.moveTo(zeroPoint.x, (int) mTickMarkHeight);
            path.lineTo(drawRange.right, (int) mTickMarkHeight);
            canvas.drawPath(path, tickMarkPaint);

            //绘制刻度值
            mTickMarkValue = String.valueOf(yMaxValue - i * yMaxValue / mEqual);
            canvas.drawText(mTickMarkValue,
                    zeroPoint.x - getFontWidth(mTextPaint, mTickMarkValue) - dp2px(2),
                    (int) mTickMarkHeight + getFontBaseLine(mTextPaint),
                    mTextPaint);

            mTickMarkHeight += mSpacing;
        }

        String zeroTickMark = "0";
        canvas.drawText(zeroTickMark,
                zeroPoint.x - getFontWidth(mTextPaint, zeroTickMark) - dp2px(2),
                zeroPoint.y + getFontBaseLine(mTextPaint),
                mTextPaint);

    }

    private void drawYAxis(Canvas canvas) {
        //画y轴线
        canvas.drawLine(zeroPoint.x, drawRange.top,
                zeroPoint.x, zeroPoint.y,
                xy_axisPaint);
    }

    private void drawXAxis(Canvas canvas) {
        //画X轴线
        canvas.drawLine(zeroPoint.x, zeroPoint.y,
                drawRange.right, zeroPoint.y,
                xy_axisPaint);
    }


}