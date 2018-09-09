package com.zmy.diamond;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zmy.diamond.base.BaseApp;
import com.zmy.diamond.base.MyBaseActivity;
import com.zmy.diamond.fragment.HomeFragment;
import com.zmy.diamond.fragment.MarketingFragment;
import com.zmy.diamond.fragment.MeFragment;
import com.zmy.diamond.fragment.TradingFragment;
import com.zmy.diamond.utli.ApiUtlis;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.UpdateAppUtlis;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 主界面
 */
public class MainActivity extends MyBaseActivity implements OnTabSelectListener {

    private String[] mTitles = {"首页", "营销", "交易", "我的"};
    private int[] mIconUnselectIds = {
            R.mipmap.tab_home_unselect, R.mipmap.tab_yx_unselect, R.mipmap.tab_jy_unselect,
            R.mipmap.tab_contact_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.tab_home_select, R.mipmap.tab_yx_select, R.mipmap.tab_jy_select,
            R.mipmap.tab_contact_select};


    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    ArrayList<Fragment> fragments;
    MeFragment meFragment;
    //    InfoFragment infoFragment;
    @BindView(R.id.tablayout)
    CommonTabLayout tablayout;

    @BindView(R.id.view_bg)
    public View view_bg;


    private void initTab() {

        HomeFragment homeFragment;
        homeFragment = new HomeFragment();
        MarketingFragment marketingFragment = new MarketingFragment();
        TradingFragment tradingFragment = new TradingFragment();


//        infoFragment = new InfoFragment();
        meFragment = new MeFragment();
        fragments = new ArrayList<>();
        fragments.add(homeFragment);
        fragments.add(marketingFragment);
        fragments.add(tradingFragment);
//        fragments.add(infoFragment);
        fragments.add(meFragment);
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        tablayout.setTabData(mTabEntities, this, R.id.framelayout, fragments);

//        tablayout.showMsg(0, 99);
        tablayout.showDot(1);
        tablayout.setOnTabSelectListener(this);
    }


    @Override
    public void initUI() {
        setContentView(R.layout.activity_main);
        super.initUI();
        initTab();
        if (AppConstant.LOG)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showShort("登录成功 userId=" + MyUtlis.getLoginUserId());
                }
            }, 200);

    }


    @Override
    public void initData() {
        ApiUtlis.getSystemTime(this, MyUtlis.getToken());
        UpdateAppUtlis.checkAppUpdate(this);
        if (AppConstant.isExperienceMode) {
            BaseApp.getInstance().startUseTiming();
        }


    }

    @Override
    public void addListeners() {
        //Test start
//        TestUtlis.test();
        if (AppConstant.DEBUG) {
//            ApiUtlis.getMapKey(this, MyUtlis.getToken(), AppConstant.MAP_KEY_TYPE_GAODE);
//            ApiUtlis.getMapKey(this, MyUtlis.getToken(), AppConstant.MAP_KEY_TYPE_BAIDU);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        ActivityUtils.startActivity(intent, R.anim.a5, R.anim.a3);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        int currentTab = tablayout.getCurrentTab();
        if (currentTab == 0) {
            //首页并且菜单显示的话,按返回键,先隐藏菜单,不显示的话,直接返回桌面
            HomeFragment homeFragment = (HomeFragment) fragments.get(0);
            if (homeFragment.isShowPlatformMenu) {
                homeFragment.hidePlatformMenu(true);
                return;
            }
        } else if (currentTab == 1) {
            MarketingFragment marketingFragment = (MarketingFragment) fragments.get(1);
            if (marketingFragment.mAdapter.isShowMore && null != marketingFragment.mAdapter.moreView) {
                marketingFragment.mAdapter.moreView.dismiss();
                return;
            }
        } else if (currentTab == 2) {
            //交易页面 并且more显示的话,按返回键,先隐藏more,不显示的话,直接返回桌面
            TradingFragment tradingFragment = (TradingFragment) fragments.get(2);
            if (tradingFragment.isShowMore && null != tradingFragment.moreView) {
                tradingFragment.moreView.dismiss();
                return;
            }
        }
        ActivityUtils.startHomeActivity();
    }

    @Override
    public void onTabSelect(int position) {
        //tab切换, 如果是我的页面判断后更新签到状态
        if (position == fragments.size() - 1 && null != meFragment) {
            meFragment.updateSignState();
        }


        //更新状态栏
//        if (position == 3) {
        //咨询页面不要状态栏
//            mImmersionBar.transparentStatusBar().removeSupportAllView().init();
//            statusBarView.setVisibility(View.GONE);
//        } else {

//            mImmersionBar.statusBarView(findViewById(R.id.statusBarView)).init();
//            statusBarView.setVisibility(View.VISIBLE);
//            statusBarView.setBackgroundResource(R.color.color_logo);
//        }
//        KeyboardUtils.hideSoftInput(this);
    }

    @Override
    public void onTabReselect(int position) {

    }
}
