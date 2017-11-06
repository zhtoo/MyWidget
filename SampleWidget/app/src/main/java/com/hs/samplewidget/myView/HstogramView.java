package com.hs.samplewidget.myView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.hs.samplewidget.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author： zht on 2017/11/3 22:32
 * @email： 820159571@qq.com
 * 这是一个轻量级的直方图的控件
 * 1、传入X、Y轴的坐标 可以自动生成对应的比例，绘制到布局上
 * 2、根据布局的大小自适应
 * 3、点击显示Y轴的数值，改变当前列的颜色
 */
public class HstogramView extends View {

    private static final String TAG = "HstogramView";
    private int wrap_content = 800;

    private Paint xy_axisPaint;
    private Paint y_axisValuePaint;
    private Paint mTextPaint;
    private Paint barChartsPaint;
    private Paint barChartsClickPaint;

    //需要由外面传进来的数据
    private String[] date = new String[]{"11-01", "11-02", "11-03", "11-04", "11-05", "11-06", "11-07"};
    private int[] values = new int[]{20, 40, 60, 80, 100, 88, 45};

    //---------------------可以自定义的属性start-------------------------------------------------------

    //默认Y轴5等分
    private int Equal = 5;
    //渐变颜色选择
    private int[] colors = {Color.argb(255, 23, 163, 219), Color.argb(255, 23, 140, 219)};
    //视图间距默认40
    private int Spacing = 40;


    //---------------------可以自定义的属性end-------------------------------------------------------

    //最大的Y值
    private int maxY = -1;
    //默认被点击的条目是-1
    private int clickItemPosion = -1;
    private List<PointRange> PointRangeList = new ArrayList<>();

    /**
     * 试图的宽高
     */
    private int viewWidth;
    private int viewHeight;

    private float columnWidth;


    public HstogramView(Context context) {
        this(context, null);
    }

    public HstogramView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HstogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context);
    }

    private void initPaint(Context context) {
        //1、画X、Y轴的线
        xy_axisPaint = new Paint();
        xy_axisPaint.setAntiAlias(true);
        xy_axisPaint.setStrokeWidth(2);//2个像素点
        xy_axisPaint.setColor(Color.argb(255, 0, 0, 0));//黑色
        xy_axisPaint.setStyle(Paint.Style.STROKE);//描边
        xy_axisPaint.setStrokeCap(Paint.Cap.ROUND);// 圆形线帽

        // 2、画Y轴的线（点击的时候）
        y_axisValuePaint = new Paint();
        y_axisValuePaint.setAntiAlias(true);
        y_axisValuePaint.setStrokeWidth(5);
        y_axisValuePaint.setColor(Color.argb(255, 23, 140, 219));
        y_axisValuePaint.setStyle(Paint.Style.STROKE);//描边
        y_axisValuePaint.setStrokeCap(Paint.Cap.ROUND);// 圆形线帽

        //3、画文字+数值显示
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(DensityUtils.sp2px(context, 9));//字体大小
        mTextPaint.setTypeface(Typeface.DEFAULT);

        //4、画默认的柱形图
        barChartsPaint = new Paint();
        barChartsPaint.setAntiAlias(true);//防锯齿
        barChartsPaint.setColor(Color.GRAY);
        barChartsPaint.setStyle(Paint.Style.FILL_AND_STROKE);//充满并且描边
        barChartsPaint.setStrokeCap(Paint.Cap.ROUND);

        //5、画点击的柱形图
        barChartsClickPaint = new Paint();
        barChartsClickPaint.setAntiAlias(true);//防锯齿
        barChartsClickPaint.setStrokeWidth(20);//宽度
        barChartsClickPaint.setColor(Color.GREEN);
        barChartsClickPaint.setStyle(Paint.Style.FILL_AND_STROKE);//充满并且描边
        barChartsClickPaint.setStrokeCap(Paint.Cap.ROUND);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**在这里可以设置+获取控件的宽高*/
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        viewWidth = width;
        viewHeight = height;
        //设置宽高
        setMeasuredDimension(width, height);
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

        /**计算最大的值*/
        if (maxY == -1) {
            int maxValue = 0;
            for (int value : values) {
                if (value > maxValue) {
                    maxValue = value;
                }
            }
            if ((maxValue % (Equal * 10)) == 0) {
                maxY = maxValue;
            } else {
                maxY = ((maxValue / (Equal * 10)) + 1) * Equal * 10;
            }
        }

        columnWidth = viewWidth / (date.length + 1);

        float maxTextWeith = getFontWidth(mTextPaint, date[0]);
        float maxTextHeight = getFontHeight(mTextPaint);
        //Y轴的高度
        float heightY = viewHeight - maxTextHeight;
        //X轴的宽度
        float weithX = viewWidth - maxTextWeith;
        float unitLengthY = heightY / maxY;
        int dY = maxY / Equal;
        float dHeight = heightY / Equal;


        //画轴线
        canvas.drawLine(columnWidth-Spacing, 0, columnWidth-Spacing, heightY, xy_axisPaint);//y
        canvas.drawLine(columnWidth-Spacing, heightY, viewWidth, heightY, xy_axisPaint);//x

        //画刻度
        for (int i = 0; i < (Equal + 1); i++) {
            float mValueWeith = getFontWidth(mTextPaint, String.valueOf(maxY - dY * i));
            float X = 0;
            if (maxTextWeith - mValueWeith >= 0) {
                X = columnWidth-Spacing - mValueWeith - 10;
            }
            if (i == 0) {
                canvas.drawText(String.valueOf(maxY - dY * i), X, maxTextHeight / 2 + 10, mTextPaint);
            } else {
                canvas.drawText(String.valueOf(maxY - dY * i), X, dHeight * i + maxTextHeight / 4, mTextPaint);
            }
        }


        //画数值的虚线
        if (clickItemPosion != -1) {
            Path path = new Path();
            path.moveTo(columnWidth-Spacing + 5, heightY - values[clickItemPosion] * unitLengthY);
            path.lineTo(viewWidth, heightY - values[clickItemPosion] * unitLengthY);
            //绘制虚线效果
            PathEffect effects = new DashPathEffect(new float[]{1, 2, 4, 8}, 1);
            y_axisValuePaint.setPathEffect(effects);
            canvas.drawPath(path, y_axisValuePaint);
        }

        //画柱状图
        for (int i = 0; i < values.length; i++) {
            PointRange pointRange;
            if (PointRangeList.size() < values.length) {
                pointRange = new PointRange();
                pointRange.minX = columnWidth * (i + 1);
                pointRange.maxX = columnWidth * (i + 2) - Spacing;
                pointRange.minY = heightY - values[i] * unitLengthY;
                pointRange.maxY = heightY - 2;
                PointRangeList.add(pointRange);
            } else {
                pointRange = PointRangeList.get(i);
            }
            //画X轴的日期
            float textWidth = getFontWidth(mTextPaint, String.valueOf(date[i]));
            float centerPoint = (pointRange.minX + pointRange.maxX) / 2;
            canvas.drawText(date[i], centerPoint - textWidth / 2, viewHeight - 5, mTextPaint);

            if (i == clickItemPosion) {
                // 一个材质,打造出一个线性梯度沿著一条线。
                Shader mShader = new LinearGradient(pointRange.minX, pointRange.minY,
                        pointRange.maxX, pointRange.maxY,
                        colors, null /*new float[]{0,1}对应的颜色只能有两个*/,
                        Shader.TileMode.MIRROR);
                barChartsClickPaint.setShader(mShader);
                canvas.drawRect(pointRange.minX,//距离左边
                        pointRange.minY + 10,//距离上边
                        pointRange.maxX, //距离右边
                        pointRange.maxY - 10, //距离底部
                        barChartsClickPaint);
            } else {
                canvas.drawRect(pointRange.minX,//距离左边
                        pointRange.minY,//距离上边
                        pointRange.maxX, //距离右边
                        pointRange.maxY, //距离底部
                        barChartsPaint);
            }

        }

        //画文字
        if (clickItemPosion != -1) {
            PointRange pointRange = PointRangeList.get(clickItemPosion);
            float valueTextWidth = getFontWidth(mTextPaint, String.valueOf(values[clickItemPosion]));
            float centerWidth = (pointRange.minX + pointRange.maxX) / 2;
            if ((pointRange.minY - 5) < maxTextHeight) {
                canvas.drawText(String.valueOf(values[clickItemPosion]),
                        centerWidth - valueTextWidth/2,
                        pointRange.minY + maxTextHeight * 3 / 4,
                        mTextPaint);
            } else {
                canvas.drawText(String.valueOf(values[clickItemPosion]),
                        centerWidth - valueTextWidth/2,
                        pointRange.minY - 5,
                        mTextPaint);
            }
        }
    }

    class PointRange {
        private float minX;
        private float maxX;
        private float minY;
        private float maxY;
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
     * @return 返回指定的文字高度
     */
    public float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        //文字基准线的下部距离-文字基准线的上部距离 = 文字高度
        return fm.descent - fm.ascent;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float downX = event.getX();
                float downY = event.getY();
                for (int i = 0; i < PointRangeList.size(); i++) {
                    PointRange pointRange = PointRangeList.get(i);
                    if (downX > pointRange.minX && downX < pointRange.maxX &&
                            downY > pointRange.minY && downY < pointRange.maxY) {
                        clickItemPosion = i;
                        invalidate();
                        return true;
                    }
                }
                if (clickItemPosion != -1) {
                    clickItemPosion = -1;
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float x = event.getX();
                float y = event.getY();
                for (int i = 0; i < PointRangeList.size(); i++) {
                    PointRange pointRange = PointRangeList.get(i);
                    if (x > pointRange.minX && x < pointRange.maxX &&
                            y > pointRange.minY && y < pointRange.maxY) {
                        if (listener != null) {
                            listener.onItemClick(i);
                        }
                        return true;
                    }
                }
                break;

        }
        return super.onTouchEvent(event);
    }


    public static onItemClickListener listener;

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public interface onItemClickListener {
        void onItemClick(int postion);
    }

}
