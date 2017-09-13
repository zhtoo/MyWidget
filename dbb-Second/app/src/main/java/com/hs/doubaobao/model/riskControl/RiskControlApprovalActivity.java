package com.hs.doubaobao.model.riskControl;

import android.os.Bundle;

import com.hs.doubaobao.R;
import com.hs.doubaobao.base.AppBarActivity;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 作者：zhanghaitao on 2017/9/11 17:42
 * 邮箱：820159571@qq.com
 *
 * @describe:
 */

public class RiskControlApprovalActivity extends AppBarActivity implements RiskApprovalContract.View{

    private RiskApprovalContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risk_control_approval);
        setTitle(getString(R.string.risk_control));
        isShowRightView(false);
        setStatusBarBackground(R.color.color2);

        new RiskApprovalPresenter(this);

        Map<String,String> map = new LinkedHashMap<>();
        map.put("amount","3");
        presenter.getData(map);

    }

    @Override
    public void setData(String text) {

    }

    @Override
    public void setError(String text) {

    }

    @Override
    public void setPresenter(RiskApprovalContract.Presenter presenter) {
            this.presenter = presenter;
    }

}
