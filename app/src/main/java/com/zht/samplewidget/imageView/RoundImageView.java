package com.zht.samplewidget.imageView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.zht.samplewidget.R;

/**
 * Created by zhanghaitao on 2017/5/28.
 * 基于CircleImageView修改的圆角矩形
 */

public class RoundImageView extends AppCompatImageView {

    private static final ScaleType SCALE_TYPE = ScaleType.CENTER;

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 2;

    private static final int DEFAULT_BORDER_WIDTH = 0;
    private static final int DEFAULT_CORNER_RADIUS = 0;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private static final int DEFAULT_FILL_COLOR = Color.TRANSPARENT;
    private static final boolean DEFAULT_BORDER_OVERLAY = false;
    private static final boolean DEFAULT_USE_SPECIAL_BORDER = false;

    private final RectF mDrawableRect = new RectF();
    private final RectF mBorderRect = new RectF();

    private final Matrix mShaderMatrix = new Matrix();

    private final Paint mBitmapPaint = new Paint();
    private final Paint mBorderPaint = new Paint();
    private final Paint mFillPaint = new Paint();
    //可以通过xml的属性来设置
    private int mBorderColor = DEFAULT_BORDER_COLOR;
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;
    private int mFillColor = DEFAULT_FILL_COLOR;
    private boolean mBorderOverlay;
    private boolean mUseSpecialBorder = DEFAULT_USE_SPECIAL_BORDER;
    //需要加载的图片
    private Bitmap mBitmap;
    //图片图形
    private BitmapShader mBitmapShader;
    private int mBitmapWidth;


    private int mBitmapHeight;

    private ColorFilter mColorFilter;
    private boolean mReady;
    private boolean mSetupPending;

    //圆角半径
    private int mCornerRadius;


    public RoundImageView(Context context) {
        super(context);
        init();
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView, defStyle, 0);

        mBorderWidth = a.getDimensionPixelSize(R.styleable.RoundImageView_riv_border_width, DEFAULT_BORDER_WIDTH);
        mBorderColor = a.getColor(R.styleable.RoundImageView_riv_border_color, DEFAULT_BORDER_COLOR);
        mBorderOverlay = a.getBoolean(R.styleable.RoundImageView_riv_border_overlay, DEFAULT_BORDER_OVERLAY);
        mFillColor = a.getColor(R.styleable.RoundImageView_riv_fill_color, DEFAULT_FILL_COLOR);
        mCornerRadius = a.getDimensionPixelSize(R.styleable.RoundImageView_riv_corner_radius, DEFAULT_CORNER_RADIUS);
        mUseSpecialBorder = a.getBoolean(R.styleable.RoundImageView_riv_use_special_border, DEFAULT_USE_SPECIAL_BORDER);
        a.recycle();
        init();
    }

    private void init() {
        super.setScaleType(SCALE_TYPE);
        mReady = true;

        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    @Override
    public ScaleType getScaleType() {
        return SCALE_TYPE;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        super.setScaleType(SCALE_TYPE);
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        if (adjustViewBounds) {
            throw new IllegalArgumentException("adjustViewBounds not supported.");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null) {
            return;
        }
        if (mUseSpecialBorder) {
            drawSpecial(canvas);
            return;
        }

        //方案一：使用drawRoundRect来实现描边圆角
        //缺点：描边宽度过长会导致内部图片边缘被裁剪
        drawNormal(canvas);

        //方案二：自己实现描边圆角规则
        //缺点：规则不统一，不适用所有需求
//        drawSpecial(canvas);
    }

    private void drawNormal(Canvas canvas) {
        if (mBitmap == null) {
            return;
        }
        RectF rect = new RectF(
                mBorderWidth / 2.0F, mBorderWidth / 2.0F,
                getWidth() - mBorderWidth / 2.0F,
                getHeight() - mBorderWidth / 2.0F);

        if (mFillColor != Color.TRANSPARENT) {
            canvas.drawRoundRect(rect,
                    mCornerRadius, //x轴的半径
                    mCornerRadius, //y轴的半径
                    mFillPaint);
        }
        //绘画图片
        canvas.drawRoundRect(rect,
                mCornerRadius, //x轴的半径
                mCornerRadius, //y轴的半径
                mBitmapPaint);
        //画边线
        if (mBorderWidth != 0) {
            if (mCornerRadius == 0) {
                canvas.drawRect(rect, mBorderPaint);
            } else {
                canvas.drawRoundRect(rect,
                        mCornerRadius, //x轴的半径
                        mCornerRadius, //y轴的半径
                        mBorderPaint);
            }
        }

    }


    private void drawSpecial(Canvas canvas) {

        RectF rect = new RectF(
                0, 0,
                getWidth(),
                getHeight());

        if (mFillColor != Color.TRANSPARENT) {
            canvas.drawRoundRect(rect,
                    mCornerRadius, //x轴的半径
                    mCornerRadius, //y轴的半径
                    mFillPaint);
        }
        //绘画图片
        canvas.drawRoundRect(rect,
                mCornerRadius, //x轴的半径
                mCornerRadius, //y轴的半径
                mBitmapPaint);
        //画边线
        if (mBorderWidth != 0) {
            if (mCornerRadius == 0) {
                canvas.drawRect(rect, mBorderPaint);
            } else {
                drawBorder(canvas);
            }
        }
    }

    private void drawBorder(Canvas canvas) {
        // TODO: 2020/5/21 还没有写好，估计不再继续写。
        //边界值
        float boundaryValue = Math.min(getHeight() / 2.0F, getWidth() / 2.0F);
        //是否是正方形
        boolean isSquare = getHeight() == getWidth();

        if (mCornerRadius >= boundaryValue) {
            mCornerRadius = (int) (boundaryValue + 0.5F);
            if (isSquare) {
                canvas.drawCircle(getWidth() / 2.0f,
                        getHeight() / 2.0f,
                        mCornerRadius - mBorderWidth / 2, mBorderPaint);
                return;
            }
        }

        //描边超过试图最短的一半
        if (mBorderWidth >= boundaryValue) {
            mBorderWidth = (int) (boundaryValue + 0.5F);
        }

        if ((mBorderWidth + mCornerRadius) >= boundaryValue) {
            //连个相加之和大于
            float borderRectWidth = mBorderWidth + mCornerRadius * 2;
            float borderRectHeight = mBorderWidth + mCornerRadius * 2;
            RectF oval = new RectF(
                    0,
                    0,
                    getHeight(),
                    getHeight());
            canvas.drawArc(oval, 175, 100, false, mBorderPaint);
            canvas.drawLine(
                    borderRectWidth / 2.0F + mBorderWidth / 2.0F,
                    mBorderWidth / 2.0F,
                    getWidth() - borderRectWidth / 2.0F - mBorderWidth / 2.0F,
                    mBorderWidth / 2.0F,
                    mBorderPaint);

            oval = new RectF(
                    getWidth() - borderRectWidth - mBorderWidth / 2.0F,
                    getHeight() - borderRectHeight - mBorderWidth / 2.0F,
                    getWidth() - mBorderWidth / 2,
                    getHeight() - mBorderWidth / 2);
            canvas.drawArc(oval, 355, 100, false, mBorderPaint);

            canvas.drawLine(
                    borderRectWidth / 2.0F + mBorderWidth / 2.0F,
                    getHeight() - mBorderWidth / 2.0F,
                    getWidth() - borderRectWidth / 2.0F - mBorderWidth / 2.0F,
                    getHeight() - mBorderWidth / 2.0F,
                    mBorderPaint);
            return;
        }


        float borderRectWidth = mBorderWidth + mCornerRadius * 2;
        float borderRectHeight = mBorderWidth + mCornerRadius * 2;

        RectF oval = new RectF(mBorderWidth / 2, mBorderWidth / 2, borderRectWidth + mBorderWidth / 2, borderRectHeight + mBorderWidth / 2);
        canvas.drawArc(oval, 175, 100, false, mBorderPaint);
        canvas.drawLine(
                borderRectWidth / 2.0F + mBorderWidth / 2.0F,
                mBorderWidth / 2.0F,
                getWidth() - borderRectWidth / 2.0F - mBorderWidth / 2.0F,
                mBorderWidth / 2.0F,
                mBorderPaint);

        oval = new RectF(getWidth() - borderRectWidth - mBorderWidth / 2.0F, mBorderWidth / 2.0F, getWidth() - mBorderWidth / 2.0F, borderRectHeight + mBorderWidth / 2.0F);
        canvas.drawArc(oval, 265, 100, false, mBorderPaint);

        canvas.drawLine(
                getWidth() - mBorderWidth / 2.0F,
                borderRectHeight / 2.0F + mBorderWidth / 2.0F,
                getWidth() - mBorderWidth / 2.0F,
                getHeight() - borderRectHeight / 2.0F - mBorderWidth / 2.0F,
                mBorderPaint);

        oval = new RectF(mBorderWidth / 2.0F,
                getHeight() - borderRectHeight - mBorderWidth / 2.0F,
                borderRectWidth + mBorderWidth / 2.0F,
                getHeight() - mBorderWidth / 2);
        canvas.drawArc(oval, 85, 100, false, mBorderPaint);

        canvas.drawLine(
                mBorderWidth / 2.0F,
                borderRectHeight / 2.0F + mBorderWidth / 2.0F,
                mBorderWidth / 2.0F,
                getHeight() - borderRectHeight / 2.0F - mBorderWidth / 2.0F,
                mBorderPaint);

        oval = new RectF(
                getWidth() - borderRectWidth - mBorderWidth / 2.0F,
                getHeight() - borderRectHeight - mBorderWidth / 2.0F,
                getWidth() - mBorderWidth / 2,
                getHeight() - mBorderWidth / 2);
        canvas.drawArc(oval, 355, 100, false, mBorderPaint);

        canvas.drawLine(
                borderRectWidth / 2.0F + mBorderWidth / 2.0F,
                getHeight() - mBorderWidth / 2.0F,
                getWidth() - borderRectWidth / 2.0F - mBorderWidth / 2.0F,
                getHeight() - mBorderWidth / 2.0F,
                mBorderPaint);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(@ColorInt int borderColor) {
        if (borderColor == mBorderColor) {
            return;
        }

        mBorderColor = borderColor;
        mBorderPaint.setColor(mBorderColor);
        invalidate();
    }

    public void setBorderColorResource(@ColorRes int borderColorRes) {
        setBorderColor(getContext().getResources().getColor(borderColorRes));
    }

    public int getFillColor() {
        return mFillColor;
    }

    public void setFillColor(@ColorInt int fillColor) {
        if (fillColor == mFillColor) {
            return;
        }

        mFillColor = fillColor;
        mFillPaint.setColor(fillColor);
        invalidate();
    }

    public void setFillColorResource(@ColorRes int fillColorRes) {
        setFillColor(getContext().getResources().getColor(fillColorRes));
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        if (borderWidth == mBorderWidth) {
            return;
        }

        mBorderWidth = borderWidth;
        setup();
    }

    public boolean isBorderOverlay() {
        return mBorderOverlay;
    }

    public void setBorderOverlay(boolean borderOverlay) {
        if (borderOverlay == mBorderOverlay) {
            return;
        }

        mBorderOverlay = borderOverlay;
        setup();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
        setup();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(drawable);
        setup();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        mBitmap = uri != null ? getBitmapFromDrawable(getDrawable()) : null;
        setup();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (cf == mColorFilter) {
            return;
        }

        mColorFilter = cf;
        mBitmapPaint.setColorFilter(mColorFilter);
        invalidate();
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setup() {
        if (!mReady) {
            mSetupPending = true;
            return;
        }

        if (getWidth() == 0 && getHeight() == 0) {
            return;
        }

        if (mBitmap == null) {
            invalidate();
            return;
        }

        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);


        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);
//        mBorderPaint.setStrokeCap(Paint.Cap.ROUND);// 圆形线帽
//        mBorderPaint.setStrokeJoin(Paint.Join.ROUND);//圆弧

        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setAntiAlias(true);
        mFillPaint.setColor(mFillColor);

        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();

        mBorderRect.set(0, 0, getWidth(), getHeight());

        mDrawableRect.set(mBorderRect);
        if (!mBorderOverlay) {
            mDrawableRect.inset(mBorderWidth, mBorderWidth);
        }

        updateShaderMatrix();
        invalidate();
    }

    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;

        mShaderMatrix.set(null);

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }

        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate(
                (int) (dx + 0.5f) + mDrawableRect.left,
                (int) (dy + 0.5f) + mDrawableRect.top);

        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

    public int getRadius() {
        return mCornerRadius;
    }

    public void setRadius(int mCornerRadius) {
        this.mCornerRadius = mCornerRadius;
        invalidate();
    }

}
