package com.hs.samplewidget.myView.checked;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.RelativeLayout;

/**
 * Created by zhanghaitao on 2018/4/27.
 */

public class CheckRelativeLayout extends RelativeLayout implements Checkable {
    public CheckRelativeLayout(Context context) {
        super(context);
    }

    public CheckRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private boolean mChecked;

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
        }
        refreshDrawableState();
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
        int childCount = getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if(child instanceof CheckBox){
                    ((CheckBox) child).toggle();
                }
            }
        }
    }

    @Override
    public boolean performClick() {
        toggle();
        final boolean handled = super.performClick();
        if (!handled) {
            // View only makes a sound effect if the onClickListener was
            // called, so we'll need to make one here instead.
            playSoundEffect(SoundEffectConstants.CLICK);
        }
        return handled;
    }

    //选中状态对应的系统资源
    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    //重写View的onCreateDrawableState方法, 这个方法就是告诉xml当前的状态
    //然后,系统会通过此状态,设置对应的drawable
    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        //固定写法
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        //判断是否选择
        if (isChecked()) {
            //如果选择, 把选择的状态, 合并到现有的状态中.
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }



}
