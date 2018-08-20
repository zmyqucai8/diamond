package com.zmy.diamond.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zmy.diamond.R;
import com.zmy.diamond.utli.bean.WalletDetailsBean;

import java.util.List;
import java.util.Random;

/**
 * Created by zhangmengyun on 2018/8/6.
 */

public class WalletDetailsAdapter extends BaseQuickAdapter<WalletDetailsBean.DataBean, BaseViewHolder> {
    Random random;

    public WalletDetailsAdapter(@Nullable List<WalletDetailsBean.DataBean> data) {
        super(R.layout.item_wallet_details, data);

        random = new Random();
    }

    @Override
    protected void convert(BaseViewHolder helper, WalletDetailsBean.DataBean item) {


        String cash_amount= "";
//        其中status=ture 表示支出（提现）
//        false 表示收入（推荐费用）
        if(item.getStatus()){

            cash_amount="- "+item.getCash_amount();
        }else {
            cash_amount="+ "+item.getCash_amount();
        }

        helper
                .setText(R.id.tv_title, item.getMessage())
                .setText(R.id.tv_time, TimeUtils.millis2String(item.getTime()))
                .setText(R.id.tv_change, cash_amount)
                .itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
