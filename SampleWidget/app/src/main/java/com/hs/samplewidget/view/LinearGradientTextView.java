package com.hs.samplewidget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * 作者：zhanghaitao on 2017/8/17 14:38
 * 邮箱：820159571@qq.com
 */

public class LinearGradientTextView extends TextView {

    private Point mPoint;
    private TextPaint mPaint;
    private int mTextWidth;
    private LinearGradient mLinearGradient;
   // private int[] mColors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
    private int[] mColors = {Color.GRAY, Color.BLACK, Color.GRAY};

    public LinearGradientTextView(Context context) {
        this(context, null);
    }

    public LinearGradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取屏幕宽度
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mPoint = new Point();
        windowManager.getDefaultDisplay().getSize(mPoint);
        String text = getText().toString();
        //拿到TextView的画笔
        mPaint = getPaint();
        mTextWidth = (int) mPaint.measureText(text);
        int gradientTextSize = mTextWidth / text.length() * 3;
        mLinearGradient = new LinearGradient(0, 0,
                gradientTextSize, 0,
                mColors, null,
                Shader.TileMode.CLAMP);
        mPaint.setShader(mLinearGradient);


    }

    int mTranslate = 0;
    //控制移动速度
    int DELTAX = 5;


    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        Matrix matrix = new Matrix();
        mTranslate += DELTAX;
        //反复循环
        if (mTranslate > mPoint.x || mTranslate < 1) {
            DELTAX = -DELTAX;
        }
        matrix.setTranslate(mTranslate, 0);
        mLinearGradient.setLocalMatrix(matrix);
        postInvalidate();
    }
}
