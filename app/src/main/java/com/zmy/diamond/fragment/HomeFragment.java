package com.zmy.diamond.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.dingmouren.annularmenu.AnnularMenu;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zmy.diamond.R;
import com.zmy.diamond.activity.CollectSettingActivity;
import com.zmy.diamond.adapter.HomePagerAdapter;
import com.zmy.diamond.base.MyBaseFragment;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.MessageEvent;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.PlatformBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页数据采集页面
 * Created by zhangmengyun on 2018/6/11.
 */
public class HomeFragment extends MyBaseFragment implements OnTabSelectListener, ViewPager.OnPageChangeListener {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tablayout)
    SlidingTabLayout tablayout;
    @BindView(R.id.tv_menu_count)
    TextView tv_menu_count;
    @BindView(R.id.tv_collect_setting)
    TextView tv_collect_setting;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    HomePagerAdapter mAdapter;
    //平台菜单是否显示
    public boolean isShowPlatformMenu = true;

    //是否采集中
    boolean isCollectIng = false;
    public List<PlatformBean> platformBeanList;


    @Override
    public int inflaterRootView() {
        return R.layout.fragment_home;
    }

    @Override
    public void initUI() {
        super.initUI();
        EventBus.getDefault().register(this);

        tv_collect_setting.setTypeface(MyUtlis.getTTF());
        platformBeanList = MyUtlis.getPlatformBeanList();
        for (int i = 0; i < platformBeanList.size(); i++) {
            mFragments.add(DataFragment.getInstance(platformBeanList.get(i)));
        }
        mAdapter = new HomePagerAdapter(mFragments, getFragmentManager());
        viewPager.setAdapter(mAdapter);

        String[] titles = AppConstant.Platform.NAME;

        //名称太短，拼接一下
        for (int i = 0; i < titles.length; i++) {
            titles[i] = "     " + titles[i] + "     ";
        }

        tablayout.setViewPager(viewPager, titles, getActivity(), mFragments);
//        tablayout.showDot(0);
//        tablayout.showDot(1);
//        tablayout.showDot(2);
//        tablayout.showDot(3);
//        tablayout.showDot(4);
//        tablayout.showDot(5);
//        tablayout.showDot(6);

//        tablayout.showMsg(3, 5);
//        tablayout.setMsgMargin(3, 0, 10);
//        MsgView rtv_2_3 = tablayout.getMsgView(3);
//        if (rtv_2_3 != null) {
//            rtv_2_3.setBackgroundColor(Color.parseColor("#6D8FB0"));
//        }
//        tablayout.showMsg(5, 5);
//        tablayout.setMsgMargin(5, 0, 10);
        tablayout.setOnTabSelectListener(this);
        viewPager.addOnPageChangeListener(this);
//        tablayout.setCurrentTab(0);
        //设置最后一个选中的平台索引
//        tablayout.setCurrentTab(MyUtlis.getLastSelectPlatformIndex());
        showPlatformImage();
//        setPlatformDataCount();

    }

    @Override
    public void initData() {
        //设置菜单点击事件
        menu.setOnMenuItemClickListener(new AnnularMenu.OnMenuItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position) {
                    case 4:
                        startCollect();
                        break;
                    case 3:
                        stopCollect();
                        break;
                    case 2:
                        dataClear();
                        break;
                    case 1:
                        dataExport();
                        break;
                }
                if (position != 0)
                    menu.getChildAt(position).setVisibility(View.GONE);


            }
        });


    }

    @BindView(R.id.menu)
    public AnnularMenu menu;

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        KeyboardUtils.unregisterSoftInputChangedListener(getActivity());
        super.onDestroy();

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.eventType == MessageEvent.UPDATE_HOME_DATA) {
            LogUtils.e("首页刷新数据");
            //重置所有平台数据
            for (int i = 0; i < mFragments.size(); i++) {
                DataFragment dataFragment = (DataFragment) mFragments.get(i);
                dataFragment.refreshData();
            }
//            dataAdapter.setNewData(new ArrayList<DataBean>());
        } else if (event.eventType == MessageEvent.UPDATE_LOCATION) {
            LogUtils.e("首页刷新定位数据=" + event.locationBean.getAddr());
        } else if (event.eventType == MessageEvent.COLLECT_COMPLETE) {
            isCollectIng = false;
        } else if (event.eventType == MessageEvent.HOME_MENU_VISIBILITY) {
            if (event.booleanValue) {
                showPlatformMenu();
            } else {
                hidePlatformMenu();
            }
        } else if (event.eventType == MessageEvent.UPDATE_PLATFORM_DATA_COUNT) {
            setPlatformDataCount();
        } else if (event.eventType == MessageEvent.COLLECT_ERROR) {
            isCollectIng = false;
        }
    }


    @Override
    public void addListeners() {
    }

    /**
     * 开始采集
     */
    public void startCollect() {
        if (isCollectIng) {
            MyUtlis.showShort(getActivity(), getString(R.string.hint_collect_ing));
            return;
        }
        String city = MyUtlis.getCollectCity();
        String key = MyUtlis.getCollectKey();


        if (TextUtils.isEmpty(city)) {
            MyUtlis.showShort(getActivity(), getString(R.string.hint_input_collect_city));
            return;
        }
        if (TextUtils.isEmpty(key)) {
            MyUtlis.showShort(getActivity(), getString(R.string.hint_input_collect_key));
            return;
        }

        hidePlatformMenu();
        isCollectIng = true;
        //获取当前平台id
        int currentItem = viewPager.getCurrentItem();
        DataFragment fragment = (DataFragment) mFragments.get(currentItem);
        fragment.startCollect(city, key);

    }

    /**
     * 停止采集
     */
    public void stopCollect() {
        if (!isCollectIng) {
            return;
        }
        int currentItem = viewPager.getCurrentItem();
        DataFragment fragment = (DataFragment) mFragments.get(currentItem);
        fragment.stopCollect();
    }

    /**
     * 数据清除
     */
    public void dataClear() {
        if (isCollectIng) {
            MyUtlis.showShort(getActivity(), getString(R.string.hint_collect_ing));
            return;
        }


        final int currentItem = viewPager.getCurrentItem();
        final PlatformBean bean = platformBeanList.get(currentItem);
        new AlertView(getString(R.string.hint_text), getString(R.string.hint_confirm_delete_platform_data, bean.name), null, new String[]{getString(R.string.hint_confirm)}, new String[]{getString(R.string.hint_cancel)}, getActivity(),
                AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                //0=好的
                if (position == 0) {
//                    MyUtlis.showShort(getActivity(), "清除数据:" + bean.name);
                    DataFragment fragment = (DataFragment) mFragments.get(currentItem);
                    fragment.clearData();
                }

            }
        }).show();


    }


    /**
     * 数据导出
     */
    public void dataExport() {
        if (isCollectIng) {
            MyUtlis.showShort(getActivity(), getString(R.string.hint_collect_ing));
            return;
        }

//        MyUtlis.showShort(getActivity(), "导出电话簿");
        final int currentItem = viewPager.getCurrentItem();
        final DataFragment fragment = (DataFragment) mFragments.get(currentItem);

        if (fragment.dataAdapter.getData().isEmpty()) {
            MyUtlis.showShort(getActivity(), getString(R.string.hint_no_data_2));
            return;
        }
        new AlertView(getString(R.string.hint_text), getString(R.string.hint_confirm_export_current_platform_data, fragment.bean.name, fragment.dataAdapter.getData().size()), null, new String[]{getString(R.string.hint_export_contact), getString(R.string.hint_export_excel)}, new String[]{getString(R.string.hint_cancel)}, getActivity(),
                AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                LogUtils.e(position);
                if (position == 0) {
                    //导出到手机通讯录
                    MyUtlis.addContacts(getActivity(), fragment.dataAdapter.getData());
                } else if (position == 1) {
                    //导出csv文件
                    //存储到哪里.
                    MyUtlis.exportCSVFile(getActivity(), fragment.bean, fragment.dataAdapter.getData());
                }

            }
        }).show();

    }


    @BindView(R.id.tv_platform_icon)
    TextView tv_platform_icon;

    @OnClick(R.id.tv_platform_icon)
    public void tv_platform_icon() {
        if (isShowPlatformMenu) {
            hidePlatformMenu(true);
        } else {
            showPlatformMenu(true);
        }
//        TestActivity.start(getContext());
    }


    @OnClick(R.id.tv_collect_setting)
    public void tv_collect_setting() {
        MyUtlis.clickEvent(AppConstant.CLICK.umeng_home_collect_setting);
        CollectSettingActivity.start(getActivity());
    }


    public void hidePlatformMenu() {
        hidePlatformMenu(false);
    }

    public void showPlatformMenu() {
        showPlatformMenu(false);
    }

    /**
     * 隐藏平台和菜单
     */
    public void hidePlatformMenu(boolean isClick) {
        LogUtils.e("hidePlatformMenu");
        if (isClick || !isCollectIng) {
            isShowPlatformMenu = false;
            tablayout.setVisibility(View.GONE);
        }
    }

    /**
     * 显示平台菜单
     */
    public void showPlatformMenu(boolean isClick) {
        LogUtils.e("showPlatformMenu");
        if (isClick || !isCollectIng) {
            isShowPlatformMenu = true;
            tablayout.setVisibility(View.VISIBLE);
        }

    }


    /**
     * 设置当前平台已采集的数据数量
     */
    public void setPlatformDataCount() {
        try {
            DataFragment dataFragment = (DataFragment) mFragments.get(viewPager.getCurrentItem());
            tv_menu_count.setText(String.valueOf(dataFragment.dataAdapter.getData().size()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示当前选择平台图片
     */

    public void showPlatformImage() {
        PlatformBean platformBean = platformBeanList.get(viewPager.getCurrentItem());
        tv_platform_icon.setText(platformBean.ttfStr);
        tv_platform_icon.setTypeface(MyUtlis.getTTF());
    }

    @Override
    public void onTabSelect(int position) {
        LogUtils.e("onTabSelect=" + position);


    }


    @Override
    public void onTabReselect(int position) {
        LogUtils.e("onTabReselect=" + position);

        //重复点击1下, 重复点击2下回底部
        try {
            DataFragment dataFragment = (DataFragment) mFragments.get(viewPager.getCurrentItem());

            if (MyUtlis.isFastDoubleClick()) {
                dataFragment.recyclerView_data.scrollToPosition(dataFragment.recyclerView_data.getAdapter().getItemCount() - 1);
            } else {
                dataFragment.recyclerView_data.scrollToPosition(0);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        LogUtils.e("onPageScrolled=" + position);
    }

    @Override
    public void onPageSelected(int position) {
        LogUtils.e("onPageSelected=" + position);
        showPlatformImage();
        setPlatformDataCount();
        MyUtlis.setLastSelectPlatformIndex(position);
        tablayout.hideMsg(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}