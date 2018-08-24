package com.zmy.diamond.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zmy.diamond.R;
import com.zmy.diamond.activity.AccountActionActivity;
import com.zmy.diamond.adapter.MyTabViewPagerAdapter;
import com.zmy.diamond.base.MyBaseFragment;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.MessageEvent;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.UserBean;
import com.zmy.diamond.utli.dao.DaoUtlis;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 交易页面
 * Created by zhangmengyun on 2018/6/11.
 */

public class TradingFragment extends MyBaseFragment implements OnTabSelectListener, ViewPager.OnPageChangeListener {


    @BindView(R.id.tv_more)
    TextView tv_more;
    private final String[] mTitles = {
            "　　我要买账号　　"
            , "　　 求购信息　　 "
    };

    @BindView(R.id.tabLayout)
    SlidingTabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    //是否显示了more菜单,默认false
    public boolean isShowMore;

    @Override
    public int inflaterRootView() {
        return R.layout.fragment_trading;
    }

    TradingDataFragment sellAccountDataFragment;

    List<Fragment> fragmentList;

    @Override
    public void initUI() {
        super.initUI();
        EventBus.getDefault().register(this);
        tv_more.setTypeface(MyUtlis.getTTF());

        fragmentList = new ArrayList<>();

        TradingDataFragment buyAccountDataFragment = TradingDataFragment.newInstance(AppConstant.DATA_TYPE_BUY_ACCOUNT);

        sellAccountDataFragment = TradingDataFragment.newInstance(AppConstant.DATA_TYPE_SELL_ACCOUNT);
        fragmentList.add(buyAccountDataFragment);
        fragmentList.add(sellAccountDataFragment);

        MyTabViewPagerAdapter viewPagerAdapter = new MyTabViewPagerAdapter(getContext(), getFragmentManager(), fragmentList, mTitles);
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setViewPager(viewPager);
        tabLayout.setOnTabSelectListener(this);
        viewPager.addOnPageChangeListener(this);
//        viewPager.setCurrentItem(0);  //初始化显示第一个页面
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

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

    public AlertView moreView;

    @OnClick(R.id.tv_more)
    public void tv_more() {
        isShowMore = true;
        if (null == moreView) {
            moreView = new AlertView(getString(R.string.hint_select), null, "取消", new String[]{}, new String[]{"我要买账号", "求购信息"}, getContext(),
                    AlertView.Style.ActionSheet, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    //0=发布需求 1=发布账户 -1=取消

                    if (0 == position) {
                        AccountActionActivity.startRelease(getContext(), AppConstant.DATA_TYPE_BUY_ACCOUNT);
                    } else if (1 == position) {
                        AccountActionActivity.startRelease(getContext(), AppConstant.DATA_TYPE_SELL_ACCOUNT);
                    }
                    moreView.dismiss();

                }
            }).setCancelable(true).setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(Object o) {
                    isShowMore = false;
                }
            });
        }
        moreView.show();

    }


    @Override
    public void onTabSelect(int position) {

    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 1 && null != sellAccountDataFragment) {
            sellAccountDataFragment.autoRefresh();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
