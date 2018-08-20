package com.zmy.diamond.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zmy.diamond.R;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.BankCardBean;

import java.util.List;

/**
 * Created by zhangmengyun on 2018/8/7.
 */

public class SelectBankCardAdapter extends BaseQuickAdapter<BankCardBean.DataBean, BaseViewHolder> {
    public SelectBankCardAdapter(@Nullable List<BankCardBean.DataBean> data) {
        super(R.layout.item_select_bank_card, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BankCardBean.DataBean item) {


        String bankCardAfter4 = MyUtlis.getBankCardAfter4(item.getCard());
        StringBuilder sb = new StringBuilder();
        sb.append(item.getBank_name());
        sb.append(" 储蓄卡 （");
        sb.append(bankCardAfter4);
        sb.append("）");
        helper.setText(R.id.tv_title, sb.toString());
    }
}
