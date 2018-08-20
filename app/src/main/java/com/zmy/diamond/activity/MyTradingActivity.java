package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.model.Response;
import com.zmy.diamond.R;
import com.zmy.diamond.adapter.TradingDataAdapter;
import com.zmy.diamond.base.MyBaseSwipeBackActivity;
import com.zmy.diamond.utli.ApiUtlis;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.JsonCallBack;
import com.zmy.diamond.utli.MessageEvent;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.TradingDataBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的交易
 * Created by zhangmengyun on 2018/6/16.
 */

public class MyTradingActivity extends MyBaseSwipeBackActivity {
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    TradingDataAdapter mAdapter;

    @Override
    public void initUI() {
        setContentView(R.layout.activity_my_trading);
        super.initUI();

        EventBus.getDefault().register(this);
        tv_title.setText("我的交易");
        tv_back.setTypeface(MyUtlis.getTTF());

        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        mAdapter = new TradingDataAdapter(new ArrayList<TradingDataBean.DataBean>(), true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter.setEmptyView(MyUtlis.getEmptyView(this, "暂无数据"));
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mRecyclerView.setAdapter(mAdapter);
        //设置刷新监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    /**
     * 自动刷新
     */
    public void autoRefresh() {

        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                getData();
            }
        }, 100);

    }

    private void getData() {

        ApiUtlis.operationPublishQuery(MyTradingActivity.this,MyUtlis.getToken(), new JsonCallBack<TradingDataBean>(TradingDataBean.class) {
            @Override
            public void onSuccess(Response<TradingDataBean> response) {


                if (null != response.body()) {
                    if (response.body().getCode() == AppConstant.CODE_SUCCESS && null != response.body().getData()) {
                        mAdapter.setNewData(response.body().getData());
                    } else {
                        MyUtlis.showShort(MyTradingActivity.this, response.body().getMsg());
                    }
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    @Override
    public void initData() {

        autoRefresh();
    }


    public static void start(Context context) {
        Intent intent = new Intent(context, MyTradingActivity.class);
        context.startActivity(intent);
    }


    @OnClick(R.id.tv_back)
    public void tv_back() {
        ActivityUtils.finishActivity(this, true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.eventType == MessageEvent.UPDATE_MY_TRADING_DATA) {
            autoRefresh();
        }
    }
}
