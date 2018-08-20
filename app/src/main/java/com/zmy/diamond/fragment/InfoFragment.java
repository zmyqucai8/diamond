package com.zmy.diamond.fragment;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.brioal.adtextviewlib.view.ADTextView;
import com.brioal.adtextviewlib.view.OnAdChangeListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zmy.diamond.MainActivity;
import com.zmy.diamond.R;
import com.zmy.diamond.activity.WebViewActivity;
import com.zmy.diamond.adapter.InfoAdapter;
import com.zmy.diamond.adapter.InfoMenuAdapter;
import com.zmy.diamond.base.MyBaseFragment;
import com.zmy.diamond.utli.GlideImageLoader;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.InfoBannerBean;
import com.zmy.diamond.utli.bean.InfoBean;
import com.zmy.diamond.utli.bean.InfoDataBean;
import com.zmy.diamond.utli.bean.InfoMenuBean;
import com.zmy.diamond.utli.bean.InfoNoticeBean;
import com.zmy.diamond.utli.dao.DaoUtlis;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

/**
 * 信息咨询页面
 * Created by zhangmengyun on 2018/6/11.
 */

public class InfoFragment extends MyBaseFragment {


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipe_layout;
    //dataList Adapter
    InfoAdapter mAdapter;
    //
    int mPages;
    InfoMenuAdapter mMenuAdapter;
    public Banner mBanner;
    int mDistanceY;
    InfoBean mInfoBean;
    @BindView(R.id.tv_title)
    TextView tv_title;


//    @BindView(R.id.statusBarView)
//    View statusBarView;

    @Override
    public int inflaterRootView() {
        return R.layout.fragment_info;
    }

    MainActivity mainActivity;

    @Override
    public void initUI() {
        super.initUI();

        mainActivity = (MainActivity) getActivity();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        swipe_layout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //滑动的距离
                mDistanceY += dy;
                //toolbar的高度
                int toolbarHeight = tv_title.getBottom() * 3;
                //当滑动的距离 <= toolbar高度的时候，改变Toolbar背景色的透明度，达到渐变的效果
                if (mDistanceY <= toolbarHeight) {
                    float scale = (float) mDistanceY / toolbarHeight;
                    float alpha = scale * 255;
//                    tv_title.setBackgroundColor(Color.argb((int) alpha, 128, 0, 0));
                    tv_title.setBackgroundColor(Color.argb((int) alpha, 18, 150, 219));
                    tv_title.setTextColor(Color.argb((int) alpha, 255, 255, 255));
//                    statusBarView.setBackgroundColor(Color.argb((int) alpha, 18, 150, 219));
                } else {
                    //上述虽然判断了滑动距离与toolbar高度相等的情况，但是实际测试时发现，标题栏的背景色
                    //很少能达到完全不透明的情况，所以这里又判断了滑动距离大于toolbar高度的情况，
                    //将标题栏的颜色设置为完全不透明状态
                    tv_title.setBackgroundResource(R.color.color_logo);
//                    statusBarView.setBackgroundResource(R.color.color_logo);
                    tv_title.setTextColor(Color.WHITE);
                }

            }
        });


    }


    @Override
    public void initData() {
//        获取本地数据
        mInfoBean = DaoUtlis.getInfoBean();
        if (null == mInfoBean) {
            LogUtils.e("infoFragment 获取的infoBean=空");
            mInfoBean = getInfoData();
        }
        if (null == mInfoBean) {
            return;
        }
        mAdapter = new InfoAdapter(getContext(), mInfoBean.getInfoDataList());
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.setEmptyView(MyUtlis.getEmptyView(getContext(), getString(R.string.hint_no_data_2)));
        mAdapter.setHeaderAndEmpty(true);
        //1.添加banner 头布局
        //2.添加menu 头布局
        //3.添加notice 头布局
        mAdapter.addHeaderView(getBannerHead());
        mAdapter.addHeaderView(getMenuHead());
        mAdapter.addHeaderView(getNotiteHead());
        setHeadData(mInfoBean);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 设置头部数据
     *
     * @param infoBean
     */
    private void setHeadData(InfoBean infoBean) {
        setMenuHeadData(infoBean.getInfoMenuList());
        setNotiteHeadData(infoBean.getInfoNoticeList());
        setBannerHeadData(infoBean.getInfoBannerList());
    }

    /**
     * 设置公告 data
     *
     * @param infoNoticeList
     */
    private void setNotiteHeadData(List<InfoNoticeBean> infoNoticeList) {
        ArrayList<String> notices = new ArrayList<>();
        if (null != infoNoticeList && !infoNoticeList.isEmpty()) {
            for (int i = 0; i < infoNoticeList.size(); i++) {
                notices.add(infoNoticeList.get(i).title);
            }
        }
        mADTextView.setInterval(3000);
        mADTextView.removeAllViews();
        mADTextView.init(notices, new OnAdChangeListener() {
            @Override
            public void DiyTextView(TextView textView, final int index) {
                textView.setTextSize(13);
                textView.setEllipsize(TextUtils.TruncateAt.END);
                textView.setMaxLines(1);
                textView.setSingleLine();
                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_text));
//                SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText());
//                builder.setSpan(new ForegroundColorSpan(Color.RED), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                textView.setText(builder);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            WebViewActivity.start(mainActivity, mInfoBean.getInfoNoticeList().get(index).detailsUrl);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        });

    }


    /**
     * 设置menu data
     *
     * @param infoMenuList
     */
    private void setMenuHeadData(List<InfoMenuBean> infoMenuList) {
        mMenuAdapter.setNewData(infoMenuList);
    }


    /**
     * 设置banner data
     *
     * @param infoBannerBeanList
     */
    private void setBannerHeadData(List<InfoBannerBean> infoBannerBeanList) {
        List<String> titles = new ArrayList<>();
        List<String> images = new ArrayList<>();

        if (null != infoBannerBeanList) {

            for (int i = 0; i < infoBannerBeanList.size(); i++) {
                titles.add(infoBannerBeanList.get(i).title);
                images.add(infoBannerBeanList.get(i).imageUrl);
            }

        }


        mBanner.stopAutoPlay();

        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(images);
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.Default);
        //设置标题集合（当banner样式有显示title时）
        mBanner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
//        mBanner.start();
        mBanner.startAutoPlay();
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            if (null != mBanner) {
                mBanner.start();
            }
            LogUtils.e(" infofragment  onStart");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            //结束轮播
            if (null != mBanner) {
                mBanner.stopAutoPlay();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        LogUtils.e(" infofragment  onStop");
    }

    ADTextView mADTextView;




    /**
     * 获取公告头部
     *
     * @param
     * @return
     */
    private View getNotiteHead() {
        View mNoticeHead = View.inflate(getContext(), R.layout.view_head_notice, null);
        TextView tv_ttf_notice = (TextView) mNoticeHead.findViewById(R.id.tv_ttf_notice);
        tv_ttf_notice.setTypeface(MyUtlis.getTTF());
        //监听点击事件
        mADTextView = mNoticeHead.findViewById(R.id.ad_textview);
        return mNoticeHead;

    }

    /**
     * 获取菜单头部
     *
     * @param
     * @return
     */
    private View getMenuHead() {
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setBackgroundColor(Color.WHITE);
        RecyclerView mMenuRecyclerView = new RecyclerView(mainActivity);
        mMenuAdapter = new InfoMenuAdapter(new ArrayList<InfoMenuBean>());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 5);
        mMenuRecyclerView.setLayoutManager(gridLayoutManager);
        mMenuAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mMenuRecyclerView.setAdapter(mMenuAdapter);
        frameLayout.addView(mMenuRecyclerView);
        return frameLayout;
    }


    /**
     * 获取banner头部
     *
     * @return
     */
    private View getBannerHead() {
        View view_head_banner = View.inflate(getContext(), R.layout.view_head_banner, null);
        mBanner = (Banner) view_head_banner.findViewById(R.id.banner);
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

                try {
                    WebViewActivity.start(getContext(), mInfoBean.getInfoBannerList().get(position).getDetailsUrl());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return view_head_banner;
    }


    @Override
    public void addListeners() {
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPages = 1;
                refreshData();
            }
        });
    }


    /**
     * 刷新数据
     */
    public void refreshData() {
        MyUtlis.getInfoData(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                if (null != mBanner) {
                    mBanner.stopAutoPlay();
                }
                mInfoBean = DaoUtlis.getInfoBean();
                mAdapter.setNewData(mInfoBean.getInfoDataList());
                setHeadData(mInfoBean);
                swipe_layout.setRefreshing(false);
            }

            @Override
            public void onError(Response<String> response) {
                swipe_layout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mADTextView) {
            mADTextView = null;
        }
    }

    /**
     * 获取info测试数据
     *
     * @return
     */
    public InfoBean getInfoData() {
        int bannerCount = new Random().nextInt(5) + 3;
        int noticeCount = new Random().nextInt(10) + 2;
        int infoDataCount = new Random().nextInt(30) + 10;
        int menuCount = new Random().nextInt(1000) % 3 == 0 ? 5 : 10;

        DaoUtlis.deleteInfoBean();
        String loginUserId = MyUtlis.getLoginUserId();
        //1.广告图数据
        List<InfoBannerBean> infoBannerBeanList = new ArrayList<>();
        for (int a = 0; a < bannerCount; a++) {
            InfoBannerBean bannerBean = new InfoBannerBean();
            bannerBean.bannerId = String.valueOf(a);
            bannerBean.actionType = 1;
            bannerBean.title = "Banner 标题" + a;
            bannerBean.content = "Banner 内容" + a;
            bannerBean.detailsUrl = "http://www.baidu.com";
            bannerBean.userId = loginUserId;
            bannerBean.imageUrl = "https://ss3.baidu.com/-rVXeDTa2gU2pMbgoY3K/it/u=1361458793,2616941158&fm=202&src=780&mola=new&crop=v1";
            infoBannerBeanList.add(bannerBean);
        }
        DaoUtlis.addBannerBean(infoBannerBeanList);


        //2.菜单数据
        List<InfoMenuBean> infoMenuBeanList = new ArrayList<>();
        for (int a = 0; a < menuCount; a++) {
            InfoMenuBean bean = new InfoMenuBean();
            bean.menuId = String.valueOf(a);
            bean.actionType = 1;
            bean.title = "menu" + a;
//            bean.content = "Banner 内容" + a;
            bean.detailsUrl = "http://www.baidu.com";
            bean.userId = loginUserId;
            bean.imageUrl = "https://ss3.baidu.com/-rVXeDTa2gU2pMbgoY3K/it/u=1361458793,2616941158&fm=202&src=780&mola=new&crop=v1";
            infoMenuBeanList.add(bean);
        }
        DaoUtlis.addInfoMenuBean(infoMenuBeanList);
        //3.公告数据
        List<InfoNoticeBean> infoNoticeBeanList = new ArrayList<>();
        for (int a = 0; a < noticeCount; a++) {
            InfoNoticeBean bean = new InfoNoticeBean();
            bean.noticeId = String.valueOf(a);
            bean.actionType = 1;
            bean.title = "公告标题" + a;
            bean.content = "公告内容" + a;
            bean.detailsUrl = "http://www.baidu.com";
            bean.userId = loginUserId;
            infoNoticeBeanList.add(bean);
        }
        DaoUtlis.addInfoNoticeBean(infoNoticeBeanList);
        //3.infodata数据
        List<InfoDataBean> infoDataBeanList = new ArrayList<>();
        for (int a = 0; a < infoDataCount; a++) {
            InfoDataBean bean = new InfoDataBean();
            bean.infoDataId = String.valueOf(a);
            bean.actionType = 1;
            bean.title = "数据标题" + a;
            bean.content = "数据内容数据内容数据内容数据内容数据内容数据内容数据内容数据内容数据内容数据内容" + a;
            bean.detailsUrl = "http://www.baidu.com";
            bean.userId = loginUserId;
            infoDataBeanList.add(bean);
        }
        DaoUtlis.addInfoDataBean(infoDataBeanList);
        InfoBean infoBean = new InfoBean();
        infoBean.userId = loginUserId;
        infoBean.updateTime = TimeUtils.getNowMills();
        DaoUtlis.addInfoBean(infoBean);
        return DaoUtlis.getInfoBean();
    }
}
