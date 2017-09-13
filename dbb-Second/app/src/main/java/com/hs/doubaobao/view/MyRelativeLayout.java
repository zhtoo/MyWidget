package com.hs.doubaobao.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * 作者：zhanghaitao on 2017/9/8 15:19
 * 邮箱：820159571@qq.com
 * rectification by zht on 2017/9/11  16:51
 * @describe:
 */

public class MyRelativeLayout extends RelativeLayout {

    private static final String TAG = "MyRelativeLayout";
    private int downX;
    private int downY;

    public MyRelativeLayout(Context context) {
        super(context);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //请求父类不拦截
        requestDisallowInterceptTouchEvent(true);
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int distanceX = Math.abs((int) (ev.getX() - downX));
                int distanceY = Math.abs((int) (ev.getY() - downY));
                if (distanceX < distanceY) {
                    // 如果水平移动距离比垂直移动距离大，则认为是水平移动，把事件拦截，不让ScrollView使用
                    Log.d(TAG, "拦截事件");
                    return true;    // true代表拦截事件
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //请求父类不拦截
        requestDisallowInterceptTouchEvent(true);
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                return true;    // true代表拦截事件
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
