package com.zmy.diamond.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.model.Response;
import com.zmy.diamond.R;
import com.zmy.diamond.adapter.TradingDataAdapter;
import com.zmy.diamond.base.MyBaseFragment;
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
import java.util.List;

import butterknife.BindView;

/**
 * 交易 数据页面
 * Created by zhangmengyun on 2018/6/11.
 */

public class TradingDataFragment extends MyBaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {


    //当前数据类型
    public int dataType;


    /**
     * @param dataType 数据类型
     * @return
     */
    public static final TradingDataFragment newInstance(int dataType) {
        TradingDataFragment instance = new TradingDataFragment();
        Bundle args = new Bundle();
        args.putInt(AppConstant.ExtraKey.DATA_TYPE, dataType);
        instance.setArguments(args);
        instance.dataType = dataType;
        return instance;
    }

    @Override
    public int inflaterRootView() {
        return R.layout.layout_recyceview;
    }


    @Override
    public void initUI() {
        super.initUI();
        EventBus.getDefault().register(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        mAdapter = new TradingDataAdapter(new ArrayList<TradingDataBean.DataBean>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter.setEmptyView(MyUtlis.getEmptyView(getContext(), "暂无数据"));
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.setOnLoadMoreListener(TradingDataFragment.this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        //设置刷新监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPages = 0;
                getData();
            }
        });


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.eventType == MessageEvent.UPDATE_TRADING_DATA) {
            if (event.intValue == dataType || event.intValue == 0) {
                //先暂时刷新 第一页
                LogUtils.e("交易页面 刷新数据=" + (dataType == AppConstant.DATA_TYPE_BUY_ACCOUNT ? "买账号" : "卖账号"));
                mPages = 0;
                getData();
            }
        }


    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Override
    public void initData() {
        this.dataType = getArguments().getInt(AppConstant.ExtraKey.DATA_TYPE);
        if (dataType == AppConstant.DATA_TYPE_BUY_ACCOUNT) {
            autoRefresh();
        }
//        UserBean currentLoginUser = DaoUtlis.getCurrentLoginUser();
//        if (null == currentLoginUser) {
//            return;
//        }


    }

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    int mPages = 0;
    boolean isAutoInitData = true;

    /**
     * 自动刷新
     */
    public void autoRefresh() {
        if (isAutoInitData) {
            isAutoInitData = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    getData();
                }
            }, 100);
        }
    }

    TradingDataAdapter mAdapter;

    /**
     * 获取数据
     */
    private void getData() {

        //模拟网络获取数据
        ApiUtlis.getTrade(getActivity(), MyUtlis.getToken(), dataType, mPages, new JsonCallBack<TradingDataBean>(TradingDataBean.class) {
            @Override
            public void onSuccess(Response<TradingDataBean> response) {

//                List<TradingDataBean> list = MyUtlis.getTestTradingData(mPages, dataType);

                List<TradingDataBean.DataBean> list = null;
                if (null != response.body() && null != response.body().getData()) {
                    list = response.body().getData();
                }
                if (mPages == 0) {
                    //刷新
                    if (null == list || list.isEmpty()) {
//                        mAdapter.setNewData(new ArrayList<TradingDataBean.DataBean>());
                    } else {
                        mAdapter.setNewData(list);
                    }
                    swipeRefreshLayout.setRefreshing(false);

                } else if (mPages > 0) {
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
            }

            @Override
            public void onError(Response<TradingDataBean> response) {
                super.onError(response);

                if (mPages == 0) {
                    //刷新失败
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    //加载失败
                    mAdapter.loadMoreFail();
                }
            }
        });


    }

    @Override
    public void addListeners() {

    }


    @Override
    public void onLoadMoreRequested() {
        //加载更多
        mPages++;
        getData();
    }
}
