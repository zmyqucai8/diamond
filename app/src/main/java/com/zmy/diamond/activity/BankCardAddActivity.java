package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.nanchen.bankcardutil.BankInfoUtil;
import com.nanchen.bankcardutil.ContentWithSpaceEditText;
import com.zmy.diamond.R;
import com.zmy.diamond.base.MyBaseSwipeBackActivity;
import com.zmy.diamond.utli.ApiUtlis;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.JsonCallBack;
import com.zmy.diamond.utli.MessageEvent;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.PublicResponseBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 银行卡 添加页面
 * Created by zhangmengyun on 2018/6/13.
 */

public class BankCardAddActivity extends MyBaseSwipeBackActivity {

    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_card_type)
    TextView tv_card_type;

    @BindView(R.id.edit_name)
    EditText edit_name;
    @BindView(R.id.edit_bank_name)
    EditText edit_bank_name;
    @BindView(R.id.edit_card_code)
    ContentWithSpaceEditText edit_card_code;
    @BindView(R.id.edit_phone)
    EditText edit_phone;


    @Override
    public void initUI() {
        setContentView(R.layout.activity_back_card_add);
        super.initUI();
        EventBus.getDefault().register(this);
        tv_back.setTypeface(MyUtlis.getTTF());
        tv_title.setText("添加银行卡");


        edit_card_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                onCheckCard();
            }
        });

    }

    String cardNum;

    private void onCheckCard() {

        cardNum = edit_card_code.getText().toString().replace(" ", "");
        if (!TextUtils.isEmpty(cardNum) && MyUtlis.checkBankCard(cardNum)) {
            BankInfoUtil mInfoUtil = new BankInfoUtil(cardNum);

            if (!"未知".equals(mInfoUtil.getBankName())) {
                edit_bank_name.setText(mInfoUtil.getBankName());
            }
            if (!"无法识别".equals(mInfoUtil.getCardType())) {
                tv_card_type.setText(mInfoUtil.getCardType());
            }
//            mTvBankId.setText(mInfoUtil.getBankId());

        } else {
            tv_card_type.setText("");
            edit_bank_name.setText("");
        }
    }


//    UserBean user;

    @Override
    public void initData() {
//        user = DaoUtlis.getCurrentLoginUser();

//        if(null!=user &&user.getNickName())


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

    }


    public static void start(Context context) {
        Intent intent = new Intent(context, BankCardAddActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.tv_back)
    public void tv_back() {
        ActivityUtils.finishActivity(this, true);
    }


    @BindView(R.id.btn_binding)
    Button btn_binding;

    @OnClick(R.id.btn_binding)
    public void btn_binding() {
//        ToastUtils.showShort("绑定");

        KeyboardUtils.hideSoftInput(this);
        String name = edit_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            MyUtlis.showShort(this, "请输入持卡人姓名");
            return;
        }
//        String cardCode = edit_card_code.getText().toString().trim();
        if (TextUtils.isEmpty(cardNum)) {
            MyUtlis.showShort(this, "请输入银行卡号");
            return;
        } else {
            //验证银行卡号
            if (!MyUtlis.checkBankCard(cardNum)) {
                MyUtlis.showShort(this, "请输入有效的银行卡号");
                return;
            }
        }

        String bankName = edit_bank_name.getText().toString().trim();
        if (TextUtils.isEmpty(bankName)) {
            MyUtlis.showShort(this, "请输入银行名称");
            return;
        }

        String phone = edit_phone.getText().toString().trim();
        if (!RegexUtils.isMobileExact(phone)) {
            MyUtlis.showShort(this, "请输入有效的手机号码");
            return;
        }

        ApiUtlis.walletBindingBankCard(BankCardAddActivity.this,MyUtlis.getToken(), cardNum, phone, name, bankName, new JsonCallBack<PublicResponseBean>(PublicResponseBean.class) {
            @Override
            public void onSuccess(Response<PublicResponseBean> response) {

                if (null != response.body()) {

                    if (response.body().getCode() == AppConstant.CODE_SUCCESS) {
                        ToastUtils.showShort(response.body().getMsg());
                        MyUtlis.eventAddBankCardOK();
                        btn_binding.setClickable(false);
                        MyUtlis.finishActivity(BankCardAddActivity.this);
//                        ActivityUtils.finishActivity(BankCardAddActivity.this);
                    } else {
                        MyUtlis.showShortNo(BankCardAddActivity.this, response.body().getMsg());
                    }


                }
            }

            @Override
            public void onStart(Request<PublicResponseBean, ? extends Request> request) {
                super.onStart(request);
                showLoading();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoading();
            }
        });


    }

}
