package com.zmy.diamond.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zmy.diamond.R;
import com.zmy.diamond.activity.MarketingDataActivity;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.CollectRecordBean;

import java.util.List;

/**
 * 营销页面数据adapter
 * Created by zhangmengyun on 2018/7/31.
 */

public class MarketingAdapter extends BaseQuickAdapter<CollectRecordBean, BaseViewHolder> {
    public MarketingAdapter(@Nullable List<CollectRecordBean> data) {
        super(R.layout.item_markeeting, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final CollectRecordBean item) {
        helper
                .setText(R.id.tv_key, item.getKey())
                .setText(R.id.tv_city, item.getCity())
                .setText(R.id.tv_count, mContext.getString(R.string.text_value_count, item.getCount()))
                .setTypeface(R.id.tv_ttf_r, MyUtlis.getTTF());


        helper
                .setTypeface(R.id.tv_ttf_key, MyUtlis.getTTF())
                .setTypeface(R.id.tv_ttf_city, MyUtlis.getTTF());

//        helper.getView(R.id.tv_more).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showMore();
//
//
//            }
//        });

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarketingDataActivity.start(mContext, item.getCollectId(), item.getPlatformId(), item.getUserId(), item.getKey(), item.getCity());
            }
        });
    }

    public AlertView moreView;
    //是否显示了more菜单,默认false
    public boolean isShowMore;

    private void showMore() {
        isShowMore = true;
        if (null == moreView) {
            moreView = new AlertView(mContext.getString(R.string.hint_select), null, "取消", new String[]{}, new String[]{"导出数据", "群发短信", "删除数据"}, mContext,
                    AlertView.Style.ActionSheet, new com.bigkoo.alertview.OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    //0=发布需求 1=发布账户 -1=取消

                    if (0 == position) {
                        ToastUtils.showShort("导出数据");
                    } else if (1 == position) {
                        ToastUtils.showShort("群发短信");
                    } else {
                        ToastUtils.showShort("删除数据");
                    }
                    moreView.dismiss();
                }
            }).setCancelable(true).setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(Object o) {
                    isShowMore = false;
                }
            });
        }
        moreView.show();
    }
}
