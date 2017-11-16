package com.hs.samplewidget.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;

import com.hs.samplewidget.R;

/**
 * 作者：zhanghaitao on 2017/8/17 11:14
 * 邮箱：820159571@qq.com

 */

public class CircleImage extends View {

    private Bitmap bitmap;

    public CircleImage(Context context) {
        this(context, null);
    }

    public CircleImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        BitmapShaderTest(canvas);
    }


    private void BitmapShaderTest(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);


        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_test_circle);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scale = Math.max(width, height) / Math.min(width, height);
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.setScale(scale, scale);
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
        //画正方形
         //canvas.drawRect(0,0,width,height,paint);
        //画圆形
         //canvas.drawCircle(height / 2, height/ 2, height / 2, paint);
        //画椭圆
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setShader(bitmapShader);
        shapeDrawable.setBounds(0, 0, width, height);
        shapeDrawable.draw(canvas);

    }

}
