package com.zht.samplewidget.myView;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * 下载时显示进度的动画效果
 * <p>
 * 作者：zhanghaitao on 2017/8/15 09:42
 * 邮箱：820159571@qq.com
 * <p>
 * 补充知识：
 * postInvalidate();//在非UI线程中使用
 * invalidate();//在UI线程自身中使用
 * onDraw是在View初化完成之后开始调用
 * 调用invalidate()/postInvalidate()后系统会重新调用onDraw方法画一次。
 * 单线程模型：AndroidUI操作并不是线程安全的，并且这些操作必须在UI线程中调用。
 * Android程序中可以使用的界面刷新方法有两种，分别是利用Handler和利用postInvalidate()来实现在线程中刷新界面。
 */

public class Download2View extends View {

    private static final String TAG = "Download2View";

    private int mProgressBackgroundColor = Color.GRAY;    //进度条的背景色
    private int mProgressColor = Color.BLUE;    //进度条的颜色
    private float mProgressMax = 100;    //最大的进度值
    private float mCircleWidth = 10;    //圆环的宽度
    private float mTextSize = 20;    //文字大小
    private int mTextColor = Color.WHITE;    //文字颜色
    private float mProgress = 0; //当前进度，只要不小于0，随便改
    private float mAnimatorValue = 0;
    private float mArcValue = 0;
    private float mCenterAdd = 20;
    private int mCenterRoundEnd = 0;

    //动画对象
    private ValueAnimator progressAnimator;
    private ValueAnimator ArcAnimator;

    //画笔
    private Paint mRingPaint;
    private Paint mTextPaint;
    private Paint mPaint;
    private Paint mBallPaint;

    private Context context;
    private DrawFilter mDrawFilter;  //开启抗锯齿
    private int mTotalWidth;    //view的宽度
    private int[] range = new int[]{0, 18, 18, 50, 43, 75, 68, 100};

    public Download2View(Context context) {
        this(context, null);
        initCofig(context, null);
        init();
    }

    public Download2View(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initCofig(context, attrs);
        init();
    }

    public Download2View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCofig(context, attrs);
        init();
    }

    private void initCofig(Context context, AttributeSet attrs) {
        this.context = context;
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
        } else if (widthSpecMode == MeasureSpec.AT_MOST || widthSpecMode == MeasureSpec.UNSPECIFIED) {
            setMeasuredDimension(400, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED) {
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
        //扩展小球画笔
        mBallPaint = new Paint();
        mBallPaint.setAntiAlias(true);
        mBallPaint.setColor(mProgressColor);
        mBallPaint.setStrokeWidth(mCircleWidth);
        mBallPaint.setStyle(Paint.Style.FILL);
        mBallPaint.setStrokeCap(Paint.Cap.ROUND);

        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 记录下view的宽高
        mTotalWidth = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 从canvas层面去除绘制时锯齿
        canvas.setDrawFilter(mDrawFilter);
        //获取控件宽度一半的大小
        int center = mTotalWidth / 2;
        //画背景圆环
        for (int i = 1; i < 5; i++) {
            //当中心圆球的半径超过中心圆环的半径，不绘画圆环
            if (!(i == 1 && mAnimatorValue >= mProgressMax - 2)) {
                drawCircle(canvas, center, center * i / 5);
            }
        }
        //绘画水波纹效果的圆圈扩散
        if (mCenterRoundEnd == 1) {
            if (ArcAnimator == null) {
                startArcAnimation();
            }
            float arcRadius = (float) Math.sqrt(2) * center;
            for (int i = 4; i > 1; i--) {
                drawCircle(canvas, center, center * i / 5 + mArcValue / 100 * (arcRadius - center * i / 5));
            }
        }
        //画进度圆环
        for (int i = 0; i < 4; i++) {
            drawProgress(canvas, center, center * (4 - i) / 5, range[2 * i], range[2 * i + 1]);
        }
        //绘画中心圆（只有当进度为100的是后才开始）
        if (mProgress >= 100) {
            if (progressAnimator == null) {
                startProgressAnimation(0, mProgressMax + mCenterAdd, 300);
            }
            if (mAnimatorValue == mProgressMax + mCenterAdd) {
                startProgressAnimation(mProgressMax + mCenterAdd, mProgressMax, 100);
            }
            drawRound(canvas, center);
        }
        //画进度文字
        drawText(canvas, center);
    }

    /**
     * 绘画进度
     *
     * @param canvas
     * @param center
     * @param radius
     * @param start
     * @param end
     */
    private void drawProgress(Canvas canvas, int center, float radius, int start, int end) {
        if (mProgress > start && mProgress < end) {
            drawArc(canvas, center, radius, (mProgress - start) * 360 / (end - start));
        } else if (mProgress >= end) {
            drawArc(canvas, center, radius, 360);
        }
    }

    /**
     * 绘画圆环
     *
     * @param canvas
     * @param center
     * @param radius
     * @param arcLength
     */
    private void drawArc(Canvas canvas, int center, float radius, float arcLength) {
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);
        canvas.drawArc(oval, 270, arcLength/*mProgress / mProgressMax * 360*/, false, mPaint);
    }

    /**
     * 绘画文字
     *
     * @param canvas
     * @param center
     */
    private void drawText(Canvas canvas, int center) {
        //计算百分比
        int percent = (int) (mProgress / mProgressMax * 100);
        //添加%
        String percentStr = percent + "%";
        canvas.drawText(percentStr,//待绘制的文字
                center - getFontWidth(mTextPaint, percentStr) / 2,//x坐标：
                center + getFontHeight(mTextPaint,percentStr) / 2,
                mTextPaint);
    }

    /**
     * 画圆环
     *
     * @param canvas
     * @param center
     */
    private void drawCircle(Canvas canvas, int center, float radius) {
        canvas.drawCircle(center, center, radius, mRingPaint);
    }

    /**
     * 画圆
     *
     * @param canvas
     * @param center
     */
    private void drawRound(Canvas canvas, int center) {
        float radius = (mAnimatorValue / mProgressMax) * center / 5;
        canvas.drawCircle(center, center, radius, mBallPaint);
        invalidate();
    }

    /**
     * 设置进度
     *
     * @param progress
     */
    public void setProgress(int progress) {
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
        postInvalidate();
    }

    /**
     * 初始化进度
     */
    public void initProgress() {
        //开启下载前的动
        mProgress = 0;
        mAnimatorValue = 0;
        mArcValue = 0;
        mCenterRoundEnd = 0;
        if (progressAnimator != null) {
            progressAnimator.cancel();
            progressAnimator = null;
        }
        if (ArcAnimator != null) {
            ArcAnimator.cancel();
            ArcAnimator = null;
        }
        postInvalidate();
    }

    /**
     * 进度完成动画
     */
    private void startProgressAnimation(float startValue, final float endValue, long duration) {
        progressAnimator = ValueAnimator.ofFloat(startValue, endValue);
        progressAnimator.setDuration(duration);
        progressAnimator.setTarget(endValue);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                mAnimatorValue = (float) animation.getAnimatedValue();
                if (mAnimatorValue == endValue) {
                    if(progressAnimator!=null){
                        progressAnimator.cancel();
                    }
                }
            }
        });
        progressAnimator.start();
        progressAnimator.addListener(progressAnimatorListener);
    }

    /**
     * 圆环扩展的动画
     */
    private void startArcAnimation() {
        ArcAnimator = ValueAnimator.ofFloat(0, mProgressMax);
        ArcAnimator.setDuration(1000);
        ArcAnimator.setTarget(mProgressMax);
        ArcAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                mArcValue = (float) animation.getAnimatedValue();
                if (mArcValue == mProgressMax) {
                    ArcAnimator.cancel();
                }
            }
        });
        ArcAnimator.start();
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
    public float getFontHeight(Paint paint,String text) {
        Rect rect = new Rect();
        paint.getTextBounds(text,0,text.length(), rect);
        return rect.height();
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

    /**
     * 动画监听
     */
    private Animator.AnimatorListener progressAnimatorListener = new Animator.AnimatorListener() {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (mAnimatorValue == mProgressMax) {
                mCenterRoundEnd = 1;
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

}