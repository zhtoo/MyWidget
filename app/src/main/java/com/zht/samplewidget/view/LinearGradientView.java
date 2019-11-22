package com.zht.samplewidget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * 作者：zhanghaitao on 2017/8/17 14:28
 * 邮箱：820159571@qq.com
 * 渐变色
 */

public class LinearGradientView extends View {

    private int[] mColors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
    private Paint paint;


    public LinearGradientView(Context context) {
        this(context, null);
        initView();
    }

    public LinearGradientView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initView();
    }


    public LinearGradientView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        paint = new Paint();
        paint.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        LinearGradientTest(canvas);
    }

    /**线性渐变
     * x0, y0, 起始点
     * x1, y1, 结束点
     * int[]  mColors, 中间依次要出现的几个颜色
     * float[] positions,数组大小跟colors数组一样大，中间依次摆放的几个颜色分别放置在那个位置上(参考比例从左往右)
     * tile
     */
    private void LinearGradientTest(Canvas canvas) {
        LinearGradient linearGradient = new LinearGradient(
                0, 0, 1080, 100,
                mColors, null,
                Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
        canvas.drawRect(0, 0, 1080, 100, paint);
    }



}
