package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.zmy.diamond.R;
import com.zmy.diamond.base.MyBaseSwipeBackActivity;
import com.zmy.diamond.utli.MyUtlis;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关于APP
 * Created by zhangmengyun on 2018/6/13.
 */

public class ExplainAppActivity extends MyBaseSwipeBackActivity {

    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_version_name)
    TextView tv_version_name;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.tv_copyright)
    TextView tv_copyright;

    String content = "　本软件旨在为您提供便捷高效的辅助工具，提升工作效率。请合理、合法的使用本软件。请勿用于违反法律，道德及影响他人利益的活动。如果因用于非法用途，由此造成的不良后果，由用户自行负责，本软件开发商不承担任何责任及损失。";

    @Override
    public void initUI() {
        setContentView(R.layout.activity_explain_app);
        super.initUI();
        tv_back.setTypeface(MyUtlis.getTTF());
        tv_title.setText(getString(R.string.title_explain_app));
        tv_content.setText(content);

    }

    @Override
    public void initData() {
        tv_version_name.setText(getString(R.string.text_app_version, AppUtils.getAppVersionName()));
        tv_copyright.setText(getString(R.string.text_copyright, getString(R.string.app_name)));
    }

    @OnClick(R.id.tv_agreement)
    public void tv_agreement() {
        AgreementActivity.start(this, true);
    }

    @OnClick(R.id.tv_back)
    public void tv_back() {
        ActivityUtils.finishActivity(this, true);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, ExplainAppActivity.class);
        context.startActivity(intent);
    }

}
