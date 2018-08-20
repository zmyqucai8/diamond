package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.model.Response;
import com.zmy.diamond.R;
import com.zmy.diamond.adapter.WalletDetailsAdapter;
import com.zmy.diamond.base.MyBaseSwipeBackActivity;
import com.zmy.diamond.utli.ApiUtlis;
import com.zmy.diamond.utli.JsonCallBack;
import com.zmy.diamond.utli.MessageEvent;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.UserBean;
import com.zmy.diamond.utli.bean.WalletDetailsBean;
import com.zmy.diamond.utli.dao.DaoUtlis;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 钱包页面
 * Created by zhangmengyun on 2018/6/16.
 */
public class WalletActivity extends MyBaseSwipeBackActivity implements BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_ttf_wallet)
    TextView tv_ttf_wallet;
    @BindView(R.id.tv_money)
    TextView tv_money;


    @OnClick(R.id.tv_bank_card)
    public void tv_bank_card() {
//        ToastUtils.showShort( "银行卡");
        BankCardActivity.start(this);
    }

    @OnClick(R.id.tv_tixian)
    public void tv_tixian() {
//        ToastUtils.showShort("提现");

        WalletTiXianActivity.start(this);
    }

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    WalletDetailsAdapter mAdapter;

    @Override
    public void initUI() {
        setContentView(R.layout.activity_wallet);
        super.initUI();
        EventBus.getDefault().register(this);
        tv_title.setText("我的钱包");
        tv_back.setTypeface(MyUtlis.getTTF());
        tv_ttf_wallet.setTypeface(MyUtlis.getTTF());

        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        mAdapter = new WalletDetailsAdapter(new ArrayList<WalletDetailsBean.DataBean>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter.setEmptyView(MyUtlis.getEmptyView(this, "暂无明细"));
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.setOnLoadMoreListener(WalletActivity.this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        //设置刷新监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 0;
                getData();
            }
        });
    }

    UserBean user;

    @Override
    public void initData() {
        user = DaoUtlis.getCurrentLoginUser();
        setMoney(user.getMoney());
        autoRefresh();


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

    int mPage = 0;

    private void getData() {
        ApiUtlis.walletDetails(WalletActivity.this, MyUtlis.getToken(), mPage, new JsonCallBack<WalletDetailsBean>(WalletDetailsBean.class) {
            @Override
            public void onSuccess(Response<WalletDetailsBean> response) {

                try {

                    List<WalletDetailsBean.DataBean> list = null;
                    if (null != response.body() && null != response.body().getData()) {
                        list = response.body().getData();
                    }
                    if (mPage == 0) {
                        //刷新
                        if (null == list || list.isEmpty()) {
//                        mAdapter.setNewData(new ArrayList<TradingDataBean.DataBean>());
                        } else {
                            mAdapter.setNewData(list);
                        }
                        swipeRefreshLayout.setRefreshing(false);

                    } else if (mPage > 0) {
                        //加载
                        if (null == list || list.isEmpty()) {
//                        mAdapter.loadMoreFail();
//                    } else if (list.isEmpty()) {
                            mAdapter.loadMoreEnd();
                        } else if (list.size() >= 20) {
                            //还有下一页
                            mAdapter.addData(list);
                            mAdapter.loadMoreComplete();
                        } else {
                            //没有下一页
                            mAdapter.addData(list);
                            mAdapter.loadMoreEnd();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Response<WalletDetailsBean> response) {
                super.onError(response);

                try {

                    if (mPage == 0) {
                        //刷新失败
                        swipeRefreshLayout.setRefreshing(false);
                    } else {
                        //加载失败
                        mAdapter.loadMoreFail();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public void setMoney(double money) {
        tv_money.setText(String.valueOf(money));
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, WalletActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.tv_back)
    public void tv_back() {
        ActivityUtils.finishActivity(this, true);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

        if (event.eventType == MessageEvent.TIXIAN_OK) {
            LogUtils.e("提现成功，刷新钱包页面");
            mPage = 0;
            initData();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onLoadMoreRequested() {
        //加载更多
        mPage++;
        getData();
    }
}
