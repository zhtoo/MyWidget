package com.hs.samplewidget.myView;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * 点击水波纹效果了
 * 作者：zhanghaitao on 2017/8/17 14:28
 * 邮箱：820159571@qq.com
 */

public class RadialGradientView extends View {

    private int[] mColors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
    private Paint mPaint;
    private RadialGradient mRadialGradient;
    private int DEFAULT_RADIUS = 50;


    public RadialGradientView(Context context) {
        this(context, null);
        initView();
    }

    public RadialGradientView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initView();
    }


    public RadialGradientView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    private void initView() {
        //画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //RadialGradientTest(canvas);
        canvas.drawCircle(mX, mY, mCurRadius, mPaint);
    }

    /**
     * SweepGradient叫渐变渲染或者梯度渲染，其效果与用法都和LinearGradient类似，还是先介绍基本用法
     * x,y 渐变的中心点
     */
    private void RadialGradientTest(Canvas canvas) {
        RadialGradient mRadialGradient = new RadialGradient(300, 300, 100, mColors, null, Shader.TileMode.REPEAT);
        mPaint.setShader(mRadialGradient);
        canvas.drawCircle(300, 300, 300, mPaint);
    }

    int mCurRadius;
    private float mX;
    private float mY;

    public void setRadius(final int radius) {
        mCurRadius = radius;
        if (mCurRadius > 0) {
            mRadialGradient = new RadialGradient(mX, mY, mCurRadius, 0x00FFFFFF, 0xFF58FAAC, Shader.TileMode.CLAMP);
            mPaint.setShader(mRadialGradient);
        }
        postInvalidate();
    }


    ObjectAnimator mAnimator;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mX != event.getX() || mY != mY) {
            mX = (int) event.getX();
            mY = (int) event.getY();
            setRadius(DEFAULT_RADIUS);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP: {
                if (mAnimator != null && mAnimator.isRunning()) {
                    mAnimator.cancel();
                }
                if (mAnimator == null) {
                    mAnimator = ObjectAnimator.ofInt(this, "radius", DEFAULT_RADIUS, getWidth());
                }
                mAnimator.setInterpolator(new AccelerateInterpolator());
                mAnimator.addListener(new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        setRadius(0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }

                });
                mAnimator.start();
            }
        }
        return super.onTouchEvent(event);
    }


}
