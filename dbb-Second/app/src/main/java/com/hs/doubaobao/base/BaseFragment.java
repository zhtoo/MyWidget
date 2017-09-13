package com.hs.doubaobao.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hs.doubaobao.MyApplication;
import com.hs.doubaobao.threadpool.ThreadPoolProxyFactory;

/**
 * Fragment的基类
 * Created by zhanghaitao on 2017/5/19.
 */

public abstract class BaseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(setLayout(), container, false);
        initView(view);
        loadDataFromNet();
        return view;
    }

    protected abstract int setLayout();

    protected abstract void initView(View view);


    private LoadDataTask mLoadDataTask;

    /**
     * @des 联网加载数据
     * @called 联网加载数据的时候调用
     */
    public void loadDataFromNet() {
        //异步加载
        mLoadDataTask = new LoadDataTask();
        //利用线程池管理类开启线程
        ThreadPoolProxyFactory.getNormalThreadPoolProxy().submit(mLoadDataTask);
    }

    /**
     * 下载任务栈
     */
    class LoadDataTask implements Runnable {
        @Override
        public void run() {
            //联网加载数据---由子类实现
            loadData();
            //处理数据---由子类实现
            disposeData();
            MyApplication.getMainThreadHandler().post(new Runnable() {
                @Override
                public void run() {
                    //刷新UI---由子类实现
                    refreshView();
                }
            });
            //置空任务
            mLoadDataTask = null;
        }
    }

    public  void loadData(){}

    public  void disposeData(){}

    public  void refreshView(){}

}
