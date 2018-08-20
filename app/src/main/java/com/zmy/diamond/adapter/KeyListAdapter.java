package com.zmy.diamond.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zaaach.citypicker.model.City;
import com.zmy.diamond.R;
import com.zmy.diamond.fragment.KeyPickerDialogFragment;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.KeyListBean;
import com.zmy.diamond.utli.view.GridDividerItemDecoration;

import java.util.List;

/**
 * 关键字页面数据适配器
 * Created by zhangmengyun on 2018/6/12.
 */

public class KeyListAdapter extends BaseQuickAdapter<KeyListBean, BaseViewHolder> {

    public KeyPickerDialogFragment fragment;

    public KeyListAdapter(KeyPickerDialogFragment fragment, @Nullable List<KeyListBean> data) {
        super(R.layout.item_key, data);
        this.mContext = fragment.getActivity();
        this.fragment = fragment;
    }

    @Override
    protected void convert(BaseViewHolder helper, KeyListBean item) {
        helper.setText(R.id.tv_title, item.title_name)
                .setTextColor(R.id.tv_title, ContextCompat.getColor(mContext, item.title_color))
                .setTextColor(R.id.tv_ttf_title, ContextCompat.getColor(mContext, item.title_color))
                .setText(R.id.tv_ttf_title, item.title_ttf)
                .setTypeface(R.id.tv_ttf_title, MyUtlis.getTTF())
                .setBackgroundColor(R.id.rl_item, ContextCompat.getColor(mContext, item.title_color));

        RecyclerView recyclerView = helper.getView(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        recyclerView.setLayoutManager(layoutManager);

        GridDividerItemDecoration space = new GridDividerItemDecoration(1, ContextCompat.getColor(mContext, R.color.item_divider));
        recyclerView.addItemDecoration(space);
        KeyItemAdapter keyItemAdapter = new KeyItemAdapter(fragment, item.listData);

        keyItemAdapter.openLoadAnimation(SCALEIN);
        recyclerView.setAdapter(keyItemAdapter);

    }

    public class KeyItemAdapter extends BaseQuickAdapter<KeyListBean.KeyItemBean, BaseViewHolder> {
        public KeyPickerDialogFragment fragment;

        public KeyItemAdapter(KeyPickerDialogFragment fragment, @Nullable List<KeyListBean.KeyItemBean> data) {
            super(R.layout.item_key_name, data);
            this.fragment = fragment;
        }

        @Override
        protected void convert(final BaseViewHolder helper, final KeyListBean.KeyItemBean item) {
            if (!TextUtils.isEmpty(item.name_ttf)) {
//                helper.setText(R.id.tv_ttf_name, item.name_ttf)
//                        .setTypeface(R.id.tv_ttf_name, MyUtlis.getTTF())
//                        .setTextColor(R.id.tv_ttf_name, item.name_ttf_color)
//                        .setVisible(R.id.tv_ttf_name, true);
            } else {
//                helper.setVisible(R.id.tv_ttf_name, false);
            }

            helper.setText(R.id.tv_name, item.name);


            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment.dismiss(helper.getAdapterPosition(), new City(item.name, "", "", ""));
                }
            });

        }
    }
}
