package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
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
import com.zmy.diamond.utli.bean.MyTradingModifyBean;
import com.zmy.diamond.utli.bean.PublicResponseBean;
import com.zmy.diamond.utli.bean.TradingDataBean;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 账户操作页面
 * 必传 actionType  =1发布 =2修改 &编辑
 * dataType  买或者卖
 * Created by zhangmengyun on 2018/6/13.
 */

public class AccountActionActivity extends MyBaseSwipeBackActivity {

    @BindView(R.id.tv_ttf_title_industry)
    TextView tv_ttf_title_industry;
    @BindView(R.id.tv_ttf_title_city)
    TextView tv_ttf_title_city;
    @BindView(R.id.tv_ttf_title_friendCount)
    TextView tv_ttf_title_friendCount;
    @BindView(R.id.tv_ttf_title_price)
    TextView tv_ttf_title_price;
    @BindView(R.id.tv_ttf_title_weixin)
    TextView tv_ttf_title_weixin;
    @BindView(R.id.tv_ttf_title_describe)
    TextView tv_ttf_title_describe;


    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_menu_1)
    TextView tv_menu_1;
    @BindView(R.id.tv_menu_2)
    TextView tv_menu_2;


    @BindView(R.id.edit_industry)
    EditText edit_industry;
    @BindView(R.id.edit_city)
    EditText edit_city;
    @BindView(R.id.edit_friendCount)
    EditText edit_friendCount;
    @BindView(R.id.edit_price)
    EditText edit_price;
    @BindView(R.id.edit_describe)
    EditText edit_describe;
    @BindView(R.id.edit_weixin)
    EditText edit_weixin;


    int dataType;
    int actionType;


    //actionType=2 value
    TradingDataBean.DataBean dataBean;

    @Override
    public void initUI() {
        setContentView(R.layout.activity_release_account);
        super.initUI();
        mImmersionBar.keyboardEnable(true).init();
        tv_ttf_title_industry.setTypeface(MyUtlis.getTTF());
        tv_ttf_title_city.setTypeface(MyUtlis.getTTF());
        tv_ttf_title_describe.setTypeface(MyUtlis.getTTF());
        tv_ttf_title_friendCount.setTypeface(MyUtlis.getTTF());
        tv_ttf_title_price.setTypeface(MyUtlis.getTTF());
        tv_ttf_title_weixin.setTypeface(MyUtlis.getTTF());
        tv_back.setTypeface(MyUtlis.getTTF());

        actionType = getIntent().getIntExtra(AppConstant.ExtraKey.ACTION_TYPE, AppConstant.ACTION_ACCOUNT_RELEASE);

        if (actionType == AppConstant.ACTION_ACCOUNT_RELEASE) {
            //发布模式
            dataType = getIntent().getIntExtra(AppConstant.ExtraKey.DATA_TYPE, AppConstant.DATA_TYPE_BUY_ACCOUNT);
            if (dataType == AppConstant.DATA_TYPE_BUY_ACCOUNT) {
                tv_title.setText(getString(R.string.text_buy_account));
            } else {
                tv_title.setText(getString(R.string.text_sell_account));
            }
            tv_menu_2.setText(getString(R.string.text_release));
        } else if (actionType == AppConstant.ACTION_ACCOUNT_EDEITE) {

            dataBean = (TradingDataBean.DataBean) getIntent().getSerializableExtra(AppConstant.ExtraKey.DATA_BEAN);

            //编辑模式 显示删除按钮
            tv_menu_1.setVisibility(View.VISIBLE);
            tv_menu_1.setTypeface(MyUtlis.getTTF());
            tv_title.setText(getString(R.string.text_edit_info));
            tv_menu_2.setText(getString(R.string.text_complete));
            setDefaultData();
        }
    }

    /**
     * 编辑模式设置默认数据
     */
    private void setDefaultData() {

        if (null != dataBean) {
            edit_describe.setText(dataBean.getTrade_describe());
            edit_city.setText(dataBean.getCity());
            edit_friendCount.setText(String.valueOf(dataBean.getFriend_number()));
            edit_industry.setText(dataBean.getIndustry());
            edit_price.setText(String.valueOf(dataBean.getPrice()));
            edit_weixin.setText(dataBean.getWeixin());
        } else {
            MyUtlis.showShortNo(this, getString(R.string.text_data_error));
        }
    }

    @Override
    public void initData() {
    }


    @OnClick(R.id.tv_menu_1)
    public void tv_menu_1() {
        //删除
        KeyboardUtils.hideSoftInput(this);

        new AlertView(getString(R.string.hint_text), getString(R.string.hint_delete_trading), null, new String[]{getString(R.string.hint_confirm)}, new String[]{getString(R.string.hint_cancel)}, this,
                AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                //0=好的

                if (position == 0)
                    ApiUtlis.operationPublishDelete(AccountActionActivity.this, MyUtlis.getToken(), dataBean.getId(), new JsonCallBack<PublicResponseBean>(PublicResponseBean.class) {
                        @Override
                        public void onSuccess(Response<PublicResponseBean> response) {


                            if (null != response.body()) {

                                if (response.body().getCode() == AppConstant.CODE_SUCCESS) {
                                    MyUtlis.eventUpdateTradingData();
                                    MyUtlis.eventUpdateMyTradingData();
                                    ToastUtils.showShort(response.body().getMsg());

                                    ActivityUtils.finishActivity(AccountActionActivity.this);
                                } else {
                                    MyUtlis.showShort(AccountActionActivity.this, response.body().getMsg());
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
        }).show();


    }

    @OnClick(R.id.tv_menu_2)
    public void tv_menu_2() {
//        ToastUtils.showShort("发布");
        KeyboardUtils.hideSoftInput(this);
        String city = edit_city.getText().toString().trim();

        if (TextUtils.isEmpty(city)) {
            MyUtlis.showShort(this, getString(R.string.hint_input_city));
            return;
        }
        String industry = edit_industry.getText().toString().trim();

        if (TextUtils.isEmpty(industry)) {
            MyUtlis.showShort(this, getString(R.string.hint_input_industry));
            return;
        }

        String friendCount = edit_friendCount.getText().toString().trim();

        Integer friendCountInt;
        try {

            friendCountInt = Integer.valueOf(friendCount);
            if (TextUtils.isEmpty(friendCount) || friendCountInt <= 0) {
                MyUtlis.showShort(this, getString(R.string.hint_input_friend_count));
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyUtlis.showShort(this, getString(R.string.hint_input_friend_count_2));
            return;
        }


        String price = edit_price.getText().toString().trim();
        int priceInt;
        try {

            priceInt = Integer.valueOf(price);
            if (TextUtils.isEmpty(price) || priceInt <= 0) {
                MyUtlis.showShort(this, getString(R.string.hint_input_price));
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyUtlis.showShort(this, getString(R.string.hint_input_price_2));
            return;
        }

        String weixin = edit_weixin.getText().toString().trim();

        if (TextUtils.isEmpty(weixin)) {
            MyUtlis.showShort(this, getString(R.string.hint_input_weixin));
            return;
        }
        String describe = edit_describe.getText().toString().trim();


        if (actionType == AppConstant.ACTION_ACCOUNT_RELEASE) {
            //发布
            ApiUtlis.operationPublish(AccountActionActivity.this, MyUtlis.getToken(), dataType, weixin, priceInt, describe, industry, city, friendCountInt, new JsonCallBack<PublicResponseBean>(PublicResponseBean.class) {
                @Override
                public void onSuccess(Response<PublicResponseBean> response) {
                    if (null != response.body()) {
                        if (response.body().getCode() == AppConstant.CODE_SUCCESS) {
                            //刷新
                            MyUtlis.eventUpdateTradingData(dataType);
                            ToastUtils.showShort(response.body().getMsg());
                            ActivityUtils.finishActivity(AccountActionActivity.this);
                        } else {
                            MyUtlis.showShortNo(AccountActionActivity.this, response.body().getMsg());
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
        } else {
            //编辑
            ApiUtlis.operationPublishModify(AccountActionActivity.this, MyUtlis.getToken(), dataBean.getId(), dataBean.getType(), weixin, priceInt, describe, industry, city, friendCountInt, new JsonCallBack<MyTradingModifyBean>(MyTradingModifyBean.class) {
                @Override
                public void onSuccess(Response<MyTradingModifyBean> response) {
                    if (null != response.body()) {
                        if (response.body().getCode() == AppConstant.CODE_SUCCESS) {
                            //刷新
                            MyUtlis.eventUpdateTradingData(dataType);
                            MyUtlis.eventUpdateMyTradingData();
                            ToastUtils.showShort(response.body().getMsg());
                            ActivityUtils.finishActivity(AccountActionActivity.this);
                        } else {
                            MyUtlis.showShortNo(AccountActionActivity.this, response.body().getMsg());
                        }
                    }
                }

                @Override
                public void onStart(Request<MyTradingModifyBean, ? extends Request> request) {
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

    @OnClick(R.id.tv_back)
    public void tv_back() {
        KeyboardUtils.hideSoftInput(this);
        ActivityUtils.finishActivity(this, true);
    }

    /**
     * 发布账户
     *
     * @param context
     * @param dataType 买还是卖
     */
    public static void startRelease(Context context, int dataType) {
        Intent intent = new Intent(context, AccountActionActivity.class);
        intent.putExtra(AppConstant.ExtraKey.DATA_TYPE, dataType);
        intent.putExtra(AppConstant.ExtraKey.ACTION_TYPE, AppConstant.ACTION_ACCOUNT_RELEASE);
        context.startActivity(intent);
    }

    /**
     * 编辑账户
     *
     * @param context
     * @param dataBean
     */
    public static void startModify(Context context, TradingDataBean.DataBean dataBean) {
        Intent intent = new Intent(context, AccountActionActivity.class);
//        intent.putExtra(AppConstant.ExtraKey.DATA_TYPE, dataType);
        intent.putExtra(AppConstant.ExtraKey.ACTION_TYPE, AppConstant.ACTION_ACCOUNT_EDEITE);
        intent.putExtra(AppConstant.ExtraKey.DATA_BEAN, dataBean);
        context.startActivity(intent);
    }
}
