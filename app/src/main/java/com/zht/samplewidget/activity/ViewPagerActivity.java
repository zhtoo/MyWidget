package com.zht.samplewidget.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.hs.samplewidget.R;
import com.zht.samplewidget.activity.ViewPager.HorizontalPagerAdapter;
import com.zht.samplewidget.myView.HorizontalViewPager.HorizontalInfiniteCycleViewPager;
import com.zht.samplewidget.myView.chart.HstogramView;
import com.zht.samplewidget.myView.RotationViewPage.MyPagerAdapter3;
import com.zht.samplewidget.myView.RotationViewPage.RotationPageTransformer;
import com.zht.samplewidget.myView.dbbViewPager.Indicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：zhanghaitao on 2017/11/8 09:59
 * 邮箱：820159571@qq.com
 *
 * @describe:
 */

public class ViewPagerActivity extends AppCompatActivity {


    //创建handler对象，主要是为了实现ViewPager的自动滑动
    private Handler handler = new Handler();
    //创建一个集合装ViewPager加载的图片控件
    private List<View> views = new ArrayList<View>();
    private Indicator indicator;
    private ViewPager viewPager;
    private ViewPager viewPager3;
    private MyPagerAdapter3 mPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        //first ViewPager code
        HorizontalInfiniteCycleViewPager horizontalInfiniteCycleViewPager =
                (HorizontalInfiniteCycleViewPager) findViewById(R.id.hicvp);
        horizontalInfiniteCycleViewPager.setAdapter(new HorizontalPagerAdapter(this, false));

        //Second ViewPager Code
        //调用封装的方法进行自动的滑动
        //AutoScroll();
        //初始化ViewPager的item数据
        initData();
        //初始化控件
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        indicator = (Indicator) findViewById(R.id.indicator);
        //设置ViewPager适配器
        viewPager.setAdapter(new MyPagerAdapter());
        //设置ViewPager的监听器
        viewPager.setOnPageChangeListener(new MyPagerListner());

        //three ViewPager Code  viewPager3
        viewPager3 = (ViewPager) findViewById(R.id.viewPager3);

        mPagerAdapter = new MyPagerAdapter3(drawableIds,this);
        viewPager3.setAdapter(mPagerAdapter);
        viewPager3.setPageTransformer(true,new RotationPageTransformer());
        viewPager3.setOffscreenPageLimit(2);//设置预加载的数量，这里设置了2,会预加载中心item左边两个Item和右边两个Item
        viewPager3.setPageMargin(1);//设置两个Page之间的距离
        findViewById(R.id.viewPager3_container).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return viewPager3.dispatchTouchEvent(motionEvent);
            }
        });





    }

    //这里的图片自己去随便找几张吧
    private static final int[] drawableIds = new int[]{R.mipmap.ic1,R.mipmap.ic2, R.mipmap.ic3,
            R.mipmap.ic4, R.mipmap.ic5, R.mipmap.ic6, R.mipmap.ic7, R.mipmap.ic8,
            R.mipmap.ic9, R.mipmap.ic10, R.mipmap.ic11, R.mipmap.ic12};

    /**
     * 第二个ViewPager的代码Start
     */
    //初始化ViewPager的item数据
    private void initData() {
        for (int x = 0; x < 2; x++) {
            View inflate = getLayoutInflater().inflate(R.layout.pager_item, null);
          //  ImageView imgShow = (ImageView) inflate.findViewById(R.id.imgShow);
            HstogramView imgShow = (HstogramView) inflate.findViewById(R.id.my_hstogram);
            //这里就调用了系统的图片来作为资源
            //imgShow.setImageResource(R.mipmap.ic_launcher);
            views.add(inflate);
        }
    }

    private void AutoScroll() {
        //使用这个方法，第二个参数可以指定方法多少秒后执行
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //获取当前的页面下标
                int currentItem = viewPager.getCurrentItem();
                //在原有的页数加一就是下一个页数，然后设置跳到这一页，即可在次死循环里实现自动轮播的效果
                viewPager.setCurrentItem(currentItem + 1);
                //使用postDelayed(this,2000)可以让 run()里面的代码成为死循环
                handler.postDelayed(this, 2000);
            }
        }, 2000);
    }


    //创建ViewPager适配器，重写其四个构造方法
    class MyPagerAdapter extends PagerAdapter {

        //设置ViewPager的item数量
        @Override
        public int getCount() {
            return views.size()/*Integer.MAX_VALUE*/;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // position %= views.size();
            container.removeView(views.get(position));
        }

        //类似于listview里的getView，参数1：ViewPager的化身，参数2：item的位置
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //position %= views.size();
            //从集合里拿对应位置的图片
            View view = views.get(position);
            //把imageView对象添加到ViewPager里
            container.addView(view);
            return view;
        }
    }

    //创建ViewPager的监听事件
    class MyPagerListner implements ViewPager.OnPageChangeListener {
        //在ViewPager滑动时回调
        //参数1:item的位置  参数2：偏移的百分比，这个百分比用于接近于1  参数3：偏移量
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            indicator.setoffset(position, positionOffset);
        }

        //在ViewPager选中时回调
        @Override
        public void onPageSelected(int position) {
        }

        //在ViewPager滑动状态改变时回调
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
/** 第二个ViewPager的代码end*/






}
