package com.zht.samplewidget.myView.chart;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

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
    private boolean showRemind;
    private ValueAnimator lineChartAnimator;
    private double mSpacing;
    private double unit;
    private RectF remindRect;
    private TextPaint remindTextPaint;
    private Paint remindPaint;
    private float remindWidth;
    private float remindHeight;


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
        lineChartPaint.setFilterBitmap(true);
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

        //画圆角矩形
        remindPaint = new Paint();
        remindPaint.setAntiAlias(true);//防止边缘的锯齿，
        remindPaint.setFilterBitmap(true);//对位图进行滤波处理
        remindPaint.setStrokeWidth(2);//2个像素点
        remindPaint.setColor(0x88000000);
        remindPaint.setStyle(Paint.Style.FILL);//描边
        remindPaint.setStrokeCap(Paint.Cap.ROUND);// 圆形线帽

        remindTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        remindTextPaint.density = getContext().getResources().getDisplayMetrics().density;
        remindTextPaint.setTextSize(sp2px(12));
        remindTextPaint.setColor(0xFFFFFFFF);
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
                    zeroPoint.x + dp2px(6) +
                            getFontWidth(mTextPaint, "00.00") / 2);
            lineChartEndX = (int) (drawRange.right - dp2px(2) - getFontWidth(mTextPaint, "00.00") / 2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
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
        //绘制点击后的提示区域
        if (showRemind) {
            drawRemind(canvas);
        }
    }

    private void drawSimulationAnimator(Canvas canvas) {
        if (lineChartAnimator == null) {
            //开启动画
            startAnimator();
        }
        if (params == null || params.getSeries() == null || params.getSeries().size() == 0) {
            return;
        }
        //单个数据不绘画
        if (mXAxisEqual == 0 || mXAxisEqual == 1) {
            return;
        }
        //循环绘制 数据
        for (LineChartParams.SeriesBean series : params.getSeries()) {
            lineChartPaint.setColor(series.getDrawColor());
            List<Point> drawPoint = series.getDrawPoint();
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
                        path.moveTo(drawPoint.get(i).x,
                                drawPoint.get(i).y);
                    } else {
                        path.lineTo(drawPoint.get(i).x,
                                drawPoint.get(i).y);
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
        if (params == null || params.getSeries() == null || params.getSeries().size() == 0) {
            return;
        }
        if (mXAxisEqual == 0 || mXAxisEqual == 1) {//单个数据不绘画
            return;
        }
        for (LineChartParams.SeriesBean series : params.getSeries()) {
            lineChartPaint.setColor(series.getDrawColor());
            List<Point> drawPoint = series.getDrawPoint();
            Path path = new Path();
            for (int i = 0; i < series.getData().size(); i++) {
                if (i == 0) {
                    path.moveTo(drawPoint.get(i).x,
                            drawPoint.get(i).y);
                } else {
                    path.lineTo(drawPoint.get(i).x,
                            drawPoint.get(i).y);
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
            List<Point> drawPoint = params.getxAxis().getDrawPoint();
            mTextPaint.setColor(drawColor);
            //绘制日期
            if (data.size() > 26) {
                for (int i = 0; i < data.size(); i++) {
                    if (monthSamplingPoint.contains(i)
                            || (i + 1) == data.size()) {
                        canvas.drawText(data.get(i), drawPoint.get(i).x, drawPoint.get(i).y,
                                mTextPaint);
                    }
                }
            } else {
                for (int i = 0; i < data.size(); i++) {
                    canvas.drawText(data.get(i),
                            drawPoint.get(i).x,
                            drawPoint.get(i).y,
                            mTextPaint);
                }
            }
        }
    }

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


    private void drawRemind(Canvas canvas) {
        if (remindRect == null) {
            return;
        }
        //画圆角矩形
        remindPaint = new Paint();
        remindPaint.setAntiAlias(true);//防止边缘的锯齿，
        remindPaint.setFilterBitmap(true);//对位图进行滤波处理
        remindPaint.setStrokeWidth(2);//2个像素点
        remindPaint.setColor(0x88000000);
        remindPaint.setStyle(Paint.Style.FILL);//描边
        remindPaint.setStrokeCap(Paint.Cap.ROUND);// 圆形线帽

        remindTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        remindTextPaint.density = getContext().getResources().getDisplayMetrics().density;
        remindTextPaint.setTextSize(sp2px(12));
        remindTextPaint.setColor(0xFFFFFFFF);

        //第二个参数是x半径，第三个参数是y半径
        canvas.drawRoundRect(remindRect, 10, 10, remindPaint);

        Point point = params.getxAxis().getDrawPoint().get(remindPosition);
        String s = params.getxAxis().getData().get(remindPosition);
        canvas.drawLine(point.x + getFontWidth(mTextPaint, s) / 2, drawRange.top,
                point.x + getFontWidth(mTextPaint, s) / 2, zeroPoint.y,
                xy_axisPaint);

        if (remindPosition > -1 && remindPosition < params.getxAxis().getData().size()) {
            canvas.drawText(params.getxAxis().getData().get(remindPosition),
                    remindRect.left + dp2px(5),
                    remindRect.top + dp2px(5) +
                            getFontHeight(remindTextPaint) / 2 +
                            getFontBaseLine(remindTextPaint),
                    remindTextPaint);
        }

        float tempHeight = remindRect.top +
                getFontHeight(remindTextPaint) + dp2px(5F);

        for (LineChartParams.SeriesBean series : params.getSeries()) {
            String name = series.getName();
            name += ":" + series.getData().get(remindPosition);
            remindPaint.setColor(series.getDrawColor());
            canvas.drawCircle(remindRect.left + dp2px(5) + dp2px(4.5F),
                    tempHeight + dp2px(5) +
                            getFontHeight(remindTextPaint) / 2,
                    dp2px(4.5F), remindPaint);
            canvas.drawText(name,
                    remindRect.left + dp2px(5) + dp2px(11),
                    tempHeight + dp2px(5) +
                            getFontHeight(remindTextPaint) / 2 +
                            getFontBaseLine(remindTextPaint),
                    remindTextPaint);
            tempHeight += getFontHeight(remindTextPaint) + dp2px(5F);


            Point seriesPoint = series.getDrawPoint().get(remindPosition);
            canvas.drawCircle(seriesPoint.x, seriesPoint.y, dp2px(3F),
                    remindPaint);
            remindPaint.setColor(0xFFFFFFFF);
            canvas.drawCircle(seriesPoint.x, seriesPoint.y, dp2px(2F),
                    remindPaint);
        }
    }

    private int remindPosition = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        if (x < zeroPoint.x || x > drawRange.right ||
                y < drawRange.top || y > zeroPoint.y) {
            if (showRemind == true) {
                showRemind = false;
                remindPosition = 0;
                invalidate();
            }
            return super.onTouchEvent(event);
        }
        if (remindRect == null) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                getTouchPosition(x);
                int remindX = (int) (lineChartStartX + remindPosition * mSpacing);
                if ((remindX - remindWidth - dp2px(20)) > 0) {
                    remindRect.left = remindX - remindWidth - dp2px(20);
                } else {
                    remindRect.left = remindX + dp2px(20);
                }
                if ((y - remindHeight - dp2px(20)) > 0) {
                    remindRect.top = y - remindHeight - dp2px(20);
                } else {
                    remindRect.top = y + dp2px(20);
                }
                //边界值处理，防止提示框超出绘制范围
                if (remindRect.left < drawRange.left) {
                    remindRect.left = drawRange.left;
                }
                if ((remindRect.left + remindWidth) > drawRange.right) {
                    remindRect.left = drawRange.right - remindWidth;
                }
                if ((remindRect.top + remindHeight) > drawRange.bottom) {
                    remindRect.top = drawRange.bottom - remindHeight;
                }
                if (remindRect.top < drawRange.top) {
                    remindRect.top = drawRange.top;
                }
                if ((remindRect.top + remindHeight) > drawRange.bottom) {
                    remindRect.top = drawRange.bottom - remindHeight;
                }
                remindRect.right = remindRect.left + remindWidth;
                remindRect.bottom = remindRect.top + remindHeight;
                showRemind = true;
                invalidate();
                break;
        }
        return true;

    }

    private void getTouchPosition(float x) {
        int position = (int) ((x - lineChartStartX) / mSpacing);
        double index = (x - lineChartStartX) % mSpacing;
        if (index > (mSpacing / 2)) {
            position++;
        }

        if (position >= params.getxAxis().getData().size()) {
            position = params.getxAxis().getData().size() - 1;
        }

        remindPosition = position;
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

        List<String> data = params.getxAxis().getData();
        String startStr;
        String endStr;
        if (data != null && data.size() > 0) {
            startStr = data.get(0);
            endStr = data.get(data.size() - 1);
        } else {
            throw new IllegalArgumentException("The date list can't be null");
        }
        lineChartStartX = (int) (
                zeroPoint.x + dp2px(6) + getFontWidth(mTextPaint, startStr) / 2);
        lineChartEndX = (int) (drawRange.right - dp2px(2) -
                getFontWidth(mTextPaint, endStr) / 2);

        needAnimation = false;

        //计算x轴 间隔
        mSpacing = 0;
        if (mXAxisEqual > 1) {
            mSpacing = (lineChartEndX - lineChartStartX) / (mXAxisEqual - 1);
        }
        LineChartParams.XAxisBean xAxisBean = params.getxAxis();
        xAxisBean.setDrawPoint(new ArrayList<Point>());
        int xAxisTextHeight = (int) (zeroPoint.y + xAxisRange / 2 + getFontBaseLine(mTextPaint));
        for (int i = 0; i < data.size(); i++) {
            Point point = new Point();
            point.x = (int) (lineChartStartX + i * mSpacing
                    - getFontWidth(mTextPaint, data.get(i)) / 2);
            point.y = xAxisTextHeight;
            xAxisBean.getDrawPoint().add(point);
        }

        //计算y轴高度/单位
        unit = 0;
        if (yMaxValue > 0) {
            unit = (zeroPoint.y - drawRange.top - mLeaveBlank) / (double) yMaxValue;
        }
        for (LineChartParams.SeriesBean series : params.getSeries()) {
            series.setDrawPoint(new ArrayList<Point>());
            for (int i = 0; i < series.getData().size(); i++) {
                Point point = new Point();
                point.x = (int) (lineChartStartX + i * mSpacing);
                point.y = (int) (zeroPoint.y - unit * series.getData().get(i));
                series.getDrawPoint().add(point);
            }
        }

        showRemind = false;
        remindRect = new RectF();
        remindWidth = 0;
        remindHeight = getFontHeight(remindTextPaint) + dp2px(5F);

        for (String datum : params.getxAxis().getData()) {
            remindWidth = Math.max(remindWidth, getFontWidth(remindTextPaint, datum + ":" + yMaxValue));
        }
        for (LineChartParams.SeriesBean series : params.getSeries()) {
            remindWidth = Math.max(remindWidth,
                    getFontWidth(remindTextPaint, series.getName() + ":" + yMaxValue));
        }
        remindWidth += dp2px(19F);

        for (int i = 0; i < params.getSeries().size(); i++) {
            remindHeight += getFontHeight(remindTextPaint) + dp2px(5F);
        }
        remindHeight += dp2px(5F);

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


}