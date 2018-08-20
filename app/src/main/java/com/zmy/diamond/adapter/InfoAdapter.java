package com.zmy.diamond.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zmy.diamond.R;
import com.zmy.diamond.activity.WebViewActivity;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.InfoDataBean;

import java.util.List;

/**
 * 信息咨询页面适配器
 * Created by zhangmengyun on 2018/6/11.
 */

public class InfoAdapter extends BaseQuickAdapter<InfoDataBean, BaseViewHolder> {
    public InfoAdapter(Context context, @Nullable List<InfoDataBean> data) {
        super(R.layout.item_info, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final InfoDataBean item) {
        helper
                .setText(R.id.tv_title, item.title)
                .setText(R.id.tv_content, item.content);
        MyUtlis.showImage(item.imageUrl, (ImageView) helper.getView(R.id.iv_image));

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebViewActivity.start(mContext, item.detailsUrl, item.getTitle());
            }
        });

    }
}
