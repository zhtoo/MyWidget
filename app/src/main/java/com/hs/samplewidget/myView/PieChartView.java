package com.hs.samplewidget.myView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.hs.samplewidget.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作者：zhanghaitao on 2017/11/28 10:06
 * 邮箱：820159571@qq.com
 *
 * @describe:饼状图
 */

public class PieChartView extends View {

    private static final String TAG = "PieChartView";

    private Context context;

    private int wrap_content = 600;//默认的尺寸（px）

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
    //中间圆半径
    private float radius = -1;
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

    public PieChartView(Context context) {
        this(context, null);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
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
            intervalAngle = typedArray.getFloat(R.styleable.PieChartView_intervalAngle, 2);
            radius = typedArray.getDimension(R.styleable.PieChartView_radius, -1);
            pieChartWidth = typedArray.getDimension(R.styleable.PieChartView_pieChartWidth, dp2px(30));
            clickWidth = typedArray.getDimension(R.styleable.PieChartView_clickWidth, dp2px(5));
            typedArray.recycle();
        }

        if (clickWidth == -1) {
            clickWidth = dp2px(5);
        }
        if (pieChartWidth == -1) {
            pieChartWidth = dp2px(30);
        }
        if (intervalAngle == -1) {
            intervalAngle = 2;
        }
        //初始化画笔
        initPaint();
        if (mParams.size() == 0) {
            for (int i = 0; i < 10; i++) {
                Params params = new Params();
                params.setDes("我是" + i);
                params.setScale((float) Math.random() * 1000F + 10);
                int red = (int) (20 + Math.random() * 200);
                int green = (int) (20 + Math.random() * 200);
                int bule = (int) (20 + Math.random() * 200);
                int color = Color.argb(255, red, green, bule);
                params.setDrawColor(color);
                mParams.add(params);
            }
        }
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        commonPaint = new Paint();
        commonPaint.setAntiAlias(true);//去锯齿
        commonPaint.setStyle(Paint.Style.FILL);//设置样式
        // commonPaint.setStrokeCap(Paint.Cap.BUTT);//
//        commonPaint.setStrokeWidth(mCircleWidth);//设置宽度
//        commonPaint.setColor(backgroundColor);//设置颜色

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(5);
        //mLinePaint.setTypeface(Typeface.DEFAULT);
        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinePaint.setStrokeJoin(Paint.Join.ROUND);//圆弧
        mLinePaint.setColor(0xFFFFFFFF);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**在这里可以设置数据+获取控件的宽高*/
        viewWidth = measureMeasureSpec(widthMeasureSpec);
        viewHeight = measureMeasureSpec(heightMeasureSpec);
        //设置宽高
        setMeasuredDimension(viewWidth, viewHeight);
    }

    private int measureMeasureSpec(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.AT_MOST || specMode == MeasureSpec.UNSPECIFIED) {
            return wrap_content; //默认wrap_content为自己定义的高度
        } else {
            return specSize;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 从canvas层面去除绘制时锯齿
        canvas.setDrawFilter(mDrawFilter);
        if (centerX == 0) {
            centerX = viewWidth / 2;
        }
        if (centerY == 0) {
            centerY = viewHeight / 2;
        }

        center = Math.min(centerX, centerY);

        if (radius == -1) {
            pieChartRadius = center - clickWidth - optimizedPadding;
            radius = pieChartRadius - pieChartWidth;
        } else if (pieChartRadius == -1) {
            pieChartRadius = radius + pieChartWidth;
        }
        if (normalOval == null) {
            normalOval = new RectF(
                    centerX - pieChartRadius,
                    centerY - pieChartRadius,
                    centerX + pieChartRadius,
                    centerY + pieChartRadius
            );
        }

        if (clickOval == null) {
            clickOval = new RectF(
                    centerX - center + optimizedPadding,
                    centerY - center + optimizedPadding,
                    centerX + center - optimizedPadding,
                    centerY + center - optimizedPadding
            );
        }


        float totalScale = 0;
        for (Params params : mParams) {
            totalScale += params.getScale();
        }
        int totalAngle = (int) (360 - intervalAngle * mParams.size());

        float startAngle = 0;
        for (Params params : mParams) {
            commonPaint.setColor(params.getDrawColor());
            float scale = totalAngle * params.getScale() / totalScale;
            params.setStartAngle(startAngle);
            params.setEndAngle(startAngle + scale);
            if (des != null && des.equals(params.getDes())) {
                canvas.drawArc(clickOval, startAngle, scale, true, commonPaint);
            } else {
                canvas.drawArc(normalOval, startAngle, scale, true, commonPaint);
            }
            startAngle += (scale + intervalAngle);
        }

        //弧长的计算方式：L=n（圆心角度数）× π（3.14）× r（半径）/180（角度制）
        //double intervalWidth = intervalAngle * Math.PI * pieChartRadius / 180;
        //两个扇形之间最大的距离：L = 2 * r * sin（θ/2）
        double intervalWidth = 2 * pieChartRadius * Math.sin(Math.PI * intervalAngle / 360);
        mLinePaint.setStrokeWidth((float) intervalWidth);

        float cutLength = 0;

        for (Params params : mParams) {
            float scale = totalAngle * params.getScale() / totalScale;
            float cutAngle = (float) (Math.PI * (scale + intervalAngle) / 360);
            float cutScale = (float) (Math.PI * scale / 360);

            float length = (float) (pieChartRadius * (Math.cos(cutScale) -
                                Math.sin(cutScale) / Math.tan(cutAngle)));

            Log.e(TAG, "length: " + length);
            cutLength =  Math.max(cutLength,length);
        }
        Log.e(TAG, "cutLength: " + cutLength);

        for (Params params : mParams) {
            float angle = params.getEndAngle() + intervalAngle / 2;
            float pointX = 0, pointY = 0;
            if (angle == 0 || angle == 360) {
                pointX = centerX * 2;
                pointY = centerY;
            } else if (angle == 90) {
                pointX = centerX;
                pointY = centerY * 2;
            } else if (angle == 180) {
                pointX = 0;
                pointY = centerY;
            } else if (angle == 270) {
                pointX = centerX;
                pointY = 0;
            } else if (angle > 0 && angle < 90) {
                pointX = (float) (centerX + pieChartRadius * Math.abs(Math.cos(Math.PI * angle / 180)));
                pointY = (float) (centerY + pieChartRadius * Math.abs(Math.sin(Math.PI * angle / 180)));
            } else if (angle > 90 && angle < 180) {
                pointX = (float) (centerX - pieChartRadius * Math.abs(Math.cos(Math.PI * angle / 180)));
                pointY = (float) (centerY + pieChartRadius * Math.abs(Math.sin(Math.PI * angle / 180)));
            } else if (angle > 180 && angle < 270) {
                pointX = (float) (centerX - pieChartRadius * Math.abs(Math.cos(Math.PI * angle / 180)));
                pointY = (float) (centerY - pieChartRadius * Math.abs(Math.sin(Math.PI * angle / 180)));
            } else if (angle > 270 && angle < 360) {
                pointX = (float) (centerX + pieChartRadius * Math.abs(Math.cos(Math.PI * angle / 180)));
                pointY = (float) (centerY - pieChartRadius * Math.abs(Math.sin(Math.PI * angle / 180)));
            }
            Path path = new Path();
            path.moveTo(centerX, centerY);
            path.lineTo(pointX, pointY);
            canvas.drawPath(path, mLinePaint);
        }


//        if (radius > 0 ) {
//            commonPaint.setColor(0xFFFFFFFF);
//            canvas.drawCircle(centerX, centerY, radius, commonPaint);
//        }else  if (cutLength > 0 ){
//            commonPaint.setColor(0xFFFFFFFF);
//            canvas.drawCircle(centerX, centerY, cutLength, commonPaint);
//        }
    }

    private String des = null;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        float pointX = x - centerX;
        float pointY = y - centerY;

        float pointSquare = pointX * pointX + pointY * pointY;
        float centerSquare = center * center;
        float radiusSquare = radius * radius;
        float pieChartRadiusSquare = pieChartRadius * pieChartRadius;

        if (pointSquare < radiusSquare) {
            return super.onTouchEvent(event);
        }

        if (pointSquare <= centerSquare) {
            double angle = getAngle(pointX, pointY);
            for (Params mParam : mParams) {
                //在点击范围
                if (angle >= mParam.getStartAngle() && angle <= mParam.getEndAngle()) {
                    if (des == null && pointSquare <= pieChartRadiusSquare) {
                        des = mParam.getDes();
                        invalidate();
                    } else if (des != null && des.equals(mParam.getDes())) {
                        des = null;
                        invalidate();
                    } else if (pointSquare <= pieChartRadiusSquare) {
                        des = mParam.getDes();
                        invalidate();
                    }
                    break;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private double getAngle(float pointX, float pointY) {
        double angle;
        if (pointX == 0 && pointY > 0) {
            return 90;
        } else if (pointX == 0 && pointY < 0) {
            return 270;
        }
        angle = Math.toDegrees(Math.atan(pointY / pointX));
        if (pointX > 0 && pointY > 0) {//第一象限
            angle = angle;
        } else if (pointX < 0 && pointY > 0) {//第二象限
            angle = angle + 180;
        } else if (pointX < 0 && pointY < 0) {//第三象限
            angle = angle + 180;
        } else if (pointX > 0 && pointY < 0) {//第四象限
            angle = angle + 360;
        }
        return angle;
    }

    class Params {

        //描述
        private String des;
        //占比
        private float scale;
        //绘制的颜色
        private int drawColor = Color.RED;
        private float startAngle;
        private float endAngle;

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
    }

    /**
     *  dp转px  
     *  @param context 
     *  @param val 
     *  @return      
     */
    public int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

}
