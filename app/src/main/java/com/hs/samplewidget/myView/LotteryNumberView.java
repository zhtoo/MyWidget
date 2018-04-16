package com.hs.samplewidget.myView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hs.samplewidget.R;

import java.util.List;

/**
 * Created by zhanghaitao on 2018/3/27.
 */

public class LotteryNumberView extends View {


    //红球
    private String[] redNumber;
    //篮球
    private String[] buleNumber;

    private int wrap_content = 300;//默认的尺寸（px）
    //宽高
    private int viewWidth;
    private int viewHeight;
    private PaintFlagsDrawFilter mDrawFilter;
    private Paint mRedPaint;
    private Paint mBulePaint;
    private Paint mWhitePaint;
    //半径
    private float mRadius;
    //间距
    private float mSpacing;
    private int length;
    private int columnMax;
    private int row = 0;

    public LotteryNumberView(Context context) {
        this(context, null);
    }

    public LotteryNumberView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LotteryNumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //TODO:在这里做初始化
        initCofig(context, attrs);
    }

    private void initCofig(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LotteryNumberView);
        mRadius = typedArray.getDimension(R.styleable.LotteryNumberView_mRadius, 30);
        mSpacing = typedArray.getDimension(R.styleable.LotteryNumberView_mSpacing, 10);
        typedArray.recycle();
        initPaint();
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    private void initPaint() {
        //红色
        mRedPaint = new Paint();
        mRedPaint.setAntiAlias(true);
        mRedPaint.setColor(Color.parseColor("#FF3743"));
        mRedPaint.setStrokeWidth(0);
        mRedPaint.setStyle(Paint.Style.FILL);
        mRedPaint.setStrokeCap(Paint.Cap.ROUND);// 圆形线帽
        mRedPaint.setStrokeJoin(Paint.Join.ROUND);//圆弧

        //蓝色
        mBulePaint = new Paint();
        mBulePaint.setAntiAlias(true);
        mBulePaint.setColor(Color.parseColor("#4FCBFF"));
        //mBulePaint.setTextSize(mTextSize);
        mBulePaint.setStrokeWidth(0);
        mBulePaint.setStyle(Paint.Style.FILL);
        mBulePaint.setStrokeCap(Paint.Cap.ROUND);// 圆形线帽
        mBulePaint.setStrokeJoin(Paint.Join.ROUND);//圆弧
        mBulePaint.setTypeface(Typeface.DEFAULT);

        //白色
        mWhitePaint = new Paint();
        mWhitePaint.setAntiAlias(true);
        mWhitePaint.setColor(Color.parseColor("#FFFFFF"));
        mWhitePaint.setTextSize(30);
        mWhitePaint.setStrokeWidth(0);
        mWhitePaint.setStyle(Paint.Style.FILL);
        mWhitePaint.setStrokeCap(Paint.Cap.ROUND);// 圆形线帽
        mWhitePaint.setStrokeJoin(Paint.Join.ROUND);//圆弧
        mWhitePaint.setTypeface(Typeface.DEFAULT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**在这里可以设置数据+获取控件的宽高*/
        viewWidth = measureWidth(widthMeasureSpec);
        viewHeight = measureHeight(heightMeasureSpec);
        if (mRadius > 0) {
            columnMax = (int) (viewWidth / (mRadius*2 + mSpacing));
            length = getNumberLength(redNumber, buleNumber);
            if (length > columnMax) {//长了
                row = length / columnMax;
                if (length % columnMax != 0) {
                    row++;
                }
            }
        }

        viewHeight = (int) (row * (mRadius * 2 + mSpacing));

        //设置宽高
        setMeasuredDimension(viewWidth, (int) (row * (mRadius * 2 + mSpacing)));
    }

    private int getNumberLength(String[] redNumber, String[] buleNumber) {
        int redLength = 0;
        int blueLength = 0;
        if (redNumber != null && redNumber.length > 0) {
            redLength = redNumber.length;
        }
        if (buleNumber != null && buleNumber.length > 0) {
            blueLength = buleNumber.length;
        }
        return redLength + blueLength;
    }

    private int getNumberLength(String[] redNumber) {
        int redLength = 0;
        if (redNumber != null && redNumber.length > 0) {
            redLength = redNumber.length;
        }
        return redLength;
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
        // 从canvas层面去除绘制时锯齿
        canvas.setDrawFilter(mDrawFilter);

        int centerY = viewHeight / 2;
        float radius = mRadius;
        float spacing = mSpacing;

        //==屏幕适配开始===========================================================================================

        if (row <= 1) {//不需要分行
            //半径长度不能比高度的一半长
            if (centerY < radius) {
                radius = centerY;
            }
            int length = 0;
            if (redNumber != null) {
                length += redNumber.length;
            }
            if (buleNumber != null) {
                length += buleNumber.length;
            }
            //半径长度综合不能比宽度长
            float totalLength = spacing + radius * 2 * length + (length - 1) * spacing;
            //球的半径和间距长了
            if (totalLength > viewWidth) {
                radius = (viewWidth - spacing * length) / length / 2;
            }
            float startX = radius + spacing;
            //绘画红球
            mWhitePaint.setTextSize((float) (Math.sqrt(1.5) * radius));
            if (redNumber != null) {
                for (int i = 0; i < redNumber.length; i++) {
                    drawRedNumber(canvas,
                            startX + (radius * 2 + spacing) * i,
                            centerY,
                            radius,
                            redNumber[i]);
                }
            }
            //绘画蓝球
            if (buleNumber != null) {
                for (int i = 0; i < buleNumber.length; i++) {
                    drawBuleNumber(canvas,
                            (startX + (radius * 2 + spacing) * (redNumber.length + i)),
                            centerY,
                            radius,
                            buleNumber[i]);
                }
            }
        } else {//长度长了
            //开始长度
            float startX = radius + spacing;
            mWhitePaint.setTextSize((float) (Math.sqrt(1.5) * radius));
            for (int i = 0; i < row; i++) {
                //中心点
                centerY = (int) (i * (mRadius * 2 + mSpacing) + (mRadius + mSpacing / 2));

                for (int j = 0; j < columnMax; j++) {
                    if (j == 0) {
                        startX = radius + spacing;
                    }else {
                        startX +=  (radius * 2 + spacing);
                    }
                    int number = i * columnMax + j +1;
                    if (number <= getNumberLength(redNumber)) {
                        drawRedNumber(canvas,
                                startX ,
                                centerY,
                                radius,
                                redNumber[number -1]);
                    } else if (number <= length) {
                        drawBuleNumber(canvas,
                                startX ,
                                centerY,
                                radius,
                                buleNumber[number - getNumberLength(redNumber) -1]);
                    }

                    Log.e("中心",startX+":"+centerY);

                }
            }
        }


    }

    private void drawRedNumber(Canvas canvas, float x, int y, float radius, String text) {
        //画圆形
        canvas.drawCircle(
                /*中心点x*/x,
                /*中心点y*/y,
                /*半径*/radius,
                /*画笔*/mRedPaint);
        //画文字
        canvas.drawText(text,
                x - getFontWidth(mWhitePaint, text) / 2,
                y + getFontHeight(mWhitePaint, text) / 2,
                mWhitePaint);
    }


    private void drawBuleNumber(Canvas canvas, float x, int y, float radius, String text) {
        //画圆形
        canvas.drawCircle(
                /*中心点x*/x,
                /*中心点y*/y,
                /*半径*/radius,
                /*画笔*/mBulePaint);
        //画文字
        canvas.drawText(text,
                x - getFontWidth(mWhitePaint, text) / 2,
                y + getFontHeight(mWhitePaint, text) / 2,
                mWhitePaint);
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
    public float getFontHeight(Paint paint, String text) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }


    public String[] getRedNuber() {
        return redNumber;
    }

    public void setRedNuber(String[] redNuber) {
        this.redNumber = redNuber;
    }

    public String[] getBuleNuber() {
        return buleNumber;
    }

    public void setBuleNuber(String[] buleNuber) {
        this.buleNumber = buleNuber;
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float mRadius) {
        this.mRadius = mRadius;
    }

    public float getSpacing() {
        return mSpacing;
    }

    public void setSpacing(float mSpacing) {
        this.mSpacing = mSpacing;
    }
}
