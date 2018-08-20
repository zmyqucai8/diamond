package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.zmy.diamond.R;
import com.zmy.diamond.base.MyBaseSwipeBackActivity;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.DataBean;
import com.zmy.diamond.utli.view.loading_button.customViews.CircularProgressButton;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创建联系人页面
 * Created by zhangmengyun on 2018/6/25.
 */

public class CreateContactActivity extends MyBaseSwipeBackActivity {


    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.tv_ttf_name)
    TextView tv_ttf_name;
    @BindView(R.id.edit_name)
    EditText edit_name;
    @BindView(R.id.tv_ttf_phone)
    TextView tv_ttf_phone;
    @BindView(R.id.edit_phone)
    EditText edit_phone;
    @BindView(R.id.tv_ttf_tel)
    TextView tv_ttf_tel;
    @BindView(R.id.edit_tel)
    EditText edit_tel;
    @BindView(R.id.tv_ttf_address)
    TextView tv_ttf_address;
    @BindView(R.id.edit_address)
    EditText edit_address;
    @BindView(R.id.tv_ttf_note)
    TextView tv_ttf_note;
    @BindView(R.id.edit_note)
    EditText edit_note;
    @BindView(R.id.btn_save)
    CircularProgressButton btn_save;

    @Override
    public void initUI() {
        setContentView(R.layout.create_contact_activity);
        super.initUI();
        tv_back.setTypeface(MyUtlis.getTTF());
        tv_title.setText(getString(R.string.d_create_contact));
        tv_ttf_address.setTypeface(MyUtlis.getTTF());
        tv_ttf_name.setTypeface(MyUtlis.getTTF());
        tv_ttf_note.setTypeface(MyUtlis.getTTF());
        tv_ttf_phone.setTypeface(MyUtlis.getTTF());
        tv_ttf_tel.setTypeface(MyUtlis.getTTF());
    }

    public DataBean mDataBean;

    @Override
    public void initData() {
        mDataBean = (DataBean) getIntent().getSerializableExtra(AppConstant.ExtraKey.DATA_BEAN);
        if (null == mDataBean) {
            tv_back();
            return;
        }

        edit_name.setText(mDataBean.getName());
        edit_phone.setText(mDataBean.getPhone());
        edit_tel.setText(mDataBean.getTel());
        edit_address.setText(mDataBean.getAddress());
        edit_note.setText(mDataBean.getSource());
    }


    @OnClick(R.id.tv_back)
    public void tv_back() {
        ActivityUtils.finishActivity(this, true);
    }

    @OnClick(R.id.btn_save)
    public void btn_save() {
//        MyUtlis.showShortSimple(this, "保存");
        KeyboardUtils.hideSoftInput(this);
        //1.姓名不能为空
        //2.手机或座机不能为空
        //3.备注可以为空
        //4.地址可以为空

        String name = edit_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            MyUtlis.showShort(this, getString(R.string.hint_name_no_null));
            return;
        }
        String phone = edit_phone.getText().toString().trim();
        String tel = edit_tel.getText().toString().trim();
        if (TextUtils.isEmpty(phone) && TextUtils.isEmpty(tel)) {
            MyUtlis.showShort(this, getString(R.string.hint_phone_or_tel_all_null));
            return;
        }

        String address = edit_address.getText().toString().trim();
        String note = edit_note.getText().toString().trim();
        mDataBean.setName(name);
        mDataBean.setPhone(phone);
        mDataBean.setTel(tel);
        mDataBean.setAddress(address);
        mDataBean.setSource(note);
        MyUtlis.addContact(this, mDataBean, true);
    }

    public static void start(Context context, DataBean bean) {
        Intent intent = new Intent(context, CreateContactActivity.class);
        intent.putExtra(AppConstant.ExtraKey.DATA_BEAN, bean);
        ActivityUtils.startActivity(intent);
    }
}
