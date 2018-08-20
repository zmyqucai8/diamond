package com.zmy.diamond.utli.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by zhangmengyun on 2018/6/19.
 */

public class MyRecyclerView extends RecyclerView {
    public MyRecyclerView(Context context) {
        super(context);

        initView();
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (null == callback)
                    return;
                callback.onStateChanged(MyRecyclerView.this, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (null == callback)
                    return;
                if (dy > 0) {
                    callback.onScrollDown(MyRecyclerView.this, dy);
                } else {
                    callback.onScrollUp(MyRecyclerView.this, dy);
                }
            }
        });


    }

    public OnScrollCallback callback;

    public interface OnScrollCallback {


        /**
         * //正在滚动
         * public static final int SCROLL_STATE_IDLE = 0;
         * <p>
         * //正在被外部拖拽,一般为用户正在用手指滚动
         * public static final int SCROLL_STATE_DRAGGING = 1;
         * <p>
         * //自动滚动开始
         * public static final int SCROLL_STATE_SETTLING = 2;
         *
         * @param recycler
         * @param state
         */
        void onStateChanged(MyRecyclerView recycler, int state);

        void onScrollUp(MyRecyclerView recycler, int dy);

        void onScrollDown(MyRecyclerView recycler, int dy);
    }

    public void setOnScrollCallback(final OnScrollCallback callback) {
        this.callback = callback;
    }


}
