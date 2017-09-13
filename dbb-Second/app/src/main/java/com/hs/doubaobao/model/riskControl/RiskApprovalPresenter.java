package com.hs.doubaobao.model.riskControl;

import java.util.Map;

/**
 * 作者：zhanghaitao on 2017/9/12 13:55
 * 邮箱：820159571@qq.com
 *
 * @describe:
 */

public class RiskApprovalPresenter implements RiskApprovalContract.Presenter {

    private static final String TAG ="RiskApprovalPresenter" ;
    RiskApprovalContract.View viewRoot;

    public RiskApprovalPresenter(RiskApprovalContract.View viewRoot) {
        this.viewRoot = viewRoot;
        viewRoot.setPresenter(this);
    }


    @Override
    public void getData(Map mapParameter) {

    }
}
