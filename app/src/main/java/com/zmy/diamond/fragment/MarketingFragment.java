package com.zmy.diamond.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.zmy.diamond.R;
import com.zmy.diamond.adapter.MarketingAdapter;
import com.zmy.diamond.base.MyBaseFragment;
import com.zmy.diamond.utli.MessageEvent;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.CollectRecordBean;
import com.zmy.diamond.utli.bean.UserBean;
import com.zmy.diamond.utli.dao.DaoUtlis;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 营销页面
 * Created by zhangmengyun on 2018/6/11.
 */

public class MarketingFragment extends MyBaseFragment {


    public MarketingAdapter mAdapter;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Override
    public int inflaterRootView() {
        return R.layout.fragment_marketing;
    }

    @Override
    public void initUI() {
        super.initUI();
        EventBus.getDefault().register(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MarketingAdapter(new ArrayList<CollectRecordBean>());
        mAdapter.setEmptyView(MyUtlis.getEmptyView(getActivity(), "暂无记录"));
//        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mRecyclerView.setAdapter(mAdapter);
        refreshData();
    }



    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

        if (event.eventType == MessageEvent.ADD_COLLECT_DATA_OK) {

            LogUtils.e("营销页面: 添加数据成功,开始刷新");


            refreshData();

        }
    }

    private void refreshData() {
        List<CollectRecordBean> collectRecord = DaoUtlis.getCollectRecord(MyUtlis.getLoginUserId());
        if (null != collectRecord ) {
            mAdapter.setNewData(collectRecord);
        }
    }


    @Override
    public void initData() {
        UserBean currentLoginUser = DaoUtlis.getCurrentLoginUser();
        if (null == currentLoginUser) {
            return;
        }

    }

    @Override
    public void addListeners() {

    }


}
