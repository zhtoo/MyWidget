package com.hs.doubaobao.model.detail;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;

import com.hs.doubaobao.R;
import com.hs.doubaobao.base.AppBarActivity;
import com.hs.doubaobao.model.detail.particulars.ParticularsFragment;
import com.hs.doubaobao.model.detail.pictrue.PictrueFragment;
import com.hs.doubaobao.model.detail.reference.ReferenceFragment;
import com.hs.doubaobao.model.detail.video.VideoFragment;

/**
 * 作者：zhanghaitao on 2017/9/12 14:53
 * 邮箱：820159571@qq.com
 *
 * @describe:  管理资料查看的各个选项的显示和隐藏。不负责具体的逻辑操作（不需要再做修改）
 */

public class DetailActivity extends AppBarActivity {

    private static final String TAG = "DetailActivity";
    //fragment 管理器
    private FragmentManager mFragmentManager;
    //fragment 控制器
    private FragmentTransaction fragmentTs;
    private static final String[] TAGS = {"Particulars", "Pictrue", "Video", "Reference"};

    //fangment
    private ParticularsFragment mParticularsFragment;
    private PictrueFragment mPictrueFragment;
    private VideoFragment mVideoFragment;
    private ReferenceFragment mReferenceFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle(getString(R.string.read_data));
        isShowRightView(false);

        mFragmentManager = getSupportFragmentManager();

        RadioButton bt = (RadioButton) findViewById(R.id.detail_particulars_bt);
        bt.setChecked(true);
        showParticulars(bt);

    }


    /**
     * 显示详情
     */
    public void showParticulars(View view) {
        if (null != mParticularsFragment && !mParticularsFragment.isHidden()) {
            return;
        }
        hideAllFragment();
        if (mParticularsFragment == null) {
            mParticularsFragment = (ParticularsFragment) mFragmentManager.findFragmentByTag(TAGS[0]);
        }
        if (mParticularsFragment == null) {
            mParticularsFragment = new ParticularsFragment();
            fragmentTs.add(R.id.preview_content, mParticularsFragment, TAGS[0]);
        } else {
            fragmentTs.show(mParticularsFragment);
        }
        fragmentTs.commit();
    }


    /**
     * 显示图片
     */
    public void showPictrue(View view) {
        if (null != mPictrueFragment && !mPictrueFragment.isHidden()) {
            return;
        }
        hideAllFragment();
        if (mPictrueFragment == null) {
            mPictrueFragment = (PictrueFragment) mFragmentManager.findFragmentByTag(TAGS[1]);
        }
        if (mPictrueFragment == null) {
            mPictrueFragment = new PictrueFragment();
            fragmentTs.add(R.id.preview_content, mPictrueFragment, TAGS[1]);
        } else {
            fragmentTs.show(mPictrueFragment);
        }
        fragmentTs.commit();

    }

    /**
     * 显示视频
     */
    public void showVideo(View view) {
        if (null != mVideoFragment && !mVideoFragment.isHidden()) {
            return;
        }
        hideAllFragment();
        if (mVideoFragment == null) {
            mVideoFragment = (VideoFragment) mFragmentManager.findFragmentByTag(TAGS[2]);
        }
        if (mVideoFragment == null) {
            mVideoFragment = new VideoFragment();
            fragmentTs.add(R.id.preview_content, mVideoFragment, TAGS[2]);
        } else {
            fragmentTs.show(mVideoFragment);
        }
        fragmentTs.commit();
    }

    /**
     * 显示参考
     */
    public void showReference(View view) {
        if (null != mReferenceFragment && !mReferenceFragment.isHidden()) {
            return;
        }
        hideAllFragment();
        if (mReferenceFragment == null) {
            mReferenceFragment = (ReferenceFragment) mFragmentManager.findFragmentByTag(TAGS[3]);
        }
        if (mReferenceFragment == null) {
            mReferenceFragment = new ReferenceFragment();
            fragmentTs.add(R.id.preview_content, mReferenceFragment, TAGS[3]);
        } else {
            fragmentTs.show(mReferenceFragment);
        }
        fragmentTs.commit();
    }

    /**
     * 隐藏所有Fragment
     */
    private void hideAllFragment() {
        fragmentTs = mFragmentManager.beginTransaction();
        // 为空，则尝试用TAG去获取fragment对象
        if (mParticularsFragment == null) {
            mParticularsFragment = (ParticularsFragment) mFragmentManager.findFragmentByTag(TAGS[0]);
        }
        if (mPictrueFragment == null) {
            mPictrueFragment = (PictrueFragment) mFragmentManager.findFragmentByTag(TAGS[1]);
        }
        if (mVideoFragment == null) {
            mVideoFragment = (VideoFragment) mFragmentManager.findFragmentByTag(TAGS[2]);
        }
        if (mReferenceFragment == null) {
            mReferenceFragment = (ReferenceFragment) mFragmentManager.findFragmentByTag(TAGS[3]);
        }
        // 如果不为空，则隐藏
        if (mParticularsFragment != null) {
            fragmentTs.hide(mParticularsFragment);
        }
        if (mPictrueFragment != null) {
            fragmentTs.hide(mPictrueFragment);
        }
        if (mVideoFragment != null) {
            fragmentTs.hide(mVideoFragment);
        }
        if (mReferenceFragment != null) {
            fragmentTs.hide(mReferenceFragment);
        }
    }


}
