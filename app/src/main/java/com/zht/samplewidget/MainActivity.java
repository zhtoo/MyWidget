package com.zht.samplewidget;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zht.samplewidget.PieChart.activity.PielineChartActivity;
import com.hs.samplewidget.R;
import com.zht.samplewidget.RecyclerView.RecyclerViewActivity;
import com.zht.samplewidget.activity.CheckViewActivity;
import com.zht.samplewidget.activity.DemoActivity;
import com.zht.samplewidget.activity.DownloadAvtivity;
import com.zht.samplewidget.activity.DropDownActivity;
import com.zht.samplewidget.activity.LineChartActivity;
import com.zht.samplewidget.activity.LotteryNumberActivity;
import com.zht.samplewidget.activity.ProgressActivity;
import com.zht.samplewidget.activity.ViewPagerActivity;
import com.zht.samplewidget.activity.HstogramActivity;
import com.zht.samplewidget.activity.MakeViewPictrue;

import java.util.ArrayList;
import java.util.List;

/**
 * http://blog.csdn.net/lmj623565791/article/details/41967509
 */
public class MainActivity extends AppCompatActivity implements MyAdapter.onItemClickListener {
    public static final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private List<String> list =new ArrayList<>();
    private List<Class> activitys =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView =(RecyclerView) findViewById(R.id.main_recyclerview);
        initData();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new MyAdapter(this,list));
        //添加分割线
        mRecyclerView.addItemDecoration(new MyItemDecoration(this, MyItemDecoration.VERTICAL_LIST));
        MyAdapter adapter =(MyAdapter) mRecyclerView.getAdapter();
        adapter.setOnItemClickListener(this);
    }

    private void initData() {
        list.add("自己练习的demo");
        activitys.add(DemoActivity.class);
        list.add("下载动画View");
        activitys.add(DownloadAvtivity.class);
        list.add("将View保存为图片");
        activitys.add(MakeViewPictrue.class);
        list.add("绘画柱状图");
        activitys.add(HstogramActivity.class);
        list.add("ViewPager将两个View左右拖动");
        activitys.add(ViewPagerActivity.class);
        list.add("RecyclerView添加头布局和脚布局");
        activitys.add(RecyclerViewActivity.class);


        list.add("进度条吧");
        activitys.add(ProgressActivity.class);

        list.add("彩票数字");
        activitys.add(LotteryNumberActivity.class);
        list.add("带有可选属性的View");
        activitys.add(CheckViewActivity.class);

        list.add("扇形图View");
        activitys.add(PielineChartActivity.class);

        list.add("折线图View");
        activitys.add(LineChartActivity.class);

        list.add("下拉选择");
        activitys.add(DropDownActivity.class);
    }

    @Override
    public void onItemClick(int postion) {
        Intent intent = new Intent();
        intent.setClass(this,activitys.get(postion));
        startActivity(intent);
    }
}
