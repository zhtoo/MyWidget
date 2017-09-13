package com.hs.doubaobao.model.GeneralManager;

import android.os.Bundle;

import com.hs.doubaobao.R;
import com.hs.doubaobao.base.AppBarActivity;

import java.util.LinkedHashMap;

/**
 * 作者：zhanghaitao on 2017/9/11 17:43
 * 邮箱：820159571@qq.com
 *
 * @describe:
 */

public class GeneralManagerApprovalActivity extends AppBarActivity implements GMAContract.View{

    private GMAContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_general_manager_approval);
        setContentView(R.layout.activity_risk_control_approval);
        setTitle(getString(R.string.general_manager));
        isShowRightView(false);
        setStatusBarBackground(R.color.color2);

        //将Presenter和View进行绑定
        new GMAPresener(this);
        //获取数据
        presenter.getData(new LinkedHashMap());

    }

    @Override
    public void setData(String text) {

    }

    @Override
    public void setError(String text) {

    }

    @Override
    public void setPresenter(GMAContract.Presenter presenter) {
        this.presenter = presenter ;
    }
}
