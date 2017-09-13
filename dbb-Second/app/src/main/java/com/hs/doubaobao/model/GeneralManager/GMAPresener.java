package com.hs.doubaobao.model.GeneralManager;

import java.util.Map;

/**
 * 作者：zhanghaitao on 2017/9/12 11:03
 * 邮箱：820159571@qq.com
 *
 * @describe:
 */

public class GMAPresener implements GMAContract.Presenter {

    private static final String TAG ="GMAPresener" ;
    GMAContract.View viewRoot;

    public GMAPresener(GMAContract.View viewRoot) {
        this.viewRoot = viewRoot;
        viewRoot.setPresenter(this);
    }

    @Override
    public void getData(Map mapParameter) {

        viewRoot.setData("13256");
        viewRoot.setError("错误");

    }
}
