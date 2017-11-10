package com.hs.samplewidget.activity.ViewPager;

import android.view.View;

import com.hs.samplewidget.R;
import com.hs.samplewidget.myView.HstogramView;

/**
 * Created by GIGAMOLE on 8/18/16.
 */
public class Utils {

    public static void setupItem(final View view, final LibraryObject libraryObject) {
        final HstogramView hstogramView = (HstogramView) view.findViewById(R.id.my_hstogram1);

    }

    public static class LibraryObject {

        private String mTitle;
        private int mRes;

        public LibraryObject(final int res, final String title) {
            mRes = res;
            mTitle = title;
        }

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(final String title) {
            mTitle = title;
        }

        public int getRes() {
            return mRes;
        }

        public void setRes(final int res) {
            mRes = res;
        }
    }
}
