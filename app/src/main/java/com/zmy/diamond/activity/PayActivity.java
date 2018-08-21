package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.didikee.donate.AlipayDonate;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.zmy.diamond.R;
import com.zmy.diamond.base.MyBaseSwipeBackActivity;
import com.zmy.diamond.utli.ApiUtlis;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.JsonCallBack;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.JoinVipResponseBean;
import com.zmy.diamond.utli.bean.LoginResponseBean;
import com.zmy.diamond.utli.bean.PayBean;
import com.zmy.diamond.utli.bean.VipOrderQueryBean;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 支付页面，可以输入推荐码 选择支付方式
 * Created by zhangmengyun on 2018/6/26.
 */
public class PayActivity extends MyBaseSwipeBackActivity {

    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_tip)
    TextView tv_tip;
    @BindView(R.id.tv_ttf_pay_hint)
    TextView tv_ttf_pay_hint;
    @BindView(R.id.edit_recom_code)
    EditText edit_recom_code;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;


    @Override
    public void initUI() {
        setContentView(R.layout.activity_recom_code);
        super.initUI();
        tv_title.setText("支付");
        tv_back.setTypeface(MyUtlis.getTTF());
        tv_ttf_pay_hint.setTypeface(MyUtlis.getTTF());


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                if (checkedId == R.id.radio_weixin) {
                    MyUtlis.showShortNo(PayActivity.this, "暂不支持微信支付");
                    ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
                }
            }
        });

    }

    int grade;

    @Override
    public void initData() {

        grade = getIntent().getIntExtra(AppConstant.ExtraKey.DATA, 0);

        if (grade == AppConstant.VIP_GRADE_1) {
//白金
            tv_tip.setText("黄金会员输入推荐码可减免1000元费用");
        } else if (grade == AppConstant.VIP_GRADE_2) {
            //黄金
            tv_tip.setText("白金会员输入推荐码可减免1000元费用");
        }


    }

    //订单id
    String orderId;

    @OnClick(R.id.btn_pay)
    public void btn_pay() {
        String recomCode = edit_recom_code.getText().toString().trim();
        startPay(grade, recomCode);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!TextUtils.isEmpty(orderId))
            orderQuery();
    }

    /**
     * 订单查询
     */
    private void orderQuery() {
        //	body:token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHQiOjE1MzM2OTY4NDA0NjEsInVpZCI6MjQ1LCJpYXQiOjE1MzM2MTA0NDA0NjF9.WA8kzTghK7A_qFlevrU6m49HVETHKKwGb-RFQZv69OE&orderId=10349570030
//{"msg":"成功","code":200,"data":{"callback_time":null,"create_time":"1533610445914","user_id":245,"signature":"35b896d627dfb179742ccdf5e9de32ed","price":"0.05","call_back":false,"recom_code":null,"type":"2","order_id":"10349570030","order_info":"245"}}
        ApiUtlis.orderQuery(PayActivity.this, MyUtlis.getToken(), orderId, new JsonCallBack<VipOrderQueryBean>(VipOrderQueryBean.class) {
            @Override
            public void onSuccess(Response<VipOrderQueryBean> response) {
                if (null != response.body()) {
                    if (response.body().getCode() == AppConstant.CODE_SUCCESS) {
                        ApiUtlis.loginUpdate(PayActivity.this, new JsonCallBack<LoginResponseBean>(LoginResponseBean.class) {
                            @Override
                            public void onSuccess(Response<LoginResponseBean> response) {
                                String tip = "";
                                if (response.body().getData().getGrade() == AppConstant.VIP_GRADE_1) {
                                    tip = "恭喜您购买白金会员成功";
                                    ToastUtils.showShort(tip);
                                    MyUtlis.finishActivity(PayActivity.this);
                                } else if (response.body().getData().getGrade() == AppConstant.VIP_GRADE_2) {
                                    tip = "恭喜您购买黄金会员成功";
                                    ToastUtils.showShort(tip);
                                    MyUtlis.finishActivity(PayActivity.this);
                                } else {
                                    tip = "支付失败";
                                    ToastUtils.showShort(tip);
                                }

                            }
                        });

                    } else {
                        MyUtlis.showShortNo(PayActivity.this, response.body().getMsg());
                    }
                }
            }

            @Override
            public void onStart(Request<VipOrderQueryBean, ? extends Request> request) {
                super.onStart(request);
                showLoading(false);
            }


            @Override
            public void onFinish() {
                super.onFinish();
                orderId = "";
                hideLoading();
            }
        });

    }

    /**
     * 开始支付
     *
     * @param grade
     * @param recomCode
     */
    private void startPay(int grade, String recomCode) {
        ApiUtlis.joinVIP(PayActivity.this, MyUtlis.getToken(), grade, recomCode, new JsonCallBack<JoinVipResponseBean>(JoinVipResponseBean.class) {
            @Override
            public void onSuccess(Response<JoinVipResponseBean> response) {


                if (null != response.body()) {

                    if (response.body().getCode() == AppConstant.CODE_SUCCESS) {
                        //开启支付
                        orderId = response.body().getData().getOrder_id();
//                        startPay(joinVipResponseBean);
//                        WebViewActivity.start2(PayActivity.this, response.body().getData());

                        pay(response.body().getData());


                    } else {
                        hideLoading();
                        MyUtlis.showShortNo(PayActivity.this, response.body().getMsg());

                    }
                }

            }

            @Override
            public void onStart(Request<JoinVipResponseBean, ? extends Request> request) {
                super.onStart(request);
                showLoading(false);
            }

//            @Override
//            public void onFinish() {
//                super.onFinish();
//                hideLoading();
//            }

            @Override
            public void onError(Response<JoinVipResponseBean> response) {
                super.onError(response);
                hideLoading();
            }
        });


    }


    /**
     * 开始支付
     *
     * @param data
     */
    private void pay(JoinVipResponseBean.DataBean data) {

        if (null == data) {
            hideLoading();
            ToastUtils.showShort("支付错误，请重试");
            return;
        }
        OkGo.<String>post(AppConstant.Api.paypayzhuPayJson)
                .tag(PayActivity.this)
                .params("api_user", data.getApi_user())
                .params("price", data.getPrice())
                .params("type", data.getType())
                .params("redirect", data.getRedirect())
                .params("order_id", data.getOrder_id())
                .params("order_info", data.getOrder_info())
                .params("signature", data.getSignature())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        PayBean payBean = null;
                        try {
                            payBean = new Gson().fromJson(response.body(), PayBean.class);
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        if (null == payBean) {
                            payError();
                            return;
                        }

                        if (payBean.getCode() == 200 && null != payBean.getInfo() && !TextUtils.isEmpty(payBean.getInfo().getPay_url())) {
                            //可以支付
                            if (payBean.getInfo().getPay_type() == 1) {
                                //微信
                                payError("暂不支持微信支付");
                            } else if (payBean.getInfo().getPay_type() == 2) {
                                //支付宝
                                boolean hasInstalledAlipayClient = AlipayDonate.hasInstalledAlipayClient(PayActivity.this);
                                if (hasInstalledAlipayClient) {
                                    String pay_url = payBean.getInfo().getPay_url();
//                                    HTTPS://QR.ALIPAY.COM/FKX03305AHV8MAPHSW4O8C
                                    String payCode = "";
                                    try {
                                        payCode = pay_url.substring(pay_url.lastIndexOf("/", pay_url.length()));
                                        LogUtils.e(payCode);
                                    } catch (Exception e) {
                                        e.printStackTrace();

                                    }
                                    if (TextUtils.isEmpty(payCode)) {
                                        payError();
                                        return;
                                    }
                                    AlipayDonate.startAlipayClient(PayActivity.this, payCode);
                                } else {
                                    payError("支付错误\n请安装支付宝后重试");
                                }
                            }

                        } else {
                            //不可以支付
                            payError("支付错误：" + payBean.getCode());

                        }


                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        payError();
                    }

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        hideLoading();
                    }
                });


    }


    /**
     * @param context
     * @param grade   会员类型
     */
    public static void start(Context context, int grade) {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra(AppConstant.ExtraKey.DATA, grade);
        context.startActivity(intent);
    }


    @OnClick(R.id.tv_service)
    public void tv_service() {
        MyUtlis.openServiceQQ(this);
    }

    @OnClick(R.id.tv_back)
    public void tv_back() {
        KeyboardUtils.hideSoftInput(this);
        ActivityUtils.finishActivity(this, true);
    }

    /**
     * 支付错误
     */
    public void payError(String text) {
        orderId = "";
        if (!TextUtils.isEmpty(text)) {
            MyUtlis.showShortNo(PayActivity.this, text);
        } else {
            MyUtlis.showShortNo(PayActivity.this, "支付错误，请重试");
        }
    }

    public void payError() {
        payError("");
    }
}
