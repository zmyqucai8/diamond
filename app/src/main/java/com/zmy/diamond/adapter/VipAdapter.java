package com.zmy.diamond.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zmy.diamond.R;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.VipPriceJsonBean;

import java.util.List;

/**
 * VIP页面数据适配器
 * Created by zhangmengyun on 2018/6/13.
 */

public class VipAdapter extends BaseQuickAdapter<VipPriceJsonBean.DataBean, BaseViewHolder> {

    public VipAdapter(@Nullable List<VipPriceJsonBean.DataBean> data) {
        super(R.layout.item_vip, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final VipPriceJsonBean.DataBean item) {
        helper
                .setText(R.id.tv_name, item.getMessage())
                .setText(R.id.tv_price_des, item.getPrice())
                .setTypeface(R.id.tv_ttf_vip_r, MyUtlis.getTTF());
    }
}
