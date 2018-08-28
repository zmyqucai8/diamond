package com.zmy.diamond.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zmy.diamond.R;
import com.zmy.diamond.activity.CreateContactActivity;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.DataBean;
import com.zmy.diamond.utli.bean.UserBean;

import java.util.List;

/**
 * 首页数据适配器
 * Created by zhangmengyun on 2018/6/12.
 */

public class HomeDataAdapter extends BaseQuickAdapter<DataBean, BaseViewHolder> {

    //手机固话显示类型
    public int mPhoneTelShowType = AppConstant.SHOW_TYPE_ALL;


    public UserBean mUser;

    public HomeDataAdapter(UserBean user, @Nullable List<DataBean> data) {
        super(R.layout.item_home_data, data);
        this.mUser = user;
    }

    @Override
    protected void convert(BaseViewHolder helper, final DataBean item) {
        helper
                .setText(R.id.tv_source, item.source)
                .setText(R.id.tv_name, item.name)
                .setText(R.id.tv_tel, MyUtlis.getPhoneByVip(item.tel, mUser.getGrade()))
                .setText(R.id.tv_phone, MyUtlis.getPhoneByVip(item.phone, mUser.getGrade()))
                .setText(R.id.tv_address, item.address)
                .setText(R.id.tv_address_details, item.address_details)
                .setText(R.id.tv_index, String.valueOf(helper.getAdapterPosition() + 1));

        helper
                .setTypeface(R.id.tv_ttf_source, MyUtlis.getTTF())
                .setTypeface(R.id.tv_ttf_name, MyUtlis.getTTF())
                .setTypeface(R.id.tv_ttf_tel, MyUtlis.getTTF())
                .setTypeface(R.id.tv_ttf_phone, MyUtlis.getTTF())
                .setTypeface(R.id.tv_ttf_address, MyUtlis.getTTF())
                .setTypeface(R.id.tv_ttf_address_details, MyUtlis.getTTF());


        //根据电话显示类型，设置显示
        helper.setGone(R.id.ll_tel, mPhoneTelShowType == AppConstant.SHOW_TYPE_TEL || mPhoneTelShowType == AppConstant.SHOW_TYPE_ALL);
        helper.setGone(R.id.ll_phone, mPhoneTelShowType == AppConstant.SHOW_TYPE_PHONE || mPhoneTelShowType == AppConstant.SHOW_TYPE_ALL);


        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存到通讯录
                CreateContactActivity.start(mContext, item);
            }
        });


    }


    /**
     * 设置手机固话显示类型，
     * 0=全部 默认
     * 1=手机
     * 2=固话
     *
     * @param showType
     */
    public void setPhoneTelShowType(int showType) {
        this.mPhoneTelShowType = showType;
    }
}
