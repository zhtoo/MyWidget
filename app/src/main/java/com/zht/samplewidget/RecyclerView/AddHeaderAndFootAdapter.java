package com.zht.samplewidget.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zht.samplewidget.R;


/**
 * Created by zhanghaitao on 2017/6/14.
 */

public class AddHeaderAndFootAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //item类型
    //在这里还是可以添加其他类型的。
    public static final int ITEM_TYPE_HEADER = 0;//头布局
    public static final int ITEM_TYPE_CONTENT = 1;//条目布局
    public static final int ITEM_TYPE_BOTTOM = 2;//脚布局

    //模拟数据（后期可以通过构造函数添加，也可以交给继承HeaderBottomAdapter的子类）
    public static String[] texts = {"java", "python", "C++", "Php", ".NET", "js", "Ruby", "Swift", "OC",
            "java", "python", "C++", "Php", ".NET", "js", "Ruby", "Swift"};
    private LayoutInflater mLayoutInflater;
    public static Context mContext;
    private int mHeaderCount = 1;//头部View个数
    private int mBottomCount = 1;//底部View个数


    /**
     * 构造函数（不多解释）
     *
     * @param context
     */
    public AddHeaderAndFootAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    /**
     * 获取内容的长度
     *
     * @return
     */
    public int getContentItemCount() {
        return texts == null ? 0 : texts.length;
    }

    /**
     * 判断当前item是否是HeadView
     *
     * @param position
     * @return
     */
    public boolean isHeaderView(int position) {
        return mHeaderCount != 0 && position < mHeaderCount;
    }

    /**
     * 判断当前item是否是FooterView
     *
     * @param position
     * @return
     */
    public boolean isBottomView(int position) {
        return mBottomCount != 0 && position >= (mHeaderCount + getContentItemCount());
    }

    /**
     * 判断当前item类型
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        int dataItemCount = getContentItemCount();
        if (mHeaderCount != 0 && position < mHeaderCount) {
            //头部View
            return ITEM_TYPE_HEADER;
        } else if (mBottomCount != 0 && position >= (mHeaderCount + dataItemCount)) {
            //底部View
            return ITEM_TYPE_BOTTOM;
        } else {
            //内容View
            return ITEM_TYPE_CONTENT;
        }
    }

    /**
     * 内容 ViewHolder类
     */
    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public ContentViewHolder(View itemView) {
            super(itemView);
            //TODO:进行内容的View的初始化
            mTextView = (TextView)  itemView.findViewById(R.id.item_text_test) ;
        }

        public void setData(final int position) {
            //TODO:给内容的View设置数据
            mTextView.setText(texts[position]);
        }
    }

    /**
     * 头部 ViewHolder
     */
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
            //TODO:进行头部的View的初始化
        }
        public void setData(final int position) {
            //TODO:给头部的View设置数据

        }
    }

    /**
     * 底部 ViewHolder
     */
    public static class BottomViewHolder extends RecyclerView.ViewHolder {


        public BottomViewHolder(View itemView) {
            super(itemView);
            //TODO:进行底部的View的初始化
        }

        public void setData(final int position) {
            //TODO:给底部的View设置数据

        }
    }


    //接下来的方法是熟悉的方法，不作解释

    /**
     * 这个方法不能返回为空，需要继承父类或者返回一个视图。
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER) {
            //TODO:在这里定义头布局的样式
           // return new HeaderViewHolder(mLayoutInflater.inflate(R.layout.header, parent, false));
            TextView HeaderText = new TextView(mContext);
            HeaderText.setText("我是头布局");
            return new HeaderViewHolder(HeaderText);

        } else if (viewType == mHeaderCount) {
            return new ContentViewHolder(mLayoutInflater.inflate(R.layout.item_recycler_addtype, parent, false));
        } else if (viewType == ITEM_TYPE_BOTTOM) {
            //TODO:在这里定义底部布局的样式
            //return new BottomViewHolder(mLayoutInflater.inflate(R.layout.footer, parent, false));
            TextView BottomText = new TextView(mContext);
            BottomText.setText("我是底部布局");
            return new HeaderViewHolder(BottomText);
        }
        return new ContentViewHolder(mLayoutInflater.inflate(R.layout.item_recycler_addtype, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).setData(position);
        } else if (holder instanceof ContentViewHolder) {
            ((ContentViewHolder) holder).setData(position-mHeaderCount);
        } else if (holder instanceof BottomViewHolder) {
            ((BottomViewHolder) holder).setData(position);
        }
    }

    /**
     * 获取整个条目的数量
     * @return
     */
    @Override
    public int getItemCount() {
        return mHeaderCount + getContentItemCount() + mBottomCount;
    }


    public static onItemClickListener listener;

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public interface onItemClickListener {

        void onItemClick(int position);
    }


}