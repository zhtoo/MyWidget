package com.hs.samplewidget.myView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.hs.samplewidget.R;

/**
 * 作者：zhanghaitao on 2017/8/21 09:42
 * 邮箱：820159571@qq.com
 */

public class DownloadView extends View {

    //进度条的背景色
    private int mProgressBackgroundColor;
    //进度条的颜色
    private int mProgressColor;
    //最大的进度
    private float mProgressMax = 100;
    //圆环的宽度
    private float mCircleWidth;
    //文字大小
    private float mTextSize;
    //文字颜色
    private int mTextColor;
    //当前进度
    private float mProgress = 0;
    //动画对象
    private ValueAnimator progressAnimator;
    private Paint mRingPaint;
    private Paint mTextPaint;
    private Paint mArcPaint;
    private Paint mArrowPaint;
    private Paint mArrowTailPaint;


    //水波纹
    //幅度
    private static final float STRETCH_FACTOR_A = 5;
    //y的偏移量
    private static final int OFFSET_Y = 0;
    // 第一条水波移动速度
    private static final int TRANSLATE_X_SPEED_ONE = 2;
    //周期
    private float mCycleFactorW;
    //Y的坐标点
    private float[] mYPositions;
    //改变后Y的坐标点
    private float[] mResetOneYPositions;
    //每次偏移量
    private int mXOffsetSpeedOne;
    //每次便宜的距离
    private int mXOneOffset;


    private DrawFilter mDrawFilter;

    private Context context;
    private int mTotalWidth;
    //箭头动画的速度
    private int drawTime  = 100;
    private float arrowCoordinate;

    public DownloadView(Context context) {
        this(context, null);
        initCofig(context, null);
        init();
    }

    public DownloadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initCofig(context, attrs);
        init();
    }

    public DownloadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCofig(context, attrs);
        init();
    }

    private void initCofig(Context context, AttributeSet attrs) {
        this.context = context;
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
            mProgressBackgroundColor = typedArray.getColor(R.styleable.CircleProgressBar_progressBackgroundColor, Color.GRAY);
            mProgressColor = typedArray.getColor(R.styleable.CircleProgressBar_progressColor, Color.BLUE);
            mProgressMax = typedArray.getFloat(R.styleable.CircleProgressBar_progressMax, 100);
            mCircleWidth = typedArray.getDimension(R.styleable.CircleProgressBar_circleWidth, 20);
            mTextSize = typedArray.getDimension(R.styleable.CircleProgressBar_textSize, 60);
            mTextColor = typedArray.getColor(R.styleable.CircleProgressBar_textColor, Color.RED);
            typedArray.recycle();
        }
    }

    private void init() {
        //圆环画笔
        mRingPaint = new Paint();
        mRingPaint.setColor(mProgressBackgroundColor);
        mRingPaint.setStrokeWidth(mCircleWidth);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setAntiAlias(true);
        //文字画笔
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setStrokeWidth(0);
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        //圆弧画笔
        mArcPaint = new Paint();
        mArcPaint.setColor(mProgressColor);
        mArcPaint.setStrokeWidth(mCircleWidth);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setAntiAlias(true);
        //箭头画笔
        mArrowPaint = new Paint();
        mArrowPaint.setStrokeWidth(mCircleWidth);
        mArrowPaint.setAntiAlias(true);
        mArrowPaint.setColor(mProgressColor);
        mArrowPaint.setStyle(Paint.Style.STROKE);
        mArrowPaint.setStrokeCap(Paint.Cap.ROUND);// 圆形线帽
        mArrowPaint.setStrokeJoin(Paint.Join.ROUND);
        //箭尾画笔
        mArrowTailPaint = new Paint();
        mArrowTailPaint.setColor(mProgressColor);
        mArrowTailPaint.setStrokeWidth(mCircleWidth);
        mArrowTailPaint.setStyle(Paint.Style.STROKE);
        mArrowTailPaint.setAntiAlias(true);
        mArrowTailPaint.setStrokeCap(Paint.Cap.ROUND);// 圆形线帽
        mArrowTailPaint.setStrokeJoin(Paint.Join.ROUND);

        // 将dp转化为px，用于控制不同分辨率上移动速度基本一致
        mXOffsetSpeedOne = dp2px(context, TRANSLATE_X_SPEED_ONE);
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /*
        重写onMeasure()是为了解决wrap_content的问题。
        如果没有加上这一段，那么使用wrap_content与match_parent就没有区别。
         */
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(300, 300);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(300, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, 300);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 从canvas层面去除绘制时锯齿
        canvas.setDrawFilter(mDrawFilter);
        resetPositonY();

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        //画圆环
        int center = getWidth() / 2;
        drawProgress(canvas, center);
        //画文字
        drawText(canvas, center);

        //处理下载前的动画
        if (drawTime > 0) {
            drawArrow(canvas, center);
        }else if (drawTime == 0) {


        } else if (drawTime < 0) {
            //画水波纹
            drawRipple(canvas, center);
        }else {
            //画直线
            drawLine(canvas, center);
        }


    }

    private void drawLine(Canvas canvas, int center) {
        mArrowPaint.setStrokeCap(Paint.Cap.ROUND);// 圆形线帽
        mArrowPaint.setStrokeJoin(Paint.Join.ROUND);
        canvas.drawLine(getWidth() * 0.25f,center, getWidth() * 0.75f, center, mArrowPaint);
    }

    private void drawText(Canvas canvas, int center) {
        int percent = (int) (mProgress / mProgressMax * 100);
        String percentStr = percent + "%";
        Paint.FontMetricsInt fm = mTextPaint.getFontMetricsInt();
        canvas.drawText(percentStr,
                center - mTextPaint.measureText(percentStr) / 2,
                center + (fm.bottom - fm.top) / 2 - fm.bottom + 50,
                mTextPaint);
    }

    private void drawProgress(Canvas canvas, int center) {
        float radius = center - mCircleWidth / 2;
        canvas.drawCircle(center, center, radius, mRingPaint);

        //画圆弧
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);
        canvas.drawArc(oval, 270, -mProgress / mProgressMax * 360, false, mArcPaint);
        invalidate();
    }

    private void drawArrow(Canvas canvas, int center) {
        //画箭头
        Path mArrowPath = new Path();
        mArrowPath.moveTo(getWidth() * 0.25f, center);
        mArrowPath.lineTo(getWidth() * 0.5f, arrowCoordinate);
        mArrowPath.lineTo(getWidth() * 0.75f, center);
        mArrowPaint.setStrokeJoin(Paint.Join.ROUND);
        canvas.drawPath(mArrowPath, mArrowPaint);
        //画箭尾
        canvas.drawLine(center, getWidth() * 0.25f +getWidth() * 0.75f - arrowCoordinate,
                center, arrowCoordinate, mArrowTailPaint);
    }

    private void drawRipple(Canvas canvas, int center) {
        Path mRipplePath = new Path();
        float startX = getWidth() * 0.25f;
        int endX = (int) (getWidth() * 0.5f);
        for (int i = 0; i < endX; i++) {
            if (i == 0) {
                mRipplePath.moveTo(startX, center + mResetOneYPositions[i]);
            } else {
                mRipplePath.lineTo(startX + i, center + mResetOneYPositions[i]);
            }
        }

        canvas.drawPath(mRipplePath, mArrowPaint);
        // 改变水波纹的移动点
        mXOneOffset += mXOffsetSpeedOne;
        // 如果已经移动到结尾处，则重头记录
        if (mXOneOffset >= mTotalWidth) {
            mXOneOffset = 0;
        }
        // 重绘
        postInvalidate();
    }


    private void resetPositonY() {
        // mXOneOffset代表当前第一条水波纹要移动的距离
        int yOneInterval = mYPositions.length - mXOneOffset;
        // 使用System.arraycopy方式重新填充第一条波纹的数据
        System.arraycopy(mYPositions, mXOneOffset, mResetOneYPositions, 0, yOneInterval);
        System.arraycopy(mYPositions, 0, mResetOneYPositions, yOneInterval, mXOneOffset);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 记录下view的宽高
        mTotalWidth = w;
        // 用于保存原始波纹的y值
        mYPositions = new float[mTotalWidth];
        // 用于保存波纹一的y值
        mResetOneYPositions = new float[mTotalWidth];
        // 将周期定为view总宽度
        mCycleFactorW = (float) (4 * Math.PI / w);
        // 根据view总宽度得出所有对应的y值
        for (int i = 0; i < mTotalWidth; i++) {
            mYPositions[i] = (float) (STRETCH_FACTOR_A * Math.sin(mCycleFactorW * i) + OFFSET_Y);
        }
    }


    private void setArrowAnimation() {
        progressAnimator = ValueAnimator.ofFloat( getWidth()*0.75f,getWidth()*0.5f);
        progressAnimator.setDuration(2000);
        progressAnimator.setTarget(arrowCoordinate);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                arrowCoordinate = (float) animation.getAnimatedValue();
                if(arrowCoordinate == getWidth()*0.5f){

                }
            }
        });
        progressAnimator.start();
    }

    /**
     * 返回指定的文字高度
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

    public void setProgress(int progress) {
        arrowCoordinate = getWidth() * 0.75f;
        postInvalidate();
        if (progress < 0) {
            throw new IllegalArgumentException("进度不能小于0！");
        }
        if (progress > mProgressMax) {
            mProgress = (int) mProgressMax;
        }
        if (progress < mProgressMax) {
            mProgress = progress;
            //不知道应用层在哪个线程调用
            postInvalidate();
        } else {
            mProgress = progress;
            postInvalidate();
        }
        setArrowAnimation();
      //  setAnimation();
    }

    /**
     * 为进度设置动画
     */
    private void setAnimation() {
        progressAnimator = ValueAnimator.ofFloat(0, mProgress);
        progressAnimator.setDuration(2000);
        progressAnimator.setTarget(mProgress);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //不能是int （具体原因不知道）
                mProgress = (float) animation.getAnimatedValue();
            }
        });
        progressAnimator.start();
    }

    public static int dp2px(Context context, int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

}
