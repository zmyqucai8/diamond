package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.zmy.diamond.MainActivity;
import com.zmy.diamond.R;
import com.zmy.diamond.base.MyBaseSwipeBackActivity;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.UserBean;
import com.zmy.diamond.utli.dao.DaoUtlis;
import com.zmy.diamond.utli.view.loading_button.customViews.CircularProgressButton;
import com.zmy.diamond.utli.view.loading_button.interfaces.OnAnimationEndListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注册页面
 * Created by zhangmengyun on 2018/6/11.
 */

public class RegistActivity extends MyBaseSwipeBackActivity {

    @BindView(R.id.tv_ttf_phone)
    TextView tv_ttf_phone;
    @BindView(R.id.edit_phone)
    EditText edit_phone;
    @BindView(R.id.tv_ttf_pwd)
    TextView tv_ttf_pwd;
    @BindView(R.id.edit_pwd)
    EditText edit_pwd;
    @BindView(R.id.tv_ttf_code)
    TextView tv_ttf_code;
    @BindView(R.id.edit_code)
    EditText edit_code;
    @BindView(R.id.tv_send_code)
    TextView tv_send_code;
    @BindView(R.id.edit_recommended_phone)
    EditText edit_recommended_phone;
    @BindView(R.id.tv_ttf_recommended_phone)
    TextView tv_ttf_recommended_phone;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_regist_agreement)
    TextView tv_regist_agreement;
    @BindView(R.id.btn_regist)
    CircularProgressButton btn_regist;

    @Override
    public void initUI() {
        setContentView(R.layout.activity_regist);
        super.initUI();
        tv_ttf_phone.setTypeface(MyUtlis.getTTF());
        tv_ttf_pwd.setTypeface(MyUtlis.getTTF());
        tv_ttf_code.setTypeface(MyUtlis.getTTF());
        tv_ttf_recommended_phone.setTypeface(MyUtlis.getTTF());
        tv_back.setTypeface(MyUtlis.getTTF());
        tv_title.setText(getString(R.string.title_regist));
    }

    @Override
    public void initData() {
        SpannableStringBuilder sp = new SpanUtils().append(getString(R.string.text_regist_agreed)).setForegroundColor(ContextCompat.getColor(this, R.color.color_text)).append(getString(R.string.text_agreement)).setForegroundColor(ContextCompat.getColor(this, R.color.color_logo)).create();
        tv_regist_agreement.setText(sp);
    }


    @OnClick(R.id.tv_regist_agreement)
    public void tv_regist_agreement() {
//       MyUtlis.showShort("金刚钻协议");
        AgreementActivity.start(this, false);
    }

    @OnClick(R.id.btn_regist)
    public void btn_regist() {
//       MyUtlis.showShort("注册");

        //1.验证手机号
        final String phone = edit_phone.getText().toString().trim();
        if (!RegexUtils.isMobileExact(phone)) {
            MyUtlis.showShort(RegistActivity.this, getString(R.string.hint_input_phone));
            return;
        }
        //2.验证密码 最低6位,内容随意
        String pwd = edit_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            MyUtlis.showShort(RegistActivity.this, getString(R.string.hint_input_pwd));
            return;
        } else if (pwd.length() < 6) {
            MyUtlis.showShort(RegistActivity.this, getString(R.string.hint_pwd_6));
            return;
        }
        //3.如果有填写,验证推荐人手机号
        String recommendedPhone = edit_recommended_phone.getText().toString().trim();
        if (!TextUtils.isEmpty(recommendedPhone)) {
            if (!RegexUtils.isMobileExact(recommendedPhone)) {
                MyUtlis.showShort(RegistActivity.this, getString(R.string.hint_input_recommended_phone));
                return;
            }
        }

        //4.调用注册接口(接口中验证 验证码)
        UserBean userBean = new UserBean();
        userBean.setPhone(phone);
//        userBean.setPwd(pwd);
        userBean.setNickName(getString(R.string.text_user) + phone);
        userBean.setUserId(phone);
        KeyboardUtils.hideSoftInput(this);
        final int state = DaoUtlis.registUser(userBean);
        MyUtlis.showLoadingBtn(btn_regist, state, new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                switch (state) {
                    case MyUtlis.STATE_REGIST_NO:
                        MyUtlis.showShort(RegistActivity.this, getString(R.string.hint_regist_no));
                        break;
                    case MyUtlis.STATE_REGIST_YES:
                        //自动登录
                        MyUtlis.showShort(RegistActivity.this, getString(R.string.hint_regist_ok));

                        SPUtils.getInstance().put(AppConstant.SPKey.LAST_LOGIN_USER, phone);
                        MainActivity.start(RegistActivity.this);
                        ActivityUtils.finishActivity(RegistActivity.this, true);
                        break;
                    case MyUtlis.STATE_REGIST_USER_REPEAT:
                        MyUtlis.showShort(RegistActivity.this, getString(R.string.hint_phone_old_regist));
                        break;
                }


            }
        });
    }

    @OnClick(R.id.tv_send_code)
    public void tv_send_code() {
//        MyUtlis.showShort(RegistActivity.this, getString(R.string.text_send_code));
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, RegistActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.tv_back)
    public void tv_back() {
        ActivityUtils.finishActivity(this, true);
    }
}
