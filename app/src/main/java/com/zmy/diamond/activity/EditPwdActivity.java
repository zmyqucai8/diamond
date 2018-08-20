package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.zmy.diamond.R;
import com.zmy.diamond.base.MyBaseSwipeBackActivity;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.dao.DaoUtlis;
import com.zmy.diamond.utli.view.loading_button.customViews.CircularProgressButton;
import com.zmy.diamond.utli.view.loading_button.interfaces.OnAnimationEndListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *   忘记密码.修改密码
 * Created by zhangmengyun on 2018/6/11.
 */

public class EditPwdActivity extends MyBaseSwipeBackActivity {
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
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.btn_ok)
    CircularProgressButton btn_ok;

    @Override
    public void initUI() {
        setContentView(R.layout.activity_edit_pwd);
        super.initUI();
        tv_ttf_phone.setTypeface(MyUtlis.getTTF());
        tv_ttf_pwd.setTypeface(MyUtlis.getTTF());
        tv_ttf_code.setTypeface(MyUtlis.getTTF());
        tv_back.setTypeface(MyUtlis.getTTF());
        tv_title.setText(getString(R.string.title_edi_pwd));
    }

    @Override
    public void initData() {

    }


    @OnClick(R.id.btn_ok)
    public void btn_ok() {
//        MyUtlis.showShort("确认");
        //1.验证手机号
        String phone = edit_phone.getText().toString().trim();
        if (!RegexUtils.isMobileExact(phone)) {
            MyUtlis.showShort(this, getString(R.string.hint_phone_edit_no));
            return;
        }

        //2.验证密码 最低6位,内容随意
        String newPwd = edit_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(newPwd)) {
            MyUtlis.showShort(this, getString(R.string.hint_input_new_pwd));
            return;
        } else if (newPwd.length() < 6) {
            MyUtlis.showShort(this, getString(R.string.hint_new_pwd_6));
            return;
        }
        KeyboardUtils.hideSoftInput(this);
        final boolean b = DaoUtlis.updateUserPwd(phone, newPwd);
        MyUtlis.showLoadingBtn(btn_ok, b ? MyUtlis.STATE_YES : MyUtlis.STATE_ERROR, new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                if (b) {
                    MyUtlis.showShort(EditPwdActivity.this, getString(R.string.hint_pwd_edit_ok));
                    ActivityUtils.finishActivity(EditPwdActivity.this, true);
                } else {
                    MyUtlis.showShort(EditPwdActivity.this, getString(R.string.hint_user_no));
                }
            }
        });
    }

    @OnClick(R.id.tv_send_code)
    public void tv_send_code() {
//        MyUtlis.showShort(this, getString(R.string.text_send_code));
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, EditPwdActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.tv_back)
    public void tv_back() {
        ActivityUtils.finishActivity(this, true);
    }
}
