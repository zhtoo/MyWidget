package com.zht.samplewidget.myView.chart;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by ZhangHaitao on 2019/11/19
 */
public class LineChartParams {

    private XAxisBean xAxis;
    private YAxisBean yAxis;
    private List<SeriesBean> series;

    public XAxisBean getxAxis() {
        return xAxis;
    }

    public void setxAxis(XAxisBean xAxis) {
        this.xAxis = xAxis;
    }

    public YAxisBean getyAxis() {
        return yAxis;
    }

    public void setyAxis(YAxisBean yAxis) {
        this.yAxis = yAxis;
    }

    public List<SeriesBean> getSeries() {
        return series;
    }

    public void setSeries(List<SeriesBean> series) {
        this.series = series;
    }

    public static class XAxisBean {

        private  int drawColor;
        private String unit;
        private List<String> data;

        public int getDrawColor() {
            return drawColor;
        }

        public void setDrawColor(int drawColor) {
            this.drawColor = drawColor;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public List<String> getData() {
            return data;
        }

        public void setData(List<String> data) {
            this.data = data;
        }
    }

    public static class YAxisBean {
        private  int drawColor;
        private String unit;
        private List<Integer> data;

        public int getDrawColor() {
            return drawColor;
        }

        public void setDrawColor(int drawColor) {
            this.drawColor = drawColor;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public List<Integer> getData() {
            return data;
        }

        public void setData(List<Integer> data) {
            this.data = data;
        }
    }

    public static class SeriesBean {
        private String name;
        private  int drawColor;
        private List<Integer> data;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getDrawColor() {
            return drawColor;
        }

        public void setDrawColor(int drawColor) {
            this.drawColor = drawColor;
        }

        public List<Integer> getData() {
            return data;
        }

        public void setData(List<Integer> data) {
            this.data = data;
        }
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
