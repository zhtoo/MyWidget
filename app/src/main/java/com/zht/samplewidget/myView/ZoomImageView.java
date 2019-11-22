package com.zht.samplewidget.myView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.hs.samplewidget.R;

/**
 * 带有放大圆效果的图片
 * 作者：zhanghaitao on 2017/8/17 11:57
 * 邮箱：820159571@qq.com
 */

public class ZoomImageView extends View {

    private Bitmap bitmap;
    private Matrix mMatrix;
    private Bitmap mBitmapScale;
    private BitmapShader mBitmapShader;
    private ShapeDrawable mShapeDrawable;
    private Paint paint;

    //放大倍数
    private int FACTOR = 2;//建议是两倍，过大会很卡
    //圆的半径
    private int RADIUS = 80;
    //图片的宽高
    private int width;
    private int height;

    public ZoomImageView(Context context) {
        this(context, null);
        initView();
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initView();
    }


    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        //获取原图
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic1);

        width = bitmap.getWidth();
        height = bitmap.getHeight();

        //缩放图
        mBitmapScale = Bitmap.createScaledBitmap(bitmap, width * FACTOR
                , height * FACTOR, true);
        mBitmapShader = new BitmapShader(mBitmapScale, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mShapeDrawable = new ShapeDrawable(new OvalShape());
        mShapeDrawable.getPaint().setShader(mBitmapShader);
        mShapeDrawable.setBounds(0, 0, 0, 0);
        mMatrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        BitmapShaderTest(canvas);
    }


    private void BitmapShaderTest(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, null);
        mShapeDrawable.draw(canvas);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:

                int x = (int) event.getX();
                int y = (int) event.getY();
                //将放大的图片往相反的方向移动
                mMatrix.setTranslate(RADIUS - x * FACTOR, RADIUS - y * FACTOR +300);
                mBitmapShader.setLocalMatrix(mMatrix);
                //手指触摸点为圆心----考虑到时及的使用效果，显示区域应该在触摸点上方
                mShapeDrawable.setBounds(x - RADIUS, y - RADIUS - 150, x + RADIUS, y + RADIUS - 150);
                invalidate();
                return true;

            case MotionEvent.ACTION_UP:
                //将放大的图片往相反的方向移动
                mMatrix.setTranslate(0, 0);
                mBitmapShader.setLocalMatrix(mMatrix);
                //手指触摸点为圆心
                mShapeDrawable.setBounds(0, 0, 0, 0);
                invalidate();
                return true;

            default:
                return false;
        }
    }
}
