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
        parentList = new String[]{"我会的语言", "开发语言", "开发工具"};

        List<String> childrenList1 = new ArrayList<>();
        List<String> childrenList2 = new ArrayList<>();
        List<String> childrenList3 = new ArrayList<>();

        //里层条目独居
        childrenList1.add("java");
        childrenList1.add("kotlin");
        childrenList1.add("JavaScript");
        childrenList1.add("c++");

        childrenList2.add("java");
        childrenList2.add("c#");
        childrenList2.add("c++");
        childrenList2.add("c");
        childrenList2.add("JavaScript");
        childrenList2.add("PHP");
        childrenList2.add("Python");
        childrenList2.add("VB");

        childrenList3.add("Android Studio");


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
