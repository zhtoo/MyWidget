package com.zht.tablayoutdemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Tablayout的FragmentPager适配器
 * 用于将Fragment加载到ViewPager中
 * 作者：zhanghaitao on 2017/8/15 09:48
 * 邮箱：820159571@qq.com
 */

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

    /**
     * fragment集合
     */
    private List<Fragment> mFragmentList = null;
    /**
     * 标题集合
     */
    private String[] titles;


    public TabFragmentPagerAdapter(FragmentManager mFragmentManager,
                                   ArrayList<Fragment> fragmentList) {
        super(mFragmentManager);
        mFragmentList = fragmentList;
    }

    /**
     * titles是给TabLayout设置title用的
     *
     * @param mFragmentManager
     * @param fragmentList
     * @param titles
     */
    public TabFragmentPagerAdapter(FragmentManager mFragmentManager,
                                   List<Fragment> fragmentList, String[] titles) {
        super(mFragmentManager);
        mFragmentList = fragmentList;
        this.titles = titles;
    }

    /**
     * 获取数量
     *
     * @return the count
     * @see android.support.v4.view.PagerAdapter#getCount()
     */
    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    /**
     * 获取索引位置的Fragment.
     *
     * @param position the position
     * @return the item
     * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
     */
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        if (position < mFragmentList.size()) {
            fragment = mFragmentList.get(position);
        } else {
            fragment = mFragmentList.get(0);
        }
        return fragment;
    }

    /**
     * 将标题设置到相对应的位置
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null && titles.length > 0)
            return titles[position];
        return null;
    }
}