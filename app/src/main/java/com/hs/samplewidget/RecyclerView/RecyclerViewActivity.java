package com.hs.samplewidget.RecyclerView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hs.samplewidget.MyItemDecoration;
import com.hs.samplewidget.R;

/**
 * 作者：zhanghaitao on 2017/11/22 09:13
 * 邮箱：820159571@qq.com
 *
 * @describe:
 */

public class RecyclerViewActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        mRecyclerView =(RecyclerView) findViewById(R.id.recyclerview);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        AddHeaderAndFootAdapter adapter = new AddHeaderAndFootAdapter(this);
        mRecyclerView.setAdapter(adapter);
        //添加分割线
        mRecyclerView.addItemDecoration(new MyItemDecoration(this, MyItemDecoration.VERTICAL_LIST));

    }

}
