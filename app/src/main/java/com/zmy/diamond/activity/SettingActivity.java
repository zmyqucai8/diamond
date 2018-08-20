package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.zmy.diamond.R;
import com.zmy.diamond.base.MyBaseSwipeBackActivity;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.UserBean;
import com.zmy.diamond.utli.dao.DaoUtlis;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置页面
 * Created by zhangmengyun on 2018/6/16.
 */

public class SettingActivity extends MyBaseSwipeBackActivity {
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_ttf_chace)
    TextView tv_ttf_chace;
    @BindView(R.id.tv_chace)
    TextView tv_chace;
    @BindView(R.id.tv_ttf_chace_r)
    TextView tv_ttf_chace_r;
    @BindView(R.id.tv_ttf_data)
    TextView tv_ttf_data;
    @BindView(R.id.tv_ttf_data_r)
    TextView tv_ttf_data_r;
    @BindView(R.id.tv_ttf_delete_contact)
    TextView tv_ttf_delete_contact;
    @BindView(R.id.tv_ttf_delete_contact_r)
    TextView tv_ttf_delete_contact_r;
    @BindView(R.id.tv_ttf_collect_setting)
    TextView tv_ttf_collect_setting;
    @BindView(R.id.tv_ttf_collect_setting_r)
    TextView tv_ttf_collect_setting_r;

    @BindView(R.id.tv_ttf_macAddress)
    TextView tv_ttf_macAddress;
    @BindView(R.id.tv_macAddress)
    TextView tv_macAddress;


    @Override
    public void initUI() {
        setContentView(R.layout.activity_setting);
        super.initUI();
        tv_title.setText(getString(R.string.title_setting));
        tv_back.setTypeface(MyUtlis.getTTF());
        tv_ttf_chace.setTypeface(MyUtlis.getTTF());
        tv_ttf_macAddress.setTypeface(MyUtlis.getTTF());
        tv_ttf_chace_r.setTypeface(MyUtlis.getTTF());
        tv_ttf_collect_setting.setTypeface(MyUtlis.getTTF());
        tv_ttf_collect_setting_r.setTypeface(MyUtlis.getTTF());
        tv_ttf_data.setTypeface(MyUtlis.getTTF());
        tv_ttf_data_r.setTypeface(MyUtlis.getTTF());
        tv_ttf_delete_contact.setTypeface(MyUtlis.getTTF());
        tv_ttf_delete_contact_r.setTypeface(MyUtlis.getTTF());
    }

    @Override
    public void initData() {


        setChaceSize();
        setMacAddress();
    }


    /**
     * 设置mac地址
     */
    private void setMacAddress() {
        UserBean user = DaoUtlis.getCurrentLoginUser();
        if (null != user) {
            tv_macAddress.setText(user.getMacAddress());
        } else {
            tv_macAddress.setText("no");
        }
    }


    public static void start(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.btn_login_out)
    public void btn_login_out() {
        MyUtlis.userLoginOut(this, true);
    }

    @OnClick(R.id.rl_data)
    public void rl_data() {
        MyUtlis.deleteData(this);
    }

    @OnClick(R.id.rl_chace)
    public void rl_chace() {
        MyUtlis.deleteChace(this);
        setChaceSize();
    }

    @OnClick(R.id.rl_collect_setting)
    public void rl_collect_setting() {
        MyUtlis.clickEvent(AppConstant.CLICK.umeng_setting_collect_setting);
        CollectSettingActivity.start(this);
    }

    @OnClick(R.id.rl_delete_contact)
    public void rl_delete_contact() {
        MyUtlis.deleteContact(this);
    }

    private void setChaceSize() {
        tv_chace.setText(MyUtlis.getChaceSize());
    }

    @OnClick(R.id.tv_back)
    public void tv_back() {
        ActivityUtils.finishActivity(this, true);
    }


    @OnClick(R.id.rl_macAddress)
    public void rl_macAddress() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
