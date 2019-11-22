package com.zht.samplewidget.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.zht.samplewidget.R;


/**
 * 作者：zhanghaitao on 2017/8/18 15:00
 * 邮箱：820159571@qq.com
 * 了解一下原理即可，功能不可用于实际
 * 需要自己进行修改
 */

public class RoundView extends View {
    private Bitmap mSrcBitmap;
    private Bitmap mDstBitmap;
    private Paint mPaint;
    private int height;
    private int width;

    public RoundView(Context context) {
        super(context);
        init();
    }

    public RoundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
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
        int widthSpecSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize=MeasureSpec.getSize(heightMeasureSpec);
        if(widthSpecMode==MeasureSpec.AT_MOST&&heightSpecMode==MeasureSpec.AT_MOST){
            setMeasuredDimension(width,height);
        }else if(widthSpecMode==MeasureSpec.AT_MOST){
            setMeasuredDimension(width,heightSpecSize);
        }else if(heightSpecMode==MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpecSize,height);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //先画目标图片
        canvas.drawBitmap(mDstBitmap,0,0,mPaint);
        //设置xfermode
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //再画原图片
        canvas.drawBitmap(mSrcBitmap,0,0,mPaint);

        mPaint.setXfermode(null);
    }

    private void init() {
        //有些xfermode不支持硬件加速，因此要关闭
        setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        mSrcBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.ic_test_circle);
        mDstBitmap=BitmapFactory.decodeResource(getResources(),R.drawable.ic_bg);
        mPaint=new Paint();

        height = mDstBitmap.getHeight();
        width = mDstBitmap.getWidth();
    }
}

