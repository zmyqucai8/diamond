package com.zmy.diamond.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zmy.diamond.R;
import com.zmy.diamond.adapter.HomeDataAdapter;
import com.zmy.diamond.base.BaseApp;
import com.zmy.diamond.base.MyBaseFragment;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.CollectUtlis;
import com.zmy.diamond.utli.MessageEvent;
import com.zmy.diamond.utli.MyLinearLayoutManager;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.DataBean;
import com.zmy.diamond.utli.bean.PlatformBean;
import com.zmy.diamond.utli.bean.UserBean;
import com.zmy.diamond.utli.dao.DaoUtlis;
import com.zmy.diamond.utli.view.MyRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 平台数据采集页面
 * Created by zhangmengyun on 2018/6/19.
 */

public class DataFragment extends MyBaseFragment implements MyRecyclerView.OnScrollCallback {

    public PlatformBean bean;
    @BindView(R.id.recyclerView)
    MyRecyclerView recyclerView_data;
    HomeDataAdapter dataAdapter;

    public static DataFragment getInstance(PlatformBean bean) {
        DataFragment fragment = new DataFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", bean);
        fragment.setArguments(bundle);
        fragment.bean = bean;

        return fragment;
    }


    @Override
    public int inflaterRootView() {
        return R.layout.fragment_data;
    }


    @Override
    public void initUI() {
        super.initUI();
        EventBus.getDefault().register(this);
        MyLinearLayoutManager manager_data = new MyLinearLayoutManager(getContext());
        manager_data.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_data.setLayoutManager(manager_data);
        recyclerView_data.setOnScrollCallback(this);
    }

    UserBean user;

    @Override
    public void initData() {
        if (null == bean) {
            bean = (PlatformBean) getArguments().getSerializable("data");
        }

        user = DaoUtlis.getCurrentLoginUser();
        if (AppConstant.DATA_FRAGMENT_IS_INIT_DATA) {
            dataAdapter = new HomeDataAdapter(user, DaoUtlis.getDataByPlatformId(null != bean ? bean.platformId : 0));
        } else {
            dataAdapter = new HomeDataAdapter(user, new ArrayList<DataBean>());
        }
        dataAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        dataAdapter.setEmptyView(MyUtlis.getEmptyView(getContext(), getString(R.string.hint_no_data, bean.name)));
        dataAdapter.setHeaderAndEmpty(true);
        recyclerView_data.setAdapter(dataAdapter);
        MyUtlis.eventUpdatePlatfromDataCount();

    }

    /**
     * 从数据库重新获取数据 (刷新不走数据库）
     */
    public void refreshData(List<DataBean> list) {
//        DaoUtlis.getDataByPlatformId(bean.platformId)

        if (list == null) {
            list = new ArrayList<>();
        }
        dataAdapter.setNewData(list);
        MyUtlis.eventUpdatePlatfromDataCount();
    }

    @Override
    public void addListeners() {

    }


    public void scrollBottom(RecyclerView recyclerView) {
        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
    }


    //是否采集中
    boolean isCollectIng = false;

    /**
     * 开始采集数据
     *
     * @param city
     * @param key
     */
    public void startCollect(String city, String key) {
        LogUtils.e(bean.name + " 开始采集 city=" + city + " key=" + key);

        BaseApp.currentDownloadDataCount = 0;
        isCollectIng = true;
        CollectUtlis.getInstance().startCollect(getActivity(), bean, city, key, MyUtlis.getCollectDataPhoneType(), new CollectUtlis.OnCollectListener() {
            @Override
            public void onCollect(List<DataBean> dataList) {
//                isCollectIng = true;
//                dataAdapter.addData(dataList);
//                scrollBottom(recyclerView_data);
//                MyUtlis.eventUpdatePlatfromDataCount();
//                DaoUtlis.addData(dataList);
//                MyUtlis.eventCollectComplete();
            }

            @Override
            public void onCollectComplete() {
                isCollectIng = false;

//                dataAdapter.addData(getData(10));
//                scrollBottom(recyclerView_data);
//                MyUtlis.eventCollectComplete();
                MyUtlis.showShortSimple(getActivity(), getString(R.string.hint_data_collect_ok, bean.name));
                MyUtlis.eventCollectComplete();

                MyUtlis.uploadDownNumber(getContext());
            }

            @Override
            public void onCollectError() {
                isCollectIng = false;

                MyUtlis.showShortSimple(getActivity(), getString(R.string.hint_data_collect_no, bean.name));
                MyUtlis.eventCollectError();


            }
        });


    }


    /**
     * 删除当前平台所有数据
     */
    public void clearData() {

//        boolean b = DaoUtlis.deleteAllData(MyUtlis.getLoginUserId(), bean.platformId);
//        if (b) {
//            MyUtlis.showShortSimple(getActivity(), getString(R.string.hint_delete_platform_data_ok, bean.name));
        refreshData(null);
//            MyUtlis.clickEvent(AppConstant.CLICK.umeng_home_clear_data);
//        } else {
//            MyUtlis.showShortSimple(getActivity(), getString(R.string.hint_delete_platform_data_no, bean.name));
//        }


    }

    /**
     * 停止采集
     */
    public void stopCollect() {
//        MyUtlis.showShortSimple(getActivity(), getString(R.string.hint_stop_collect, bean.name));
        BaseApp.isStopCollect = true;
        try {
            recyclerView_data.scrollToPosition(dataAdapter.getItemCount() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStateChanged(MyRecyclerView recycler, int state) {
        String log = " onStateChanged ";
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
//            log = "停止滚动";
//            Log.e("TAG", log);
            if (isCollectIng) {
                //采集中不做滚动判断
                isCollectIng = false;
//                MyUtlis.eventCollectComplete();
                return;
            }
            if (scrollDirection == 0) {
//                Log.e("TAG", "最终滚动结果=无");
            } else if (scrollDirection == 1) {
//                Log.e("TAG", "最终滚动结果=向上");
                MyUtlis.eventHomeMenuVisibility(true);
            } else if (scrollDirection == 2) {
//                Log.e("TAG", "最终滚动结果=向下");
                MyUtlis.eventHomeMenuVisibility(false);
            }
            scrollDirection = -1;
            return;

        } else if (state == RecyclerView.SCROLL_STATE_DRAGGING) {
            log = "正在被外部拖拽,一般为用户正在用手指滚动";
        } else if (state == RecyclerView.SCROLL_STATE_SETTLING) {
            log = "自动滚动开始";
        }
//        Log.e("TAG", log);
    }


    /**
     * 滚动方向  0=忽略滚动  1=向上 2=向下
     */
    int scrollDirection = -1;


    @Override
    public void onScrollUp(MyRecyclerView recycler, int dy) {

        if (scrollDirection != 2) {
//            Log.e("TAG", "向上滚动" + dy);
            scrollDirection = 1;
        }
//        else if (scrollDirection != 1 && scrollDirection != 2) {
//            //因为会一直回调,所以需要判断是否=过1
//            Log.e("TAG", "忽略向上滚动" + dy);
//            scrollDirection = 0;
//        }
    }

    @Override
    public void onScrollDown(MyRecyclerView recycler, int dy) {
        if (dy > 10 && scrollDirection != 1) {
//            Log.e("TAG", "向下滚动" + dy);
            scrollDirection = 2;
        } else if (scrollDirection != 2 && scrollDirection != 1) {
//            Log.e("TAG", "忽略向下滚动" + dy);
            scrollDirection = 0;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.eventType == MessageEvent.COLLECT_DATA
                && event.intValue == bean.platformId && null != event.dataList) {

            LogUtils.e(bean.name + "采集数据成功");
            //采集到了数据 ,并且platformId是当前平台的id,并且数据不为空
            //直接加入到UI显示, 不管之前是否存在, 因为在存储到本地的时候,已经进行了过滤.
            isCollectIng = true;

//            todo: 根据当前采集数据UI加载速度等级来控制采集中的ui加载速度.
            //方案1.
            //1.采集的数据先添加到 临时集合中,
            //2.开启定时任务,每次从集合中取出一定量的数据,再加载到当前ui中
            //3.这种方式给用户造成一种,数据采集慢的感觉

            //方案2
            //1.调整滚动速度
            dataAdapter.addData(event.dataList);
            scrollBottom(recyclerView_data);
            MyUtlis.eventUpdatePlatfromDataCount();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
