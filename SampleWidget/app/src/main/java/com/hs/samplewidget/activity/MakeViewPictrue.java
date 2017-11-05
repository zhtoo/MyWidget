package com.hs.samplewidget.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hs.samplewidget.R;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 用于将View保存为图片的工具类
 * 作者：zhanghaitao on 2017/8/28 14:29
 * 邮箱：820159571@qq.com
 */

public class MakeViewPictrue extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout myView;
    private Button button;
    private int RequestCode = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_view_pictrue);
        myView = (RelativeLayout) findViewById(R.id.mypic_bitmap);
        button = (Button) findViewById(R.id.save_pic);
        button.setOnClickListener(this);
        //定义一个变量 记录当前权限的状态
        int checkSlfePermission =
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //当前么有相应的权限
        if (checkSlfePermission == PackageManager.PERMISSION_DENIED) {
            //申请权限 （弹出一个申请权限的对话框）
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequestCode);
        } else
            //申请到了权限
            if (checkSlfePermission == PackageManager.PERMISSION_GRANTED) {

            }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RequestCode == requestCode) {
            Log.d("我好烦", "" + resultCode);
        } else {
            Log.d("我好烦", "" + resultCode);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 将bitmap保存为JPG格式的图片
     *
     * @param bitmap
     */
    private void saveMypic(Bitmap bitmap) {
        //非空判断
        if (bitmap == null) return;
        // 保存图片
        try {
            File file = new File(Environment.getExternalStorageDirectory(),
                    SystemClock.currentThreadTimeMillis() + ".png");
            String url = file.getAbsolutePath();
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();
            Toast.makeText(this, "保存成功，已经保存到" + url, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap makeView2Bitmap(View view) {
        //View是你需要绘画的View
        int width = view.getWidth();
        int height = view.getHeight();
        //创建bitmap对象，设置宽高、和编码类型
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //创建canvas对象，将bitmap传入。
        Canvas canvas = new Canvas(bitmap);
        //如果不设置canvas画布为白色，则生成透明
        canvas.drawColor(Color.WHITE);
        //将View绘画到canvas中
        view.draw(canvas);
        return bitmap;
    }

    @Override
    public void onClick(View view) {
        Bitmap viewBitmap = makeView2Bitmap(myView);
        saveMypic(viewBitmap);
    }


}
