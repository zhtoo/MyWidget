package com.zht.samplewidget.myView;

import android.content.Context;
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


                MarginLayoutParams lp =(MarginLayoutParams) child.getLayoutParams();


                if (width >= viewWidth) {
                    currentWidth = 0;
                    if (i != 0) {
                        ChildViewPosition childViewPosition = childViewPositions.get(i - 1);
                        currentHeight += childViewPosition.bottom - childViewPosition.top;
                    }
                    ChildViewPosition viewPosition = new ChildViewPosition();
                    viewPosition.left = currentWidth;
                    viewPosition.top = currentHeight;
                    viewPosition.right = viewWidth;
                    viewPosition.bottom = currentHeight + height;
                    childViewPositions.add(viewPosition);
                    currentHeight += height;
                } else if (currentWidth + width < viewWidth) {
                    ChildViewPosition viewPosition = new ChildViewPosition();
                    viewPosition.left = currentWidth;
                    viewPosition.top = currentHeight;
                    viewPosition.right = currentWidth + width;
                    viewPosition.bottom = currentHeight + height;
                    childViewPositions.add(viewPosition);
                    currentWidth += width;
                } else {
                    currentWidth = 0;
                    ChildViewPosition childViewPosition = childViewPositions.get(i - 1);
                    currentHeight += childViewPosition.bottom - childViewPosition.top;
                    ChildViewPosition viewPosition = new ChildViewPosition();
                    viewPosition.left = currentWidth;
                    viewPosition.top = currentHeight;
                    viewPosition.right = currentWidth + width;
                    viewPosition.bottom = currentHeight + height;
                    childViewPositions.add(viewPosition);
                    currentHeight += height;
                }
            }
        }
        ChildViewPosition childViewPosition = childViewPositions.get(childViewPositions.size() - 1);
        if (childViewPosition.left == 0) {
            currentHeight += (childViewPosition.bottom-childViewPosition.top);
        }


        setMeasuredDimension(viewWidth, currentHeight);
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
