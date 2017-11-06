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
 * 下载时显示进度的动画效果
 * <p>
 * 作者：zhanghaitao on 2017/8/15 09:42
 * 邮箱：820159571@qq.com
 *
 * @DES: 这是吹牛逼的产物，开发时长8h。
 * <p>
 * 补充知识：
 * postInvalidate();//在非UI线程中使用
 * invalidate();//在UI线程自身中使用
 * onDraw是在View初化完成之后开始调用
 * 调用invalidate()/postInvalidate()后系统会重新调用onDraw方法画一次。
 * 单线程模型：AndroidUI操作并不是线程安全的，并且这些操作必须在UI线程中调用。
 * Android程序中可以使用的界面刷新方法有两种，分别是利用Handler和利用postInvalidate()来实现在线程中刷新界面。
 */

public class DownloadView extends View {

    private static final String TAG = "DownloadView";

    private int mProgressBackgroundColor;    //进度条的背景色
    private int mProgressColor;    //进度条的颜色
    private float mProgressMax = 100;    //最大的进度值
    private float mCircleWidth;    //圆环的宽度
    private float mTextSize;    //文字大小
    private int mTextColor;    //文字颜色
    private float mProgress = 30; //当前进度，只要不小于0，随便改

    //动画对象
    private ValueAnimator arrowAnimator;
    private ValueAnimator launchAnimator;
    private ValueAnimator progressAnimator;

    //画笔
    private Paint mRingPaint;
    private Paint mTextPaint;
    private Paint mPaint;
    private Paint mLaunchBallPaint;

    //水波纹
    private static final float STRETCH_FACTOR_A = 10;   //幅度
    private static final int OFFSET_Y = 0;              //y的偏移量
    private static final int TRANSLATE_X_SPEED = 1; //水波移动速度
    private float mCycleFactorW;    //周期
    private float[] mYPositions;    //Y的坐标点
    private float[] mResetYPositions;   //改变后Y的坐标点
    private int mXOffsetSpeed;  //每次偏移量
    private int mXOffset;   //每次便宜的距离

    private Context context;
    private DrawFilter mDrawFilter;  //开启抗锯齿
    private int mTotalWidth;    //view的宽度

    //箭头动画的类型
    private int drawType = 0;
    private final int START_DOWNLOAD = 1;    //开始下载前箭头收缩的动画
    private final int START_LAUNCH = 2;    //箭头收缩后，小球弹射的动画
    private final int START_PROGRESS = 3;    //绘画进度的动画

    private float arrowCoordinate;    //箭头变化的坐标
    private float launchCoordinate;    //弹射小球Y坐标

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
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DownloadView);
        mProgressBackgroundColor = typedArray.getColor(R.styleable.DownloadView_progress_background_color, Color.GRAY);
        mProgressColor = typedArray.getColor(R.styleable.DownloadView_progress_color, Color.BLUE);
        mProgressMax = typedArray.getFloat(R.styleable.DownloadView_progress_max, 100);
        mCircleWidth = typedArray.getDimension(R.styleable.DownloadView_circle_width, 10);
        mTextSize = typedArray.getDimension(R.styleable.DownloadView_text_size, 60);
        mTextColor = typedArray.getColor(R.styleable.DownloadView_text_color, Color.RED);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if ((widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) ||
                (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.UNSPECIFIED) ||
                (widthSpecMode == MeasureSpec.UNSPECIFIED && heightSpecMode == MeasureSpec.AT_MOST) ||
                (widthSpecMode == MeasureSpec.UNSPECIFIED && heightSpecMode == MeasureSpec.UNSPECIFIED)) {
            //默认wrap_content为400px
            setMeasuredDimension(400, 400);
        } else if (widthSpecMode == MeasureSpec.AT_MOST||widthSpecMode == MeasureSpec.UNSPECIFIED) {
            setMeasuredDimension(400, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST||heightSpecMode == MeasureSpec.UNSPECIFIED) {
            setMeasuredDimension(widthSpecSize, 400);
        }
    }

    private void init() {
        //圆环画笔
        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setColor(mProgressBackgroundColor);
        mRingPaint.setStrokeWidth(mCircleWidth);
        mRingPaint.setStyle(Paint.Style.STROKE);
        //文字画笔
        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setStrokeWidth(0);
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        //圆弧画笔和箭头画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mProgressColor);
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);// 圆形线帽
        mPaint.setStrokeJoin(Paint.Join.ROUND);//线的连接处圆滑
        //弹射小球画笔
        mLaunchBallPaint = new Paint();
        mLaunchBallPaint.setAntiAlias(true);
        mLaunchBallPaint.setColor(mProgressColor);
        mLaunchBallPaint.setStrokeWidth(mCircleWidth);
        mLaunchBallPaint.setStyle(Paint.Style.FILL);
        mLaunchBallPaint.setStrokeCap(Paint.Cap.ROUND);
        // 将dp转化为px，用于控制不同分辨率上移动速度基本一致
        mXOffsetSpeed = dp2px(context, TRANSLATE_X_SPEED);
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 记录下view的宽高
        mTotalWidth = w;
        // 用于保存原始波纹的y值
        mYPositions = new float[mTotalWidth];
        // 用于保存波纹一的y值
        mResetYPositions = new float[mTotalWidth];
        // 将周期定为view总宽度
        mCycleFactorW = (float) (4 * Math.PI / w);
        // 根据view总宽度得出所有对应的y值
        for (int i = 0; i < mTotalWidth; i++) {
            mYPositions[i] = (float) (STRETCH_FACTOR_A * Math.sin(mCycleFactorW * i) + OFFSET_Y);
        }
    }

    /**
     * 调用系统的方法重新改变Y坐标的数值
     */
    private void resetPositonY() {
        // mXOffset代表当前第一条水波纹要移动的距离
        int yOneInterval = mYPositions.length - mXOffset;
        // 使用System.arraycopy方式重新填充波纹的数据
        System.arraycopy(mYPositions, mXOffset, mResetYPositions, 0, yOneInterval);
        System.arraycopy(mYPositions, 0, mResetYPositions, yOneInterval, mXOffset);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 从canvas层面去除绘制时锯齿
        canvas.setDrawFilter(mDrawFilter);
        //重新绘制Y的坐标
        resetPositonY();
        //获取控件宽度一半的大小
        int center = mTotalWidth / 2;
        //画背景圆环
        drawCircle(canvas, center);
        if (drawType == START_DOWNLOAD) {
            if (arrowAnimator == null) {
                setArrowAnimation();
            }
            drawArrow(canvas, center);
            invalidate();
        } else if (drawType == START_LAUNCH) {
            if (launchAnimator == null) {
                setLaunchAnimation();
            }
            canvas.drawCircle(center, launchCoordinate, mCircleWidth / 2, mLaunchBallPaint);
            drawLine(canvas, center);
            invalidate();
        } else if (drawType == START_PROGRESS) {
            if (progressAnimator == null) {
                startProgressAnimation();
            }
            //画圆弧
            drawArc(canvas, center);
            //画文字
            drawText(canvas, center);
            if (mProgress == mProgressMax) {
                //画直线
                drawLine(canvas, center);
            } else {
                //画水波纹
                drawRipple(canvas, center);
                invalidate();
            }
        } else {
            //画箭头
            Path mArrowPath = new Path();
            mArrowPath.moveTo(center * 0.5f, center);
            mArrowPath.lineTo(center, center * 1.5f);
            mArrowPath.lineTo(center * 1.5f, center);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            canvas.drawPath(mArrowPath, mPaint);
            //画箭尾
            canvas.drawLine(center, center * 0.5f,
                    center, center * 1.5f, mPaint);

        }
    }

    private void drawArc(Canvas canvas, int center) {
        float radius = center - mCircleWidth / 2;
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);
        canvas.drawArc(oval, 270, -mProgress / mProgressMax * 360, false, mPaint);
    }

    private void drawLine(Canvas canvas, int center) {
        canvas.drawLine(center * 0.5f, center, center * 1.5f, center, mPaint);
    }

    private void drawText(Canvas canvas, int center) {
        //计算百分比
        int percent = (int) (mProgress / mProgressMax * 100);
        //添加%
        String percentStr = percent + "%";
        canvas.drawText(percentStr,//待绘制的文字
                center - getFontWidth(mTextPaint, percentStr) / 2,//x坐标：
                center + STRETCH_FACTOR_A + getFontHeight(mTextPaint),
                mTextPaint);
    }

    private void drawCircle(Canvas canvas, int center) {
        float radius = center - mCircleWidth / 2;
        canvas.drawCircle(center, center, radius, mRingPaint);
    }

    private void drawArrow(Canvas canvas, int center) {
        //画箭头
        Path mArrowPath = new Path();
        mArrowPath.moveTo(center * 0.5f, center);
        mArrowPath.lineTo(center, arrowCoordinate);
        mArrowPath.lineTo(center * 1.5f, center);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        canvas.drawPath(mArrowPath, mPaint);
        //画箭尾
        canvas.drawLine(center, center * 0.5f + center * 1.5f - arrowCoordinate,
                center, arrowCoordinate, mPaint);
        //当箭头的坐标达到绘制小球的位置时，绘制小球
        if (arrowCoordinate <= (center + mCircleWidth)) {
            canvas.drawCircle(center, center - mCircleWidth, mCircleWidth / 2, mLaunchBallPaint);
        }
    }

    private void drawRipple(Canvas canvas, int center) {
        Path mRipplePath = new Path();
        float startX = center * 0.5f;
        //循环将各个点连接起来
        for (int i = 0; i < center; i++) {
            if (i == 0) {
                mRipplePath.moveTo(startX, center + mResetYPositions[i]);
            } else {
                mRipplePath.lineTo(startX + i, center + mResetYPositions[i]);
            }
        }
        canvas.drawPath(mRipplePath, mPaint);
        // 改变水波纹的移动点
        mXOffset += mXOffsetSpeed;
        // 如果已经移动到结尾处，则重头记录
        if (mXOffset >= mTotalWidth) {
            mXOffset = 0;
        }
    }

    /**
     * 设置进度
     *
     * @param progress
     */
    public void setProgress(int progress) {
        //开启下载前的动画
        if (drawType == 0) {
            drawType = START_DOWNLOAD;
            postInvalidate();
        }
        //进度值不能小于0
        if (progress < 0) {
            throw new IllegalArgumentException("进度不能小于0！");
        } else if (progress > mProgressMax) {
            mProgress = (int) mProgressMax;
        } else if (progress < mProgressMax) {
            mProgress = progress;
        } else if (progress == mProgressMax) {
            mProgress = progress;
        }
    }

    /**
     * 初始化进度
     */
    public void initProgress() {
        //开启下载前的动画
        drawType = 0;
        mProgress = 0;
        if (arrowAnimator != null) {
            arrowAnimator.cancel();
            arrowAnimator = null;
        }
        if (launchAnimator != null) {
            launchAnimator.cancel();
            launchAnimator = null;
        }
        if (progressAnimator != null) {
            progressAnimator.cancel();
            progressAnimator = null;
        }
        postInvalidate();
    }

    /**
     * 箭头动画
     */
    private void setArrowAnimation() {
        arrowAnimator = ValueAnimator.ofFloat(mTotalWidth * 0.75f, mTotalWidth * 0.5f);
        arrowAnimator.setDuration(250);
        arrowAnimator.setTarget(arrowCoordinate);
        arrowAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                arrowCoordinate = (float) animation.getAnimatedValue();
                if (arrowCoordinate == mTotalWidth * 0.5f) {
                    drawType = START_LAUNCH;
                    arrowAnimator = null;
                }
            }
        });
        arrowAnimator.start();
    }

    /**
     * 弹射动画
     */
    private void setLaunchAnimation() {
        launchAnimator = ValueAnimator.ofFloat(mTotalWidth * 0.5f - mCircleWidth, mCircleWidth / 2);
        launchAnimator.setDuration(250);
        launchAnimator.setTarget(launchCoordinate);
        launchAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                launchCoordinate = (float) animation.getAnimatedValue();
                if (launchCoordinate == mCircleWidth / 2) {
                    drawType = START_PROGRESS;
                    launchAnimator = null;
                }
            }
        });
        launchAnimator.start();
    }

    /**
     * 进度动画
     */
    private void startProgressAnimation() {
        progressAnimator = ValueAnimator.ofFloat(0, mProgress);
        progressAnimator.setDuration(500);
        progressAnimator.setTarget(mProgress);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                mProgress = (float) animation.getAnimatedValue();
                if (mProgress >= mProgressMax) {
                    progressAnimator.cancel();
                    progressAnimator = null;
                }
            }
        });
        progressAnimator.start();
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

    /**
     * dp转Px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }
}
