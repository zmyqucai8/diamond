package com.zmy.diamond.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zmy.diamond.R;
import com.zmy.diamond.activity.WebViewActivity;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.InfoMenuBean;

import java.util.List;

/**
 * Created by zhangmengyun on 2018/7/4.
 */

public class InfoMenuAdapter extends BaseQuickAdapter<InfoMenuBean, BaseViewHolder> {
    public InfoMenuAdapter(@Nullable List<InfoMenuBean> data) {
        super(R.layout.item_info_menu, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final InfoMenuBean item) {

        helper.setText(R.id.tv_text, item.title);

//        MyUtlis.showImage(item.imageUrl, (ImageView) helper.getView(R.id.iv_image));

        Glide.with(mContext).load(item.imageUrl)
                .apply(MyUtlis.getGlideRequestOptions()).into((ImageView) helper.getView(R.id.iv_image));

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.actionType == AppConstant.CLICK_ACTION_TYPE_OPEN_WEB_URL) {
                    WebViewActivity.start(mContext, item.detailsUrl, item.getTitle());
                } else if (item.actionType == AppConstant.CLICK_ACTION_TYPE_OPEN_LIST_DATA) {
                    MyUtlis.showShort(mContext, "open data list page");
                }
            }
        });
    }
}
