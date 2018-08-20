package com.zmy.diamond.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zmy.diamond.R;
import com.zmy.diamond.utli.bean.BankCardBean;

import java.util.List;

/**
 * 管理银行卡列表适配器
 * Created by zhangmengyun on 2018/8/7.
 */

public class BankCardAdapter extends BaseQuickAdapter<BankCardBean.DataBean, BaseViewHolder> {
    public BankCardAdapter(@Nullable List<BankCardBean.DataBean> data) {
        super(R.layout.item_bank_card, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final BankCardBean.DataBean item) {


        //精准粉丝，假一赔十

        helper
                .setText(R.id.tv_bank_card_name, mContext.getString(R.string.text_bank_card_name,item.getBank_name()))
                .setText(R.id.tv_bank_card_type, mContext.getString(R.string.text_bank_card_type,"储蓄卡"))
                .setText(R.id.tv_bank_card_code, mContext.getString(R.string.text_bank_card_code,item.getCard()));

        //蓝色 红色

//        helper.setBackgroundColor(R.id.rootview, helper.getAdapterPosition() % 3 == 0 ? Color.RED : Color.BLUE);

        //银行icon ， 银行背景颜色， 先不处理
        helper.getView(R.id.tv_ttf_bank_card_icon).setVisibility(View.INVISIBLE);
    }
}
