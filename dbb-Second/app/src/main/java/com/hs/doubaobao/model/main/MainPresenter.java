package com.hs.doubaobao.model.main;

import android.util.Log;

import java.util.Map;

/**
 * 作者：zhanghaitao on 2017/9/12 14:12
 * 邮箱：820159571@qq.com
 *
 * @describe:
 */

public class MainPresenter implements MainContract.Presenter {

    private static final String TAG ="MainPresenter" ;
    MainContract.View viewRoot;

    public MainPresenter(MainContract.View viewRoot) {
        this.viewRoot = viewRoot;
        viewRoot.setPresenter(this);
    }

    @Override
    public void getData(Map mapParameter) {
        String  o =(String) mapParameter.get("1");

        Log.d(TAG,o);
        viewRoot.setError(o);
        viewRoot.setData("大家好");

    }
}
