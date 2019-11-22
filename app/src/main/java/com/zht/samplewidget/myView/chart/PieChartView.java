package com.zht.samplewidget.myView.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.zht.samplewidget.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：zhanghaitao on 2017/11/28 10:06
 * 邮箱：820159571@qq.com
 *
 * @describe:饼状图
 */

public class PieChartView extends View {

    private static final String TAG = "PieChartView";

    private int wrap_content = 200;//默认的尺寸（px）

    //视图的宽高
    private int viewWidth;
    private int viewHeight;

    //通用的画笔
    private Paint commonPaint;
    //滤镜，用于画布层面抗锯齿
    private DrawFilter mDrawFilter;

    //view的中心点
    private int center;
    private int centerX;
    private int centerY;

    private float centerCircleRadius = 0;
    //扇形间隔
    private float intervalAngle = -1; //间隔距离要小于 360/mParams.size()
    //展示的宽度
    private float pieChartWidth = -1;//
    //点击的宽度
    private float clickWidth = -1;

    private List<Params> mParams = new ArrayList<>();
    private float pieChartRadius = -1;
    private Paint mLinePaint;

    //这个值用于解决扇形点击放大后，会在边缘残留颜色，此处设置为固定值，不建议修改
    private int optimizedPadding = 2;
    private RectF normalOval;
    private RectF clickOval;
    private float cutLength = 0;
    private boolean equalInterval;
    private boolean useCenterCircle;
    private float centerSquare;
    private float radiusSquare;
    private float pieChartRadiusSquare;

    //点击事件
    private float x;
    private float y;
    private int clickPosition = -1;

    private String centerText;
    private String titleText;
    private TextPaint mTextPaint;

    public PieChartView(Context context) {
        this(context, null);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化必要参数
        initCofig(context, attrs);
    }


    /**
     * 初始化配置
     *
     * @param context
     * @param attrs
     */
    private void initCofig(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PieChartView);
            intervalAngle = typedArray.getFloat(R.styleable.PieChartView_intervalAngle, 0);
            //  centerCircleRadius = typedArray.getDimension(R.styleable.PieChartView_centerCircleRadius, -1);
            pieChartWidth = typedArray.getDimension(R.styleable.PieChartView_pieChartWidth, dp2px(30));
            clickWidth = typedArray.getDimension(R.styleable.PieChartView_clickWidth, dp2px(5));
            equalInterval = typedArray.getBoolean(R.styleable.PieChartView_equalInterval, false);
            useCenterCircle = typedArray.getBoolean(R.styleable.PieChartView_useCenterCircle, true);
            typedArray.recycle();
        }

        if (clickWidth == -1) {
            clickWidth = dp2px(5);
        }
        if (pieChartWidth == -1) {
            pieChartWidth = dp2px(30);
        }
        if (intervalAngle == -1) {
            intervalAngle = 0;
        }
        //初始化画笔
        initPaint();

        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        commonPaint = new Paint();
        commonPaint.setAntiAlias(true);//去锯齿
        commonPaint.setStyle(Paint.Style.FILL);//设置样式

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(1);
        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinePaint.setStrokeJoin(Paint.Join.ROUND);//圆弧
        mLinePaint.setColor(0xFFFF0000);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.density = getContext().getResources().getDisplayMetrics().density;
        mTextPaint.setTextSize(dp2px(10));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**在这里可以设置数据+获取控件的宽高*/
        //设置宽高
        setMeasuredDimension(
                measureMeasureSpec(widthMeasureSpec),
                measureMeasureSpec(heightMeasureSpec));
    }

    private int measureMeasureSpec(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.AT_MOST) {
            return wrap_content; //默认wrap_content为自己定义的高度
        } else if (specMode == MeasureSpec.UNSPECIFIED) {
            return wrap_content; //默认wrap_content为自己定义的高度
        } else {
            return specSize;
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        viewWidth = w;
        viewHeight = h;
        initData();
        if (mParams.size() > 0) {
            onDataChanged();
        }
    }

    private void initData() {
        //计算中心点和 最短边距
        if (centerX == 0) {
            centerX = viewWidth / 2;
        }
        if (centerY == 0) {
            centerY = viewHeight / 2;
        }
        center = Math.min(centerX, centerY);

        //扇形半径
        pieChartRadius = center - clickWidth - optimizedPadding;

        // center = optimizedPadding + clickWidth + pieChartWidth + centerCircleRadius;
        // pieChartRadius =  pieChartWidth + centerCircleRadius;

        //使用中心圆 计算中间圆半径
        if (useCenterCircle) {
            centerCircleRadius = pieChartRadius - pieChartWidth;
            if (centerCircleRadius <= 0) {
                centerCircleRadius = 0;
            }
        } else {
            centerCircleRadius = 0;
        }

        centerSquare = center * center;
        radiusSquare = centerCircleRadius * centerCircleRadius;
        pieChartRadiusSquare = pieChartRadius * pieChartRadius;

        //初始化默认扇形的绘画 区域
        if (normalOval == null) {
            normalOval = new RectF(
                    centerX - pieChartRadius,
                    centerY - pieChartRadius,
                    centerX + pieChartRadius,
                    centerY + pieChartRadius
            );
        }
        //初始化点击扇形的绘画 区域
        if (clickOval == null) {
            clickOval = new RectF(
                    centerX - center + optimizedPadding,
                    centerY - center + optimizedPadding,
                    centerX + center - optimizedPadding,
                    centerY + center - optimizedPadding
            );
        }

    }

    private void onDataChanged() {
        //计算总共数值
        float totalScale = 0;
        for (Params params : mParams) {
            totalScale += params.getScale();
        }

        //计算可用的最大角度
        int totalAngle;
        if (mParams.size() > 1) {
            totalAngle = (int) (360 - intervalAngle * mParams.size());
        } else {
            totalAngle = 360;
        }

        if (totalAngle <= 310) {
            totalAngle = 310;
            intervalAngle = 50F / mParams.size();
        }

        //当间隙角度有值的时候才计算
        if (intervalAngle > 0) {
            //弧长的计算方式：L=n（圆心角度数）× π（3.14）× r（半径）/180（角度制）
            //double intervalWidth = intervalAngle * Math.PI * pieChartRadius / 180;
            //两个扇形之间最大的距离：L = 2 * r * sin（θ/2）
            double intervalWidth = 2 * pieChartRadius * Math.sin(Math.PI * intervalAngle / 360);
            mLinePaint.setStrokeWidth((float) intervalWidth);
        }

        //计算每一个扇形的开始角度和结束角度
        float startAngle = 0;
        for (Params params : mParams) {

            float scale = totalAngle * params.getScale() / totalScale;
            params.setStartAngle(startAngle);
            params.setEndAngle(startAngle + scale);

            startAngle += (scale + intervalAngle);

            if (equalInterval) {

                float cutAngle = (float) (Math.PI * (scale + intervalAngle) / 360);
                float cutScale = (float) (Math.PI * scale / 360);
                float length = (float) (pieChartRadius * (Math.cos(cutScale)
                        - Math.sin(cutScale) / Math.tan(cutAngle)));
                cutLength = Math.max(cutLength, length);

                float pointX = computePointX(startAngle - intervalAngle / 2, centerX, pieChartRadius);
                float pointY = computePointY(startAngle - intervalAngle / 2, centerY, pieChartRadius);
                params.setPointX(pointX);
                params.setPointY(pointY);
            }
        }
    }


    private float computePointX(float angle, float centerX, float radius) {
        float pointX;
        if (angle == 0 || angle == 360) {
            pointX = centerX + radius;
        } else if (angle == 90 || angle == 270) {
            pointX = centerX;
        } else if (angle == 180) {
            pointX = 0;
        } else {
            pointX = (float) (centerX + radius * Math.cos(Math.PI * angle / 180));
        }
        return pointX;
    }

    private float computePointY(float angle, float centerY, float radius) {
        float pointY;
        if (angle == 0 || angle == 360 || angle == 180) {
            pointY = centerY;
        } else if (angle == 90) {
            pointY = centerY + radius;
        } else if (angle == 270) {
            pointY = 0;
        } else {
            pointY = (float) (centerY + radius * Math.sin(Math.PI * angle / 180));
        }
        return pointY;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 从canvas层面去除绘制时锯齿
        canvas.setDrawFilter(mDrawFilter);

        //绘制扇形图
        int position = 0;
        for (Params params : mParams) {
            commonPaint.setColor(params.getDrawColor());
            if (position == clickPosition) {
                canvas.drawArc(clickOval, params.getStartAngle(),
                        params.getEndAngle() - params.getStartAngle(), true, commonPaint);
            } else {
                canvas.drawArc(normalOval, params.getStartAngle(),
                        params.getEndAngle() - params.getStartAngle(),
                        true, commonPaint);
            }
            position++;
        }
        //绘制等宽的间隔线
        if (equalInterval) {
            for (Params params : mParams) {
                Path path = new Path();
                path.moveTo(centerX, centerY);
                path.lineTo(params.getPointX(), params.getPointY());
                canvas.drawPath(path, mLinePaint);
            }
        }
        //绘制中心白圆
        if (useCenterCircle && mParams.size() > 0) {
            if (centerCircleRadius > cutLength) {
                commonPaint.setColor(0xFFFFFFFF);
                canvas.drawCircle(centerX, centerY, centerCircleRadius, commonPaint);
            } else {
                commonPaint.setColor(0xFFFFFFFF);
                canvas.drawCircle(centerX, centerY, (0.1F * pieChartRadius + 0.9F * cutLength), commonPaint);
            }
//            commonPaint.setColor(0xFFFFFFFF);
//            canvas.drawCircle(centerX, centerY, centerCircleRadius, commonPaint);

            //X
//            Path path = new Path();
//            path.moveTo(0, centerY);
//            path.lineTo(centerX * 2, centerY);
//            canvas.drawPath(path, mLinePaint);

            //Y
//            path.moveTo(centerX, 0);
//            path.lineTo(centerX, centerY * 2);
//            canvas.drawPath(path, mLinePaint);


            canvas.drawText(centerText,
                    0, centerText.length(),
                    centerX - getFontWidth(mTextPaint, centerText.toString().trim()) / 2,
                    centerY + getFontBaseLine(mTextPaint),
                    mTextPaint);

            //X点
//            canvas.drawLine(
//                    centerX - getFontWidth(mTextPaint, centerText.toString().trim()) / 2,
//                    0,
//                    centerX - getFontWidth(mTextPaint, centerText.toString().trim()) / 2,
//                    centerY * 2,
//                    mLinePaint);
            //Y点
//            canvas.drawLine(
//                    0,
//                    centerY + getFontBaseLine(mTextPaint),
//                    centerX * 2,
//                    centerY + getFontBaseLine(mTextPaint),
//                    mLinePaint);


        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (itemClickListener == null) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (x == event.getX()
                        && y == event.getY()) {
                    float clickX = x - centerX;
                    float clickY = y - centerY;
                    float pointSquare = clickX * clickX + clickY * clickY;
                    if (pointSquare < radiusSquare) {
                        return false;
                    }
                    if (pointSquare <= centerSquare) {
                        double angle = getAngle(clickX, clickY);
                        int position = 0;
                        for (Params mParam : mParams) {
                            //在点击范围
                            if (angle >= mParam.getStartAngle() && angle <= mParam.getEndAngle()) {

                                if (position == clickPosition) {
                                    clickPosition = -1;
                                    invalidate();
                                    if (itemClickListener != null) {
                                        itemClickListener.onPieChartItemClick(false, position);
                                    }
                                } else if (pointSquare <= pieChartRadiusSquare) {
                                    clickPosition = position;
                                    invalidate();
                                    if (itemClickListener != null) {
                                        itemClickListener.onPieChartItemClick(true, position);
                                    }
                                }
                                break;
                            }
                            position++;
                        }
                    }
                    return true;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private double getAngle(float clickX, float clickY) {

        double angle;
        if (clickX == 0 && clickY > 0) {
            return 90;
        } else if (clickX == 0 && clickY < 0) {
            return 270;
        } else if (clickX == 0 && clickY == 0) {
            return -1;
        }
        angle = Math.toDegrees(Math.atan(clickY / clickX));
        if (clickX < 0) {//第二、三象限
            angle = angle + 180;
        } else if (clickX > 0 && clickY < 0) {//第四象限
            angle = angle + 360;
        }
        return angle;
//        if (clickX > 0 && clickY > 0) {//第一象限
//            return angle;
//        } else if (clickX < 0 && clickY > 0) {//第二象限
//            angle = angle + 180;
//        } else if (clickX < 0 && clickY < 0) {//第三象限
//            angle = angle + 180;
//        } else if (clickX > 0 && clickY < 0) {//第四象限
//            angle = angle + 360;
//        }
    }


    /**
     *  dp转px  
     *  @param context 
     *  @param val 
     *  @return      
     */
    public int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getContext().getResources().getDisplayMetrics());
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
     * @return 返回指定的文字基线相对于中心点的距离
     */
    public float getFontBaseLine(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return -(fm.ascent + fm.descent) / 2;
    }

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onPieChartItemClick(boolean isSelected, int position);
    }


    public static class Params {

        //描述
        private String des;
        //占比
        private float scale;
        //绘制的颜色
        private int drawColor = Color.RED;
        private float startAngle;
        private float endAngle;

        private float pointX;
        private float pointY;

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public float getScale() {
            return scale;
        }

        public void setScale(float scale) {
            this.scale = scale;
        }

        public int getDrawColor() {
            return drawColor;
        }

        public void setDrawColor(@ColorInt int drawColor) {
            this.drawColor = drawColor;
        }

        public float getStartAngle() {
            return startAngle;
        }

        public void setStartAngle(float startAngle) {
            this.startAngle = startAngle;
        }

        public float getEndAngle() {
            return endAngle;
        }

        public void setEndAngle(float endAngle) {
            this.endAngle = endAngle;
        }

        public float getPointX() {
            return pointX;
        }

        public void setPointX(float pointX) {
            this.pointX = pointX;
        }

        public float getPointY() {
            return pointY;
        }

        public void setPointY(float pointY) {
            this.pointY = pointY;
        }
    }

    public String getCenterText() {
        return centerText;
    }

    public void setCenterText(String centerText) {
        this.centerText = centerText;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public int getOptimizedPadding() {
        return optimizedPadding;
    }

    public void setOptimizedPadding(int optimizedPadding) {
        this.optimizedPadding = optimizedPadding;
    }

    public boolean isEqualInterval() {
        return equalInterval;
    }

    public void setEqualInterval(boolean equalInterval) {
        this.equalInterval = equalInterval;
    }

    public boolean isUseCenterCircle() {
        return useCenterCircle;
    }

    public void setUseCenterCircle(boolean useCenterCircle) {
        this.useCenterCircle = useCenterCircle;
    }

    public float getCenterCircleRadius() {
        return centerCircleRadius;
    }

    public void setCenterCircleRadius(float centerCircleRadius) {
        this.centerCircleRadius = centerCircleRadius;
    }

    public float getIntervalAngle() {
        return intervalAngle;
    }

    public void setIntervalAngle(float intervalAngle) {
        this.intervalAngle = intervalAngle;
    }

    public float getPieChartWidth() {
        return pieChartWidth;
    }

    public void setPieChartWidth(float pieChartWidth) {
        this.pieChartWidth = pieChartWidth;
    }

    public float getClickWidth() {
        return clickWidth;
    }

    public void setClickWidth(float clickWidth) {
        this.clickWidth = clickWidth;
    }

    public List<Params> getParams() {
        return mParams;
    }

    public void setParams(List<Params> params) {
        mParams.clear();
        mParams.addAll(params);
        clickPosition = -1;
        onDataChanged();
        invalidate();
    }
}
