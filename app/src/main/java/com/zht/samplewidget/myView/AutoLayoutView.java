package com.zht.samplewidget.myView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhangHaitao on 2019/11/25
 */
public class AutoLayoutView extends ViewGroup {

    private final String TAG = "AutoLayoutView";

    private List<ChildViewPosition> childViewPositions;

    public AutoLayoutView(Context context) {
        super(context);
    }

    public AutoLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int currentHeight = 0;
        int currentWidth = 0;
        int viewWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        childViewPositions = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final int width = child.getMeasuredWidth()
                        + child.getPaddingLeft() + child.getPaddingRight();

                final int height = child.getMeasuredHeight()
                        + child.getPaddingTop() + child.getPaddingBottom();

                MarginLayoutParams lp = null;

                int leftMargin = 0;
                int rightMargin = 0;
                int topMargin = 0;
                int bottomMargin = 0;

                if (child.getLayoutParams() instanceof MarginLayoutParams) {
                    lp = (MarginLayoutParams) child.getLayoutParams();
                    leftMargin = lp.leftMargin;
                    rightMargin = lp.rightMargin;
                    topMargin = lp.topMargin;
                    bottomMargin = lp.bottomMargin;
                }

                //是否需要换行
                boolean beyondWidth =
                        ((width +  leftMargin +  rightMargin) >= viewWidth) ||
                                ((currentWidth + width +  leftMargin /*+ lp.rightMargin*/) >= viewWidth);
                if (beyondWidth) {
                    currentWidth = 0;
                    if (i != 0) {
                        ChildViewPosition childViewPosition = childViewPositions.get(i - 1);
                        MarginLayoutParams preChildLp = null;
                        int preBottomMargin = 0;
                        if (child.getLayoutParams() instanceof MarginLayoutParams) {
                            preBottomMargin = lp.bottomMargin;
                        }
                        currentHeight = childViewPosition.bottom + preBottomMargin;
                    }
                    ChildViewPosition viewPosition = getChildViewPosition(currentHeight, currentWidth, width, height, leftMargin, topMargin);
                    checkChildWidth(viewWidth, child, rightMargin, viewPosition);
                    childViewPositions.add(viewPosition);
                    currentHeight = viewPosition.bottom + bottomMargin;
                } else {
                    ChildViewPosition viewPosition = getChildViewPosition(currentHeight, currentWidth, width, height, leftMargin, topMargin);
                    checkChildWidth(viewWidth, child, rightMargin, viewPosition);
                    childViewPositions.add(viewPosition);
                    currentWidth = viewPosition.right + rightMargin;

                    if (i == getChildCount() - 1) {
                        currentHeight = viewPosition.bottom + bottomMargin;
                    }
                }
            }
        }
        setMeasuredDimension(viewWidth, currentHeight);
    }

    private void checkChildWidth(int viewWidth, View child, int rightMargin, ChildViewPosition viewPosition) {
        if ((viewPosition.right + rightMargin) > viewWidth) {
            viewPosition.right = viewWidth - rightMargin;
            child.measure(getMeasureSpec(viewPosition.right - viewPosition.left),
                    getMeasureSpec(viewPosition.bottom - viewPosition.top));
        }
    }

    @NonNull
    private ChildViewPosition getChildViewPosition(int currentHeight, int currentWidth, int width, int height, int leftMargin, int topMargin) {
        ChildViewPosition viewPosition = new ChildViewPosition();
        viewPosition.left = currentWidth + leftMargin;
        viewPosition.top = currentHeight + topMargin;
        viewPosition.right = viewPosition.left + width;
        viewPosition.bottom = viewPosition.top + height;
        return viewPosition;
    }

    public static int getMeasureSpec(int specSize) {
        return MeasureSpec.makeMeasureSpec(specSize, MeasureSpec.EXACTLY);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                ChildViewPosition viewPosition = childViewPositions.get(i);
                child.layout(
                        viewPosition.left,
                        viewPosition.top,
                        viewPosition.right,
                        viewPosition.bottom);
            }
        }

    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    public static class ChildViewPosition {
        public int left;
        public int top;
        public int right;
        public int bottom;
    }

}
