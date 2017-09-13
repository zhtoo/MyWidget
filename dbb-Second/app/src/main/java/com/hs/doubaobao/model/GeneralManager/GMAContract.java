package com.hs.doubaobao.model.GeneralManager;

import com.hs.doubaobao.base.BasePresenter;
import com.hs.doubaobao.base.BaseView;

/**
 * 作者：zhanghaitao on 2017/9/12 10:59
 * 邮箱：820159571@qq.com
 *
 * @describe:
 */

public interface GMAContract {

    interface Presenter extends BasePresenter {
        //TODO:需要哪些获取数据的方法，就在此处定义
    }

    interface View extends BaseView<Presenter> {
        //TODO:在此处定义需要用来更新视图的方法
        void setData(String text);
        void setError(String text);
    }

}
