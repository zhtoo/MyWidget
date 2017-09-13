package com.hs.doubaobao.model.invalid;

import android.os.Bundle;

import com.hs.doubaobao.R;
import com.hs.doubaobao.base.AppBarActivity;

import java.util.LinkedHashMap;

/**
 * 作者：zhanghaitao on 2017/9/11 17:44
 * 邮箱：820159571@qq.com
 *
 * @describe:
 */

public class InvalidListActivity extends AppBarActivity  implements InvalidListContract.View{

    private  InvalidListContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invalid_list);

        setTitle(getString(R.string.invalid_list));
        isShowRightView(false);
        setStatusBarBackground(R.color.color2);

        new InvalidListPresenter(this);
        presenter.getData(new LinkedHashMap());

    }

    @Override
    public void setData(String text) {

    }

    @Override
    public void setError(String text) {

    }

    @Override
    public void setPresenter(InvalidListContract.Presenter presenter) {
        this.presenter = presenter ;
    }
}
