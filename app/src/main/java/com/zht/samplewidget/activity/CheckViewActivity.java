package com.zht.samplewidget.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hs.samplewidget.R;
import com.zht.samplewidget.myView.checked.CheckLinearLayout;

/**
 * Created by zhanghaitao on 2018/4/27.
 */

public class CheckViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_view);

         CheckLinearLayout viewById = (CheckLinearLayout)findViewById(R.id.cll);


//        viewById.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//              // viewById.toggle();
//            }
//        });



    }

}
