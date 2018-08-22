package com.zmy.diamond.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zmy.diamond.R;
import com.zmy.diamond.utli.MyUtlis;

import java.io.File;
import java.util.List;

/**
 * Created by zhangmengyun on 2018/8/22.
 */

public class MyFileAdapter extends BaseQuickAdapter<File, BaseViewHolder> {
    public MyFileAdapter(Context context, @Nullable List<File> data) {
        super(R.layout.item_file, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final File item) {
        helper
                .setTypeface(R.id.ttf_image, MyUtlis.getTTF())
                .setText(R.id.tv_name, FileUtils.getFileName(item))
                .setText(R.id.tv_time, TimeUtils.millis2String(item.lastModified()))
                .setText(R.id.tv_size, MyUtlis.fromatFileSize(item.length()))
                .setText(R.id.tv_path, item.getAbsolutePath())
                .itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开|分享
                MyUtlis.openMyFile(mContext, item);
            }
        });
    }
}
