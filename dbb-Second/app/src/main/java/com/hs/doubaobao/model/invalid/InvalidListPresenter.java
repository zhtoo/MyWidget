package com.hs.doubaobao.model.invalid;

import java.util.Map;

/**
 * 作者：zhanghaitao on 2017/9/12 13:58
 * 邮箱：820159571@qq.com
 *
 * @describe:
 */

public class InvalidListPresenter implements InvalidListContract.Presenter {

    private static final String TAG ="InvalidListPresenter" ;
    InvalidListContract.View viewRoot;

    public InvalidListPresenter(InvalidListContract.View viewRoot) {
        this.viewRoot = viewRoot;
        viewRoot.setPresenter(this);
    }

    @Override
    public void getData(Map mapParameter) {

    }
}
