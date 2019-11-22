package com.zht.samplewidget.myView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：zhanghaitao on 2017/8/23 16:59
 * 邮箱：820159571@qq.com
 * 练手的demo，爱心View
 */

public class LoveView extends View {

    private Context context;
    private Paint mPaint;
    private Path path;
    private List<Point> mPointList;
    private int mPointWidth;
    private int mPointHeight;
    private int count;
    private int centerPointX=0;
    private int centerPointY=0;
    private int centerX=0;
    private int centerY=0;
    private int minDY=0;
    private int maxDY=0;
    private int dY;
    private int minX=0;
    private int maxX=0;
    private int minY=0;
    private int maxY=0;

    private boolean firstMeasure=false;


    public LoveView(Context context) {
        this(context, null);
        initCofig(context, null);
    }

    public LoveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCofig(context, attrs);
    }

    private void initCofig(Context context, AttributeSet attrs) {
        this.context = context;
        initTool();
    }

    private void initTool() {
        //文字画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);
        mPaint.setTypeface(Typeface.DEFAULT);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);//圆弧
        path = new Path();
        mPointList = new ArrayList<>();
        count = 0;

        //获取到爱心的轨迹坐标
        getPointSet();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            //默认wrap_content为400px
            setMeasuredDimension(300, 300);
            centerPointX = 300;
            centerPointY = 300;
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(300, heightSpecSize);
            centerPointX = 300;
            centerPointY = heightSpecSize;
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, 300);
            centerPointX = widthSpecSize;
            centerPointY = 300;
        } else {
            centerPointX = widthSpecSize;
            centerPointY = heightSpecSize;
        }


        if(!firstMeasure){
            for (Point point : mPointList) {
                if (point.x > maxX) {
                    maxX = point.x;
                }
                if (point.x < minX) {
                    minX = point.x;
                }
                if (point.y > maxY) {
                    maxY = point.y;
                }
                if (point.y < minY) {
                    minY = point.y;
                }

                if (point.x == 0 && minDY > point.y) {
                    minDY = point.y;
                }
                if (point.x == 0 && maxDY < point.y) {
                    maxDY = point.y;
                }
            }
            centerX = maxX - minX;
            centerY = maxY - minY;

            mPointWidth = centerPointX / 2;
            mPointHeight = centerPointY / 2;

            double numerator = Math.min(mPointWidth * 2, mPointHeight * 2);
            double denominator = Math.max(centerX * 1.08, centerY * 1.08);

            double multiple = numerator / denominator;
            dY = (int) Math.abs(multiple * (maxDY + minDY) * 1.08)/4;
            for (Point point : mPointList) {
                point.x = (int) (multiple * point.x);
                point.y = (int) (-multiple * point.y);
            }
            Point point = mPointList.get(0);
            path.moveTo(point.x + mPointWidth, point.y + mPointHeight - dY);
            mHandler.sendEmptyMessageDelayed(MSG_WHAT, DELAY_TIME);
            firstMeasure = true;
        }else {
            mHandler.sendEmptyMessageDelayed(MSG_WHAT, DELAY_TIME);
        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (count < mPointList.size()) {
            Point point = mPointList.get(count);
            Log.d("坐标", "(" + point.x + "," + point.y + ")");
            if (count == 0) {
                path.moveTo(point.x + mPointWidth, point.y + mPointHeight- dY);
            } else {
                path.lineTo(point.x + mPointWidth, point.y + mPointHeight- dY);
            }

        }
        canvas.drawPath(path, mPaint);

    }

    private void getPointSet() {
        for (double i = 0; i < 2 * Math.PI; i += 0.02) {
            Point point = new Point();
            point.x = (int) (10000 * 16 * Math.pow(Math.sin(i), 3));//Math.pow(2,3)计算2的3次方
            point.y = (int) (10000 * (13 * Math.cos(i) - 5 * Math.cos(2 * i) - 2 * Math.cos(3 * i) - Math.cos(4 * i)));
            mPointList.add(point);
        }
    }

    private int MSG_WHAT = 66;
    private long DELAY_TIME = 10;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (count < mPointList.size()) {
                count++;
                //重绘，在此会重新调用onDraw（）
                postInvalidate();
                mHandler.sendEmptyMessageDelayed(MSG_WHAT, DELAY_TIME);
            }
        }
    };


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // reset();
        return super.onTouchEvent(event);
    }

    public void reset() {
        path.reset();
        count = 0;
        Point point = mPointList.get(0);
        path.moveTo(point.x + mPointWidth, point.y + mPointHeight);
        mHandler.removeMessages(MSG_WHAT);
        mHandler.sendEmptyMessageDelayed(MSG_WHAT, DELAY_TIME);
    }
}
