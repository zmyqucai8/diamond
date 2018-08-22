package com.zmy.diamond.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zmy.diamond.R;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.VipBean;

import java.util.List;

/**
 * VIP页面数据适配器
 * Created by zhangmengyun on 2018/6/13.
 */

public class VipAdapter extends BaseQuickAdapter<VipBean, BaseViewHolder> {


    public VipAdapter(@Nullable List<VipBean> data) {
        super(R.layout.item_vip, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final VipBean item) {


        helper
                .setText(R.id.tv_name, item.name)
                .setText(R.id.tv_price_des, item.price_des)
                .setTypeface(R.id.tv_ttf_vip_r, MyUtlis.getTTF());


//        CheckBox checkBox = helper.getView(R.id.checkbox);
//        checkBox.setChecked(item.isCheck);
//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
//                refreshCheck(helper.getAdapterPosition(), isCheck);
//            }
//        });

    }

    /**
     * 刷新选中
     *
     * @param position
     */
//    public void refreshCheck(int position) {
//        boolean isRefresh = true;
//        for (int i = 0; i < getData().size(); i++) {
//            if (i == position) {
//                if (getData().get(i).isCheck) {
//                    isRefresh = false;
//                    return;
//                }
//                getData().get(i).isCheck = !getData().get(i).isCheck;
//            } else {
//                getData().get(i).isCheck = false;
//            }
//        }
//        if (isRefresh)
//            notifyDataSetChanged();
//
//    }
}
