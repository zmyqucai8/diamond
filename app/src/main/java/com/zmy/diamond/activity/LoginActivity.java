package com.zmy.diamond.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.zmy.diamond.MainActivity;
import com.zmy.diamond.R;
import com.zmy.diamond.base.MyBaseActivity;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.view.loading_button.customViews.CircularProgressButton;
import com.zmy.diamond.utli.view.loading_button.interfaces.OnAnimationEndListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录页面
 * Created by zhangmengyun on 2018/6/11.
 */

public class LoginActivity extends MyBaseActivity {

    @BindView(R.id.tv_ttf_phone)
    TextView tv_ttf_phone;
    @BindView(R.id.tv_ttf_pwd)
    TextView tv_ttf_pwd;
    @BindView(R.id.edit_pwd)
    TextView edit_pwd;
    @BindView(R.id.edit_phone)
    TextView edit_phone;
    @BindView(R.id.btn_login)
    CircularProgressButton btn_login;
    @BindView(R.id.tv_ttf_qq)
    TextView tv_ttf_qq;
    @BindView(R.id.tv_ttf_wechat)
    TextView tv_ttf_wechat;

    @Override
    public void initUI() {
        setContentView(R.layout.activity_login);
        super.initUI();
        tv_ttf_phone.setTypeface(MyUtlis.getTTF());
        tv_ttf_pwd.setTypeface(MyUtlis.getTTF());
        tv_ttf_qq.setTypeface(MyUtlis.getTTF());
        tv_ttf_wechat.setTypeface(MyUtlis.getTTF());


        if (AppConstant.DEBUG)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    edit_phone.setText("13000000004");
                    edit_pwd.setText("111111");
                }
            }, 100);
    }

    @Override
    public void initData() {

    }

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.tv_resetPwd)
    public void tv_resetPwd() {
        EditPwdActivity.start(this);
    }

    @OnClick(R.id.tv_regist)
    public void tv_regist() {
        RegistActivity.start(this);

    }

    @OnClick(R.id.tv_ttf_qq)
    public void tv_ttf_qq() {
        MyUtlis.showShort(this, "QQ登录");
    }

    @OnClick(R.id.tv_ttf_wechat)
    public void tv_ttf_wechat() {
        MyUtlis.showShort(this, "微信登录");
    }

    @OnClick(R.id.btn_login)
    public void btn_login() {
        //1.验证手机号
        final String phone = edit_phone.getText().toString().trim();
        if (!RegexUtils.isMobileExact(phone)) {
            MyUtlis.showShort(this, getString(R.string.hint_input_phone));
            return;
        }
        //2.验证密码 最低6位,内容随意
        String pwd = edit_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            MyUtlis.showShort(this, getString(R.string.hint_input_pwd));

            return;
        } else if (pwd.length() < 6) {
            MyUtlis.showShort(this, getString(R.string.hint_pwd_6));
            return;
        }
        KeyboardUtils.hideSoftInput(this);
        //3.去登录
        final int state = MyUtlis.userLogin(phone, pwd);

        MyUtlis.showLoadingBtn(btn_login, state, new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                switch (state) {
                    case MyUtlis.STATE_LOGIN_YES:
//                        ToastUtils.showShort("登录成功");
                        SPUtils.getInstance().put(AppConstant.SPKey.LAST_LOGIN_USER, phone);
                        MainActivity.start(LoginActivity.this);
                        ActivityUtils.finishActivity(LoginActivity.this, true);
                        break;
                    case MyUtlis.STATE_LOGIN_NO:
                        MyUtlis.showShort(LoginActivity.this, getString(R.string.hint_pwd_no));
                        break;
                    case MyUtlis.STATE_LOGIN_NO_USER:

                        MyUtlis.showShort(LoginActivity.this, getString(R.string.hint_user_no));
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        btn_login.dispose();
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        ActivityUtils.startHomeActivity();
    }
}
