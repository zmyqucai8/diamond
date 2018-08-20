package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.zmy.diamond.R;
import com.zmy.diamond.base.MyBaseSwipeBackActivity;
import com.zmy.diamond.utli.ApiUtlis;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.JsonCallBack;
import com.zmy.diamond.utli.MessageEvent;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.BankCardBean;
import com.zmy.diamond.utli.bean.LoginResponseBean;
import com.zmy.diamond.utli.bean.UserBean;
import com.zmy.diamond.utli.dao.DaoUtlis;
import com.zmy.diamond.utli.view.SelectBankCardPop;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 钱包提现页面
 * Created by zhangmengyun on 2018/6/13.
 */

public class WalletTiXianActivity extends MyBaseSwipeBackActivity {

    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_bank_name)
    TextView tv_bank_name;
    @BindView(R.id.tv_ttf_r)
    TextView tv_ttf_r;
    @BindView(R.id.tv_money)
    TextView tv_money;

    @BindView(R.id.view_bg)
    public View view_bg;

    @BindView(R.id.edit_amount)
    EditText edit_amount;

    @Override
    public void initUI() {
        setContentView(R.layout.activity_wallet_tixian);
        super.initUI();
        EventBus.getDefault().register(this);
        tv_back.setTypeface(MyUtlis.getTTF());
        tv_ttf_r.setTypeface(MyUtlis.getTTF());
        tv_title.setText("零钱提现");


    }


    //银行卡数据
    List<BankCardBean.DataBean> bankData = new ArrayList<>();
    UserBean user;

    @Override
    public void initData() {

        user = DaoUtlis.getCurrentLoginUser();

        if (null != user) {
            tv_money.setText("零钱余额￥" + user.getMoney());
        }
        //读取本地银行卡数据显示
        ApiUtlis.walletBankCardList(WalletTiXianActivity.this, MyUtlis.getToken(), new JsonCallBack<BankCardBean>(BankCardBean.class) {
            @Override
            public void onSuccess(Response<BankCardBean> response) {
                if (null != response.body()) {
                    if (response.body().getCode() == AppConstant.CODE_SUCCESS && null != response.body().getData() && response.body().getData().size() > 0) {
                        bankData = response.body().getData();
                        setBankInfo(bankData.get(0));

                        if (null != selectBankCardPop) {
                            selectBankCardPop.setNewData(bankData);
                        }
                    } else {
                        //没有数据
                        tv_bank_name.setText("请先绑定银行卡");
//                        ToastUtils.showShort("你还未绑定过银行卡，先去绑定吧");
//                        BankCardAddActivity.start(WalletTiXianActivity.this);
                    }
                }
            }

            @Override
            public void onError(Response<BankCardBean> response) {
                super.onError(response);
                MyUtlis.finishActivity(WalletTiXianActivity.this);
            }
        });

    }

    /**
     * 设置提现银行卡信息
     *
     * @param dataBean
     */
    private void setBankInfo(BankCardBean.DataBean dataBean) {
        tv_bank_name.setText(dataBean.getBank_name());
        cardId = dataBean.getId();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.eventType == MessageEvent.ADD_BANK_CARD_OK) {
            //银行卡添加成功
            initData();
        }
    }


    public static void start(Context context) {
        Intent intent = new Intent(context, WalletTiXianActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.tv_back)
    public void tv_back() {
        KeyboardUtils.hideSoftInput(this);
        ActivityUtils.finishActivity(this, true);
    }

    SelectBankCardPop selectBankCardPop;

    @OnClick(R.id.rl_bank)
    public void rl_bank() {
//        ToastUtils.showShort("切换银行卡");


        if (null == bankData || bankData.size() == 0 || TextUtils.isEmpty(cardId)) {

            BankCardAddActivity.start(WalletTiXianActivity.this);
            return;
        }


        if (null == selectBankCardPop) {
            selectBankCardPop = new SelectBankCardPop(this, bankData, new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    selectBankCardPop.dismiss();
                    setBankInfo(bankData.get(position));

                }
            });
        }
        selectBankCardPop.show();
    }

    @BindView(R.id.btn_tixian)
    Button btn_tixian;


    public String cardId;

    @OnClick(R.id.btn_tixian)
    public void btn_tixian() {
        KeyboardUtils.hideSoftInput(this);
        final String amount = edit_amount.getText().toString().trim();

        try {
            double amountDounble = Double.valueOf(amount);
            if (amountDounble > user.getMoney()) {
                MyUtlis.showShort(this, "超出零钱余额");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(cardId)) {
            MyUtlis.showShort(WalletTiXianActivity.this, "请选择提现银行卡");
            return;
        }
        if (TextUtils.isEmpty(amount)) {
            MyUtlis.showShort(WalletTiXianActivity.this, "请输入提现金额");
            return;
        }


        ApiUtlis.walletTiXian(WalletTiXianActivity.this, MyUtlis.getToken(), amount, cardId, new JsonCallBack<LoginResponseBean>(LoginResponseBean.class) {
            @Override
            public void onSuccess(Response<LoginResponseBean> response) {
                if (null != response.body()) {
                    if (response.body().getCode() == AppConstant.CODE_SUCCESS && null != response.body().getData()) {
                        ToastUtils.showShort(response.body().getMsg());
                        DaoUtlis.updateUserMoney(response.body().getData().getMoney());
                        MyUtlis.eventTiXianOK();

                        MyUtlis.finishActivity(WalletTiXianActivity.this);
//                        ActivityUtils.finishActivity(BankCardAddActivity.this);
                    } else if (response.body().getCode() == AppConstant.CODE_SUCCESS) {
                        //data=null
//                        ApiUtlis.loginUpdate();
                        UserBean user = DaoUtlis.getCurrentLoginUser();
                        double money = user.getMoney() - Double.valueOf(amount);
                        DaoUtlis.updateUserMoney(money);
                        MyUtlis.eventTiXianOK();
                        ApiUtlis.loginUpdate(WalletTiXianActivity.this);
                        MyUtlis.finishActivity(WalletTiXianActivity.this);
                    } else {
                        MyUtlis.showShortNo(WalletTiXianActivity.this, response.body().getMsg());
                    }
                }
            }

            @Override
            public void onStart(Request<LoginResponseBean, ? extends Request> request) {
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
