package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
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
                                    tip = "恭喜您购买白金会员成功！";
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

                        WebViewActivity.start2(PayActivity.this, response.body().getData());


                    } else {
                        MyUtlis.showShortNo(PayActivity.this, response.body().getMsg());
                    }
                }

            }

            @Override
            public void onStart(Request<JoinVipResponseBean, ? extends Request> request) {
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


    /**
     * @param context
     * @param grade   会员类型
     */
    public static void start(Context context, int grade) {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra(AppConstant.ExtraKey.DATA, grade);
        context.startActivity(intent);
    }

    @OnClick(R.id.tv_back)
    public void tv_back() {
        KeyboardUtils.hideSoftInput(this);
        ActivityUtils.finishActivity(this, true);
    }
}
