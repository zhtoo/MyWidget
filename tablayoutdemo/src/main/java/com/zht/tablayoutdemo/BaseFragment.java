package com.zht.tablayoutdemo;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

/**
 * 作者：zhanghaitao on 2017/8/16 17:13
 * 邮箱：820159571@qq.com
 */

public class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView view = new TextView(getContext());

        String string = "你多好的金萨芬就卡机的";
//        LogWrap.d(TAG,string);
//        LogWrap.e(TAG,string);
//        LogWrap.i(TAG,string);
//        LogWrap.v(TAG,string);
//        LogWrap.w(TAG,string);
        String test = "0123456789零一二三四五六七八九";
        String show = "";
        view.setText(show);
        return view;
    }*/


    public View getTextView(Class clazz){
        TextView view = new TextView(getContext());
        String show = clazz.getSimpleName();
        view.setText(show);
        return view;
    }




}
