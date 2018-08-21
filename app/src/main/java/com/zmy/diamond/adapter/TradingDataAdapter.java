package com.zmy.diamond.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zmy.diamond.R;
import com.zmy.diamond.activity.AccountActionActivity;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.TradingDataBean;

import java.util.List;

/**
 * 交易大厅 数据adapter
 * Created by zhangmengyun on 2018/7/28.
 */

public class TradingDataAdapter extends BaseQuickAdapter<TradingDataBean.DataBean, BaseViewHolder> {

    /**
     * 是否显示数据类型（买账号|卖账号）
     */
    public boolean isShowDataType;
    String loginUserId = MyUtlis.getLoginUserId();


    public TradingDataAdapter(@Nullable List<TradingDataBean.DataBean> data, boolean isShowDataType) {
        super(R.layout.item_trading_data, data);
        this.isShowDataType = isShowDataType;
    }

    public TradingDataAdapter(@Nullable List<TradingDataBean.DataBean> data) {
        super(R.layout.item_trading_data, data);
        this.isShowDataType = false;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final TradingDataBean.DataBean item) {


        helper
                .setTypeface(R.id.tv_ttf_title_industry, MyUtlis.getTTF())
                .setTypeface(R.id.tv_ttf_title_city, MyUtlis.getTTF())
                .setTypeface(R.id.tv_ttf_title_describe, MyUtlis.getTTF())
                .setTypeface(R.id.tv_ttf_title_friendCount, MyUtlis.getTTF())
                .setTypeface(R.id.tv_ttf_title_price, MyUtlis.getTTF())
                .setTypeface(R.id.tv_ttf_title_weixin, MyUtlis.getTTF())
                .setText(R.id.tv_industry, item.getIndustry())
                .setText(R.id.tv_city, item.getCity())
                .setText(R.id.tv_friendCount, String.valueOf(item.getFriend_number()))
                .setText(R.id.tv_price, String.valueOf(item.getPrice()))
                .setText(R.id.tv_describe, MyUtlis.isEmpty(item.getTrade_describe()))
                .setText(R.id.tv_weixin, item.getWeixin())

                .itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showShort(helper.getAdapterPosition() + "");

                //自己的 可以点击编辑
                if (loginUserId.equals(item.getUser_id())) {
                    AccountActionActivity.startModify(mContext, item);
                }


            }

        });
        if (isShowDataType) {
            helper.setText(R.id.tv_data_type, item.getType() == AppConstant.DATA_TYPE_BUY_ACCOUNT ? "买账号" : "卖账号")
                    .setGone(R.id.tv_data_type, true);
        } else {
            helper.setGone(R.id.tv_data_type, false);
        }
    }
}
