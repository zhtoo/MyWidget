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
 * 作者：zhanghaitao on 2017/11/6 16:55
 * 邮箱：820159571@qq.com
 *
 * @describe:
 */

public class TestShaderTileModeView extends View {
    public TestShaderTileModeView(Context context) {
        super(context);
    }

    public TestShaderTileModeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestShaderTileModeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //渐变颜色选择
    private int[] colors = new int[]{Color.YELLOW, Color.GREEN, Color.RED};

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint barChartsClickPaint = new Paint();
        barChartsClickPaint.setAntiAlias(true);//防锯齿
        barChartsClickPaint.setStrokeWidth(20);//宽度
        barChartsClickPaint.setColor(Color.GREEN);
        barChartsClickPaint.setStyle(Paint.Style.FILL_AND_STROKE);//充满并且描边
        barChartsClickPaint.setStrokeCap(Paint.Cap.ROUND);

        Paint barChartsClickPaint1 = new Paint();
        barChartsClickPaint1.setAntiAlias(true);//防锯齿
        barChartsClickPaint1.setStrokeWidth(20);//宽度
        barChartsClickPaint1.setColor(Color.GREEN);
        barChartsClickPaint1.setStyle(Paint.Style.FILL_AND_STROKE);//充满并且描边
        barChartsClickPaint1.setStrokeCap(Paint.Cap.ROUND);

        Paint barChartsClickPaint2 = new Paint();
        barChartsClickPaint2.setAntiAlias(true);//防锯齿
        barChartsClickPaint2.setStrokeWidth(20);//宽度
        barChartsClickPaint2.setColor(Color.GREEN);
        barChartsClickPaint2.setStyle(Paint.Style.FILL_AND_STROKE);//充满并且描边
        barChartsClickPaint2.setStrokeCap(Paint.Cap.ROUND);


        Shader mShader = new LinearGradient(0, 0, 1080, 600,
                colors, null,
                Shader.TileMode.REPEAT);
        barChartsClickPaint.setShader(mShader);
        canvas.drawRect(0, 0, 1080, 600,
                barChartsClickPaint);


        Shader mShader1 = new LinearGradient(0, 650, 1080, 1250,
                colors, null,
                Shader.TileMode.CLAMP);
        barChartsClickPaint1.setShader(mShader1);
        canvas.drawRect(0, 650, 1080, 1250,
                barChartsClickPaint1);

        Shader mShader2 = new LinearGradient(0, 1300, 1080, 1800,
                colors, null ,
                Shader.TileMode.MIRROR);
        barChartsClickPaint2.setShader(mShader2);
        canvas.drawRect(0, 1300, 1080, 1800,
                barChartsClickPaint2);

        /**
         定义了平铺的3种模式：
         static final Shader.TileMode CLAMP: 边缘拉伸.
         static final Shader.TileMode MIRROR：在水平方向和垂直方向交替景象, 两个相邻图像间没有缝隙.
         Static final Shader.TillMode REPETA：在水平方向和垂直方向重复摆放,两个相邻图像间有缝隙缝隙.

         方法：
         1. boolean getLoaclMatrix(Matrix localM); 如果shader有一个非本地的矩阵将返回true.
         localM：如果不为null将被设置为shader的本地矩阵.
         2. void setLocalMatrix(Matrix localM);
         设置shader的本地矩阵,如果localM为空将重置shader的本地矩阵。

         Shader的直接子类:
         BitmapShader    : 位图图像渲染
         LinearGradient  : 线性渲染
         RadialGradient  : 环形渲染
         SweepGradient   : 扫描渐变渲染/梯度渲染
         ComposeShader   : 组合渲染，可以和其他几个子类组合起来使用
         */

    }
}
