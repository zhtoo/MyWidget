
package com.zht.samplewidget.PieChart.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.zht.samplewidget.PieChart.ColorTemplate;
import com.zht.samplewidget.PieChart.Entry;
import com.zht.samplewidget.PieChart.Highlight;
import com.zht.samplewidget.PieChart.Legend;
import com.zht.samplewidget.PieChart.OnChartValueSelectedListener;
import com.zht.samplewidget.PieChart.PieChart;
import com.zht.samplewidget.PieChart.PieData;
import com.zht.samplewidget.PieChart.PieDataSet;
import com.zht.samplewidget.PieChart.PieEntry;
import com.zht.samplewidget.PieChart.animation.Easing;
import com.zht.samplewidget.R;

import java.util.ArrayList;

public class PielineChartActivity extends AppCompatActivity implements OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    protected final String[] parties = new String[] {
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };

    private PieChart chart;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_piechart);

        tvX = findViewById(R.id.tvXMax);
        tvY = findViewById(R.id.tvYMax);

        seekBarX = findViewById(R.id.seekBar1);
        seekBarY = findViewById(R.id.seekBar2);

        seekBarX.setOnSeekBarChangeListener(this);
        seekBarY.setOnSeekBarChangeListener(this);

        chart = findViewById(R.id.chart1);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);


        chart.setCenterText("中间文字提示");

        chart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(0f);
        chart.setTransparentCircleRadius(0f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);

        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);


        chart.setOnChartValueSelectedListener(this);

        seekBarX.setProgress(4);
        seekBarY.setProgress(100);

        chart.animateY(1400, Easing.EaseInOutQuad);


        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);
    }

    private void setData(int count, float range) {

        ArrayList<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            entries.add(new PieEntry((float) (Math.random() * range) + range / 5, parties[i % parties.length]));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        //添加颜色

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);



        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);

        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);

        chart.setData(data);


        chart.highlightValues(null);

        chart.invalidate();
    }

//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.viewGithub: {
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/PiePolylineChartActivity.java"));
//                startActivity(i);
//                break;
//            }
//            case R.id.actionToggleValues: {
//                for (IDataSet<?> set : chart.getData().getDataSets())
//                    set.setDrawValues(!set.isDrawValuesEnabled());
//
//                chart.invalidate();
//                break;
//            }
//            case R.id.actionToggleHole: {
//                if (chart.isDrawHoleEnabled())
//                    chart.setDrawHoleEnabled(false);
//                else
//                    chart.setDrawHoleEnabled(true);
//                chart.invalidate();
//                break;
//            }
//            case R.id.actionToggleMinAngles: {
//                if (chart.getMinAngleForSlices() == 0f)
//                    chart.setMinAngleForSlices(36f);
//                else
//                    chart.setMinAngleForSlices(0f);
//                chart.notifyDataSetChanged();
//                chart.invalidate();
//                break;
//            }
//            case R.id.actionToggleCurvedSlices: {
//                boolean toSet = !chart.isDrawRoundedSlicesEnabled() || !chart.isDrawHoleEnabled();
//                chart.setDrawRoundedSlices(toSet);
//                if (toSet && !chart.isDrawHoleEnabled()) {
//                    chart.setDrawHoleEnabled(true);
//                }
//                if (toSet && chart.isDrawSlicesUnderHoleEnabled()) {
//                    chart.setDrawSlicesUnderHole(false);
//                }
//                chart.invalidate();
//                break;
//            }
//            case R.id.actionDrawCenter: {
//                if (chart.isDrawCenterTextEnabled())
//                    chart.setDrawCenterText(false);
//                else
//                    chart.setDrawCenterText(true);
//                chart.invalidate();
//                break;
//            }
//            case R.id.actionToggleXValues: {
//
//                chart.setDrawEntryLabels(!chart.isDrawEntryLabelsEnabled());
//                chart.invalidate();
//                break;
//            }
//            case R.id.actionTogglePercent:
//                chart.setUsePercentValues(!chart.isUsePercentValuesEnabled());
//                chart.invalidate();
//                break;
//            case R.id.animateX: {
//                chart.animateX(1400);
//                break;
//            }
//            case R.id.animateY: {
//                chart.animateY(1400);
//                break;
//            }
//            case R.id.animateXY: {
//                chart.animateXY(1400, 1400);
//                break;
//            }
//            case R.id.actionToggleSpin: {
//                chart.spin(1000, chart.getRotationAngle(), chart.getRotationAngle() + 360, Easing.EaseInOutCubic);
//                break;
//            }
//            case R.id.actionSave: {
//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    saveToGallery();
//                } else {
//                    requestStoragePermission(chart);
//                }
//                break;
//            }
//        }
//        return true;
//    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        tvX.setText(String.valueOf(seekBarX.getProgress()));
        tvY.setText(String.valueOf(seekBarY.getProgress()));

        setData(seekBarX.getProgress(), seekBarY.getProgress());
    }



    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", xIndex: " + e.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}
}
