package com.hs.doubaobao.base;

/**
 * 作者：zhanghaitao on 2017/9/12 10:45
 * 邮箱：820159571@qq.com
 *
 * @describe: View的基类
 */

public interface BaseView<T> {
    /**
     * 给View传递Presenter实例
     * @param presenter
     */
    void setPresenter(T presenter);

}
