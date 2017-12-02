package com.zht.tablayoutdemo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private TabLayout mTabLayout;
    private ViewPager mViewpager;
    private TabFragmentPagerAdapter adapter;

    private List<Fragment> fragments;
    private String[] mTabItemNameArray;
    private int[] mTabItemIconArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        addFragment();
        initViewPager();
    }

    /**
     * 初始化试图和获取资源文件
     */
    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.main_tablayout);
        mViewpager = (ViewPager) findViewById(R.id.main_viewpager);

        mTabItemNameArray = getResources().getStringArray(R.array.main_tab_item);
        mTabItemIconArray = new int[]{
                R.drawable.ic_tab_selector,
                R.drawable.ic_tab1_selector,
                R.drawable.ic_tab2_selector,
                R.drawable.ic_tab3_selector
        };
    }



    /**
     * 在这里添加Fragment
     */
    private void addFragment() {
        if (mTabLayout == null || mViewpager == null) {
            return;
        }
        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new BusinessFragment());
        fragments.add(new DepartmentFragment());
        fragments.add(new PersonFragment());
    }

    private void initViewPager() {
        if (fragments == null) {
            return;
        }
        adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), fragments, mTabItemNameArray);
        mViewpager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewpager);
        //绘制自定义的Tab布局
        setupTabIcons();
    }

    /**
     * 设置条目的布局
     */
    private void setupTabIcons() {
        if (mTabLayout == null || mTabLayout.getTabCount() <= 0) {
            return;
        }
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            mTabLayout.getTabAt(i).setCustomView(getTabView(i));
        }
    }

    /**
     * 创建tab对应的视图
     * @param position
     * @return
     */
    public View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_tab, null);
        TextView txt_title = (TextView) view.findViewById(R.id.tab_text);
        //设置文字（文字的颜色只需要在xml里面设置一次）
        txt_title.setText(mTabItemNameArray[position]);
        //设置图片背景（只需要设置一次）
        ImageView img_title = (ImageView) view.findViewById(R.id.tab_icon);
        img_title.setImageResource(mTabItemIconArray[position]);
        return view;
    }

}
