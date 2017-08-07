package com.hs.expandablelistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ExpandableListView.OnChildClickListener {

    private ExpandableListView elv;
    private MyExpandableListViewAdapter adapter;
    private Map<String, List<String>> dataset = new HashMap<>();
    private String[] parentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initELV();
    }

    private void initELV() {
        //初始化ExpandableListView控件
        elv = (ExpandableListView) findViewById(R.id.my_elv);
        //设置ExpandableListView的指向标为空
        //elv.setGroupIndicator(null);
        //初始化每个条目的数据
        initItemData();
        adapter = new MyExpandableListViewAdapter(this, dataset, parentList);
        elv.setAdapter(adapter);
        //设置
        elv.setOnChildClickListener(this);
    }

    /**
     * 初始化父条目和自条目的数据
     */
    private void initItemData() {
        //外层数据
        parentList = new String[]{"客户信息表", "客户需求表", "上传借款信息表照片"};

        List<String> childrenList1 = new ArrayList<>();
        List<String> childrenList2 = new ArrayList<>();
        List<String> childrenList3 = new ArrayList<>();

        //里层条目独居
        childrenList1.add("个人信息");
        childrenList1.add("个人配偶信息");
        childrenList1.add("共同借款人信息/保证人信息");
        childrenList1.add("配偶信息");
        childrenList1.add("紧急联系人信息");
        childrenList1.add("签字及日期");

        childrenList2.add("您的借款需求");
        childrenList2.add("*您的房产信息1");
        childrenList2.add("您的房产信息2");
        childrenList2.add("*您的车辆信息1");
        childrenList2.add("您的车辆信息2");
        childrenList2.add("申请人签字及日期");

        childrenList3.add("我就是占位子的");

        dataset.put(parentList[0], childrenList1);
        dataset.put(parentList[1], childrenList2);
        dataset.put(parentList[2], childrenList3);

    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

        Toast.makeText(this, "点击了["+groupPosition+","+childPosition+"]", Toast.LENGTH_SHORT).show();
        return false;
    }
}
