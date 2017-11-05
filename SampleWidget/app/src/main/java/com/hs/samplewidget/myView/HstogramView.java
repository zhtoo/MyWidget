package com.hs.samplewidget.myView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author： Tom on 2017/11/3 22:32
 * @email： 820159571@qq.com
 *  这是一个轻量级的直方图的控件
 *  1、传入X、Y轴的坐标 可以自动生成对应的比例，绘制到布局上
 *  2、根据布局的大小自适应
 *  3、点击显示Y轴的数值，改变当前列的颜色
 *  
 */
public class HstogramView extends View {
    public HstogramView(Context context) {
        super(context);
    }

    public HstogramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HstogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    




}
