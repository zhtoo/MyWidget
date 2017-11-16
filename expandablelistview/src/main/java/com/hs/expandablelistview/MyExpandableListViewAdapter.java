package com.hs.expandablelistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * 作者：zhanghaitao on 2017/8/7 11:01
 * 邮箱：820159571@qq.com
 */

public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {



    private Context context;
    private Map<String, List<String>> dataset;
    private String[] parentList;

    public MyExpandableListViewAdapter(Context context, Map<String, List<String>> dataset, String[] parentList) {
        this.context = context;
        this.dataset = dataset;
        this.parentList = parentList;
    }


    /**
     *需要重写的方法
     */
    //获得父项的数量
    @Override
    public int getGroupCount() {
        return  dataset == null?0:dataset.size();
    }
    //获得父项对应的子项数目
    @Override
    public int getChildrenCount(int groupPosition) {
        return dataset.get(parentList[groupPosition]).size();
    }

    //获得父项的条目
    @Override
    public Object getGroup(int groupPosition) {
        return dataset.get(parentList[groupPosition]);
    }

    //获得父项对应子项的条目
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return dataset.get(parentList[groupPosition]).get(childPosition);
    }
    // 获得父项的id
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    //获得父项对应子项的id
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    //按函数的名字来理解应该是是否具有稳定的id，这个方法目前一直都是返回false，没有去改动过
    @Override
    public boolean hasStableIds() {
        return false;
    }


    //获得父项显示的view
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_parent, null);
        }

        TextView text = (TextView) convertView.findViewById(R.id.parent_title);
        text.setText(parentList[groupPosition]);


        ImageView mImage = (ImageView) convertView.findViewById(R.id.parent_image);
        //判断是否已经打开列表
        if (isExpanded) {
            mImage.setBackgroundResource(R.drawable.ic_arrow_up);
        } else {
            mImage.setBackgroundResource(R.drawable.ic_arrow_down);
        }
        return convertView;
    }
    //获得子项显示的view
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_child, null);
        }
        TextView text = (TextView) convertView.findViewById(R.id.child_title);
        text.setText(dataset.get(parentList[groupPosition]).get(childPosition));
        return convertView;
    }
    //子项是否可选中，如果需要设置子项的点击事件，需要返回true
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    /**
     * 设置监听回调接口
     */
    public static onItemClickListener listener;

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public interface onItemClickListener {
        void onItemClick(int parentposion, int childposion);

    }




}
