package com.hs.doubaobao.model.main;

import com.hs.doubaobao.base.BasePresenter;
import com.hs.doubaobao.base.BaseView;

/**
 * 作者：zhanghaitao on 2017/9/12 14:10
 * 邮箱：820159571@qq.com
 *
 * @describe:
 */

public interface MainContract {

    interface Presenter extends BasePresenter {
        //TODO:需要哪些获取数据的方法，就在此处定义
    }

    interface View extends BaseView<Presenter> {
        //TODO:在此处定义需要用来更新视图的方法
        void setData(String text);
        void setError(String text);
    }



}
