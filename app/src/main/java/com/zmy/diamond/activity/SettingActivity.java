package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.IntentUtils;
import com.zmy.diamond.R;
import com.zmy.diamond.base.MyBaseSwipeBackActivity;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.MyUtlis;

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


    @BindView(R.id.tv_ttf_my_file)
    TextView tv_ttf_my_file;
    @BindView(R.id.tv_ttf_my_file_r)
    TextView tv_ttf_my_file_r;

    @BindView(R.id.tv_ttf_share)
    TextView tv_ttf_share;
    @BindView(R.id.tv_ttf_share_r)
    TextView tv_ttf_share_r;
    @BindView(R.id.tv_ttf_explain_app_r)
    TextView tv_ttf_explain_app_r;
    @BindView(R.id.tv_ttf_explain_app)
    TextView tv_ttf_explain_app;
    @BindView(R.id.tv_ttf_close_app)
    TextView tv_ttf_close_app;
    @BindView(R.id.tv_ttf_close_app_r)
    TextView tv_ttf_close_app_r;

    @Override
    public void initUI() {
        setContentView(R.layout.activity_setting);
        super.initUI();
        tv_title.setText(getString(R.string.title_setting));
        tv_ttf_close_app.setTypeface(MyUtlis.getTTF());
        tv_ttf_close_app_r.setTypeface(MyUtlis.getTTF());
        tv_back.setTypeface(MyUtlis.getTTF());
        tv_ttf_my_file.setTypeface(MyUtlis.getTTF());
        tv_ttf_my_file_r.setTypeface(MyUtlis.getTTF());
        tv_ttf_chace.setTypeface(MyUtlis.getTTF());
        tv_ttf_chace_r.setTypeface(MyUtlis.getTTF());
        tv_ttf_collect_setting.setTypeface(MyUtlis.getTTF());
        tv_ttf_collect_setting_r.setTypeface(MyUtlis.getTTF());
        tv_ttf_data.setTypeface(MyUtlis.getTTF());
        tv_ttf_data_r.setTypeface(MyUtlis.getTTF());
        tv_ttf_delete_contact.setTypeface(MyUtlis.getTTF());
        tv_ttf_delete_contact_r.setTypeface(MyUtlis.getTTF());

        tv_ttf_explain_app.setTypeface(MyUtlis.getTTF());
        tv_ttf_explain_app_r.setTypeface(MyUtlis.getTTF());
        tv_ttf_share.setTypeface(MyUtlis.getTTF());
        tv_ttf_share_r.setTypeface(MyUtlis.getTTF());
    }

    @Override
    public void initData() {
        setChaceSize();
    }


    public static void start(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
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


    @OnClick(R.id.rl_my_file)
    public void rl_my_file() {
        MyFileActivity.start(this);
    }

    @OnClick(R.id.rl_explain_app)
    public void rl_explain_app() {
        ExplainAppActivity.start(this);
    }

    @OnClick(R.id.rl_close_app)
    public void rl_close_app() {
        new AlertView("提示", "确定要关闭应用吗？", null, new String[]{"确定"}, new String[]{"取消"}, SettingActivity.this,
                AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    AppUtils.exitApp();
                }
            }
        }).show();


    }

    @OnClick(R.id.rl_share)
    public void rl_share() {
        try {
            ActivityUtils.startActivity(IntentUtils.getShareTextIntent(getString(R.string.text_app_share_url)));
            MyUtlis.clickEvent(AppConstant.CLICK.umeng_share);
        } catch (Exception e) {
            e.printStackTrace();
            MyUtlis.showShort(this, getString(R.string.hint_share_error));
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
