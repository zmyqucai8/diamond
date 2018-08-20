package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zmy.diamond.R;
import com.zmy.diamond.adapter.VipAdapter;
import com.zmy.diamond.base.MyBaseSwipeBackActivity;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.MessageEvent;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.UserBean;
import com.zmy.diamond.utli.bean.VipBean;
import com.zmy.diamond.utli.dao.DaoUtlis;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * VIP页面
 * Created by zhangmengyun on 2018/6/13.
 */

public class VipActivity extends MyBaseSwipeBackActivity {

    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    TextView tv_vip;
    TextView tv_vip_time;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    @Override
    public void initUI() {
        setContentView(R.layout.activity_vip);
        super.initUI();

        EventBus.getDefault().register(this);
        tv_back.setTypeface(MyUtlis.getTTF());
        tv_title.setText(getString(R.string.title_vip));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    VipAdapter vipAdapter;
    UserBean user;

    @Override
    public void initData() {


        user = DaoUtlis.getCurrentLoginUser();
        vipAdapter = new VipAdapter(MyUtlis.getVipItemData());
        vipAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        vipAdapter.addHeaderView(getVipHeadView());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(vipAdapter);
            }
        }, 0);

        vipAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                vipAdapter.refreshCheck(position);
            }
        });


        setVip();
    }

    //1=黄金会员2=白金会员
    private void setVip() {
        if (null != user) {

            int grade = user.getGrade();

            if (grade == AppConstant.VIP_GRADE_1) {
                tv_vip_time.setVisibility(View.GONE);
                tv_vip.setText("您当前已是黄金会员");
            } else if (grade == AppConstant.VIP_GRADE_2) {
                tv_vip_time.setVisibility(View.GONE);
                tv_vip.setText("您当前已是白金会员");
            } else {
                tv_vip.setText("您还不是会员");
                tv_vip_time.setText("您可以选择购买会员");
                tv_vip_time.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.eventType == MessageEvent.UPDATE_LOGIN_USER_INFO) {

            LogUtils.e("VIP页面刷新数据");
            user = DaoUtlis.getCurrentLoginUser();
            setVip();
        }
    }

    /**
     * 获取vipheadview
     *
     * @return
     */
    private View getVipHeadView() {
        View headViewVip = View.inflate(VipActivity.this, R.layout.view_head_vip, null);
        tv_vip_time = headViewVip.findViewById(R.id.tv_vip_day);
        TextView tv_ttf_vip = headViewVip.findViewById(R.id.tv_ttf_vip);
        tv_vip = headViewVip.findViewById(R.id.tv_vip_state);


        if (null != user && !TextUtils.isEmpty(user.getCode())) {
            View ll_my_code = headViewVip.findViewById(R.id.ll_my_code);
            TextView tv_my_code = headViewVip.findViewById(R.id.tv_my_code);
            tv_my_code.setText(String.valueOf(user.getCode()));
            TextView tv_copy = headViewVip.findViewById(R.id.tv_copy);


            tv_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //复制
                    MyUtlis.copyText(user.getCode());
                    MyUtlis.showShortYes(VipActivity.this, "推荐码已复制");
                }
            });
            ll_my_code.setVisibility(View.VISIBLE);

        }

        tv_ttf_vip.setTypeface(MyUtlis.getTTF());
        return headViewVip;

    }

    public static void start(Context context) {
        Intent intent = new Intent(context, VipActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.tv_back)
    public void tv_back() {
        ActivityUtils.finishActivity(this, true);
    }

    @OnClick(R.id.btn_buy)
    public void btn_buy() {
//        ToastUtils.showShort("去支付");


        List<VipBean> data = vipAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            VipBean bean = data.get(i);
            if (bean.isCheck) {
//                MyUtlis.showServiceInfoAlert(this, getString(R.string.hint_buy_vip_des, bean.name, bean.price_des));
//                startBuy(bean.grade);
                PayActivity.start(this, bean.grade);
                break;
            }
        }
    }

//    private void startBuy(int grade) {
//        ApiUtlis.joinVIP(grade, MyUtlis.getToken(), new JsonCallBack<JoinVipResponseBean>(JoinVipResponseBean.class) {
//            @Override
//            public void onSuccess(Response<JoinVipResponseBean> response) {
//
//
//                if (null != response.body()) {
//
//                    if (response.body().getCode() == AppConstant.CODE_SUCCESS) {
//                        //开启支付
//                        orderId = response.body().getData().getOrder_id();
////                        startPay(joinVipResponseBean);
//
//                        WebViewActivity.start2(VipActivity.this, response.body().getData());
//
//
//                    } else {
//                        MyUtlis.showShortNo(VipActivity.this, response.body().getMsg());
//                    }
//                }
//            }
//        });
//
//
//    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        if (!TextUtils.isEmpty(orderId))
//            orderQuery();
//    }

//    String orderId;

    /**
     * 订单查询
     */
//    private void orderQuery() {
//        //	body:token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHQiOjE1MzM2OTY4NDA0NjEsInVpZCI6MjQ1LCJpYXQiOjE1MzM2MTA0NDA0NjF9.WA8kzTghK7A_qFlevrU6m49HVETHKKwGb-RFQZv69OE&orderId=10349570030
////{"msg":"成功","code":200,"data":{"callback_time":null,"create_time":"1533610445914","user_id":245,"signature":"35b896d627dfb179742ccdf5e9de32ed","price":"0.05","call_back":false,"recom_code":null,"type":"2","order_id":"10349570030","order_info":"245"}}
//        ApiUtlis.orderQuery(MyUtlis.getToken(), orderId, new JsonCallBack<VipOrderQueryBean>(VipOrderQueryBean.class) {
//            @Override
//            public void onSuccess(Response<VipOrderQueryBean> response) {
//
//                if (null != response.body()) {
//                    if (response.body().getCode() == AppConstant.CODE_SUCCESS) {
//                        ApiUtlis.login();
//                    } else {
//                        MyUtlis.showShortNo(VipActivity.this, response.body().getMsg());
//                    }
//                }
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                orderId = "";
//            }
//        });
//
//    }

}
