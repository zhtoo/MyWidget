package com.zht.samplewidget.myView.chart;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
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
    private LineChartParams params;

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
    private List<Integer> monthSamplingPoint =
            Arrays.asList(new Integer[]{0, 4, 9, 14, 19, 24});

    private float xAxisRange;//可变
    private float yAxisRange;//可变化的
    private Point zeroPoint = new Point();
    private int lineChartStartX;
    private int lineChartEndX;
    private int mXAxisEqual = 0;

    private int mAnimatorTarget;
    private boolean needAnimation;
    private ValueAnimator lineChartAnimator;

    @Override
    public int getDefaultWidth() {
        return 300;
    }

    @Override
    public int getDefaultHeight() {
        return 300;
    }

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
        xy_axisPaint.setFilterBitmap(true);
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
        //绘制虚线效果  //DottedLine 虚线
        PathEffect effects = new DashPathEffect(new float[]{1, 2, 4, 8}, 1);
        tickMarkPaint.setPathEffect(effects);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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

    public void setParams(LineChartParams lineChartBean) {

        if (lineChartAnimator != null) {
            lineChartAnimator.cancel();
            lineChartAnimator = null;
        }

        params = lineChartBean;
        mXAxisEqual = 0;
        List<Integer> yAxitList = new ArrayList<>();
        for (LineChartParams.SeriesBean seriesBean : params.getSeries()) {
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


        needAnimation = false;

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

        if (maxValue == 0) {
            return 50;
        } else if (maxValue % 50 == 0) {
            return maxValue;
        }
        int multiple = maxValue / 50;
        return (multiple + 1) * 50;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        Log.e(TAG, "onDraw");
        super.onDraw(canvas);
        drawYAxis(canvas);//绘制Y轴
        drawXAxis(canvas);//绘制X轴
        drawYAxisTickMark(canvas);//绘制Y轴刻度虚线、数值
        drawXAxisTickMark(canvas);//绘制X轴数值

        //动画
        if (needAnimation) {
            drawSimulationAnimator(canvas);
        } else {
            drawLineCharts(canvas);//绘制折线
        }
    }

    private void drawSimulationAnimator(Canvas canvas) {
        if (lineChartAnimator == null) {
            //开启动画
            startAnimator();
        }
        //计算y轴  高度/单位
        double unit = (zeroPoint.y - drawRange.top - mLeaveBlank) / (double) yMaxValue;
        if (params == null || params.getSeries() == null || params.getSeries().size() == 0) {
            return;
        }
        //单个数据不绘画
        if (mXAxisEqual == 0 || mXAxisEqual == 1) {
            return;
        }
        //计算x轴 间隔
        double mSpacing = (lineChartEndX - lineChartStartX) / (mXAxisEqual - 1);

        //循环绘制 数据
        for (LineChartParams.SeriesBean series : params.getSeries()) {
            lineChartPaint.setColor(series.getDrawColor());
            Path path = new Path();

            //计算当前绘制的位置
            int position = (int) ((mAnimatorTarget - lineChartStartX) / mSpacing);

            double remainder = ((mAnimatorTarget - lineChartStartX)) % mSpacing;

            //position最大值为最长数据的长度
            //在此限制
            if (position <= series.getData().size()) {

                //绘制position之前的点
                for (int i = 0; i < position; i++) {
                    if (i == 0) {
                        path.moveTo((float) (lineChartStartX + i * mSpacing),
                                (float) (zeroPoint.y - unit * series.getData().get(i)));
                    } else {
                        path.lineTo((float) (lineChartStartX + i * mSpacing),
                                (float) (zeroPoint.y - unit * series.getData().get(i)));
                    }
                }

                if (position == 0 && mAnimatorTarget != lineChartStartX) {
                    path.moveTo((float) (lineChartStartX),
                            (float) (zeroPoint.y - unit * series.getData().get(0)));
                }
                //绘制position之后的点
                if (remainder != 0) {
                    if (position + 1 < series.getData().size()) {
                        int startPoint = (int) (unit * series.getData().get(position));
                        int endPoint = (int) (unit * series.getData().get(position + 1));
                        int dY = endPoint - startPoint;
                        double value = (startPoint + dY * remainder / mSpacing) / unit;
                        path.lineTo(mAnimatorTarget,
                                (float) (zeroPoint.y - (value * unit)));
                    }
                }
                if (mAnimatorTarget != lineChartStartX) {
                    canvas.drawPath(path, lineChartPaint);
                }
            }
        }
    }

    private void startAnimator() {

        lineChartAnimator = ValueAnimator.ofInt(lineChartStartX, lineChartEndX);
        lineChartAnimator.setDuration(500);
        lineChartAnimator.setTarget(lineChartEndX);
        lineChartAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                mAnimatorTarget = (int) animation.getAnimatedValue();
                Log.e(TAG, "onAnimationUpdate: " + mAnimatorTarget);
                invalidate();
            }
        });
        lineChartAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                lineChartAnimator = null;
                needAnimation = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        lineChartAnimator.start();

    }

    private void drawLineCharts(Canvas canvas) {
        double unit = (zeroPoint.y - drawRange.top - mLeaveBlank) / (double) yMaxValue;
        if (params == null || params.getSeries() == null || params.getSeries().size() == 0) {
            return;
        }
        if (mXAxisEqual == 0 || mXAxisEqual == 1) {//单个数据不绘画
            return;
        }

        double mSpacing = (lineChartEndX - lineChartStartX) / (mXAxisEqual - 1);

        for (LineChartParams.SeriesBean series : params.getSeries()) {
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
            mTextPaint.setColor(0xFFCCCCCC);
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
            mTextPaint.setColor(drawColor);
            //绘制日期

            if (data.size() > 26) {
                for (int i = 0; i < data.size(); i++) {
                    if (monthSamplingPoint.contains(i)
                            || (i + 1) == data.size()) {
                        canvas.drawText(data.get(i),
                                (float) (lineChartStartX + i * mSpacing - getFontWidth(mTextPaint, data.get(i)) / 2),
                                zeroPoint.y + xAxisRange / 2 + getFontBaseLine(mTextPaint),
                                mTextPaint);
                    }
                }
            } else {
                for (int i = 0; i < data.size(); i++) {
                    canvas.drawText(data.get(i),
                            (float) (lineChartStartX + i * mSpacing - getFontWidth(mTextPaint, data.get(i)) / 2),
                            zeroPoint.y + xAxisRange / 2 + getFontBaseLine(mTextPaint),
                            mTextPaint);
                }
            }


        }
    }

    /**
     * 绘制Y轴刻度线
     *
     * @param canvas
     */
    private void drawYAxisTickMark(Canvas canvas) {
        if (params != null) {
            int drawColor = params.getyAxis().getDrawColor();
            mTextPaint.setColor(drawColor);
        } else {
            mTextPaint.setColor(0xFFCCCCCC);
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