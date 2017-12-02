package com.zht.tablayoutdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * 作者：zhanghaitao on 2017/8/17 09:11
 * 邮箱：820159571@qq.com
 */

public class PersonFragment extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        View view = inflater.inflate(R.layout.fragment_person, null, false);
//        return view;

        return getTextView(PersonFragment.class);
    }
}
