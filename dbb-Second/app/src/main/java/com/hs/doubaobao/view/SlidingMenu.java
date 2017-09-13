package com.hs.doubaobao.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 首页的侧滑菜单控制器
 * rectification by zht on 2017/9/11  16:51
 */
public class SlidingMenu extends ViewGroup {

    private static final String TAG = "SlidingMenu";
    private View menu;//菜单
    private View main;//主界面
    private int menuWidth;//菜单的宽度
    private int downX;//手指按下的位置
    private int currentX;//当前x的坐标
    /**
     * 这个类专门用于模拟滚动的数值
     */
    private Scroller scroller;

    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);    // 测量容器自己的宽高
        menu = getChildAt(0); // 获取菜单容器
        main = getChildAt(1); // 获取主界面容器
        menuWidth = menu.getLayoutParams().width;     // 获取菜单的宽
        // 测量菜单
        menu.measure(menuWidth, heightMeasureSpec);
        // 测量主界面
        main.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // 对菜单进行排版
        int menuLeft = -menuWidth;    // 菜单的left位置为菜单的宽的负数
        int menuTop = 0;
        int menuRight = 0;
        int menuBottom = bottom - top;        // 菜单的bottom位置为容器的高
        menu.layout(menuLeft, menuTop, menuRight, menuBottom);
        // 对主界面进行排版
        int mainLeft = 0;
        int mainTop = 0;
        int mainRight = right - left;        // 主界面的right坐标和容器的宽一样
        int mainBottom = bottom - top;        // 主界面的botton坐标和容器的高一样
        main.layout(mainLeft, mainTop, mainRight, mainBottom);
    }

    /**
     * 让界面滚动到x的位置，传正数往右移，传负往左移
     *
     * @param x x>0 往右移  x<0 往左移
     */
    public void scrollTo(int x) {
        super.scrollTo(-x, 0);    // 系统的x，传正数往左移，传负往右
    }

    /**
     * 获取当前滑动到的位置
     */
    public int getMyScrollX() {
        return -super.getScrollX();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                downX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int distanceX = Math.abs((int) (ev.getX() - downX));
                int distanceY = Math.abs((int) (ev.getY() - downY));
                if (distanceX > distanceY) {
                    // 如果水平移动距离比垂直移动距离大，则认为是水平移动，把事件拦截，不让ScrollView使用
                    return true;    // true代表拦截事件
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int fingerMoveDistanceX = (int) event.getX() - downX;    // 手指移动的距离
                int destX = currentX + fingerMoveDistanceX;    // 界面要移动到的目标位置
                // 预防超出范围 （控制显示的界面是自己的）
                if (destX < 0) {
                    destX = 0;
                } else if (destX > menuWidth) {
                    destX = menuWidth;
                }
                //保证只显示菜单和主界面  presenter
                scrollTo(destX);
                break;
            case MotionEvent.ACTION_UP:
                int endX = (int) event.getX();
                int dX = endX - downX;
                //向右>0 ;向左<0;
                int adsX = Math.abs(dX);

                if (dX > 0) {//向右
                    if (adsX > menuWidth / 5) {
                        // 把菜单完全滑出来
                        startScroll(menuWidth);
                    } else {
                        startScroll(0);
                    }
                } else if (dX < 0) {//向左
                    if (adsX > menuWidth / 5) {
                        //菜单完全隐藏
                        startScroll(0);
                    } else {
                        startScroll(menuWidth);
                    }
                }
                break;
        }
        return true;
    }

    int count;
    private int downY;

    /**
     * 以动画的方式滚动到指定的位置
     *
     * @param destX 要滑动到哪里（目标位置）
     */
    private void startScroll(int destX) {
        currentX = destX;                // 界面当前的位置
        int startX = getMyScrollX();    // 指定从哪里开始滑动
        int distatnceX = destX - startX;// 要滑动的距离
        int duration = 800;
        scroller.startScroll(startX, 0, distatnceX, 0, duration);
        invalidate();
        // 刷新界面，内部会调用ViewGroup的draw方法，draw方法调用dispatchDraw方法-->
        // drawChild-->子View的draw方法-->computeScroll()
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {    // 如果数值没有模拟完，则返回true
            int currX = scroller.getCurrX();    // 模拟出来的滑动值
            scrollTo(currX);
            invalidate();
            count++;
        }
    }

    /**
     * 菜单的开关按钮，要么开，要么关
     */
    public void toggle() {
        if (getMyScrollX() > 0) {
            // 如果大于0，说明菜单已经显示出来了，需要隐藏
            startScroll(0);
        } else {
            // 需要完全显示
            Log.d("toggle", "被瞎点了");
            startScroll(menuWidth);
        }
    }

}
