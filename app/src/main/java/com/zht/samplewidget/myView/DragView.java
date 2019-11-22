package com.zht.samplewidget.myView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 作者：zhanghaitao on 2017/11/8 09:59
 * 邮箱：820159571@qq.com
 *
 * @describe:
 */

public class DragView extends ViewGroup {


    private final Context context;
    /**
     * 这个类专门用于模拟滚动的数值
     */
    private final Scroller scroller;

    private View first;//菜单
    private View second;//主界面
    private int firstWidth;
    private int firstHeight;

    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        scroller = new Scroller(context);
    }


    /**
     * 测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);// 测量容器自己的宽高
        //获取条目中的子控件
        first = getChildAt(0);
        second = getChildAt(1);
        //获取子控件的宽
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST || widthSpecMode == MeasureSpec.UNSPECIFIED) {
            //默认wrap_content为自己定义的高度
            firstWidth = 800;
        } else {
            firstWidth = widthSpecSize;
        }

        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightSpecMode == MeasureSpec.AT_MOST ||
                heightSpecMode == MeasureSpec.UNSPECIFIED//这里是为了解决在Listview和ScrollView的嵌套
                ) {
            //默认wrap_content为自己定义的高度
            firstHeight= 800;
        } else {
            firstHeight = heightSpecSize;
        }

        // 测量菜单
        first.measure(firstWidth, firstHeight);
        // 测量主界面
        second.measure(firstWidth, firstHeight);
    }



    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // 对子控件进行排版
        int firstLeft = left;    // 菜单的left位置为菜单的宽的负数
        int firstTop = 20;
        int firstRight =firstWidth;
        int firstBottom = bottom - top;        // 菜单的bottom位置为容器的高
        first.layout(firstLeft, firstTop, firstRight, firstBottom);

        // 对主界面进行排版
        int secondLeft = firstRight;
        int secondTop = 20;
        int secondRight = secondLeft + firstWidth;        // 主界面的right坐标和容器的宽一样
        int secondBottom = bottom - top;        // 主界面的botton坐标和容器的高一样
        second.layout(secondLeft, secondTop, secondRight, secondBottom);
    }


}
