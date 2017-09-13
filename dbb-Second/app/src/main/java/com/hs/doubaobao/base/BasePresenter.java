package com.hs.doubaobao.base;

import java.util.Map;

/**
 * 作者：zhanghaitao on 2017/9/12 10:45
 * 邮箱：820159571@qq.com
 *
 * @describe: 接口的基类
 */


public interface BasePresenter {
    /**
     *  获取数据的方法(联网操作都需要参数，具体参数由view给出)
     */
    void getData(Map mapParameter);

}
