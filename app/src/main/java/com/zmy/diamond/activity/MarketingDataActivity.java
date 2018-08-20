package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zmy.diamond.R;
import com.zmy.diamond.adapter.HomeDataAdapter;
import com.zmy.diamond.base.MyBaseSwipeBackActivity;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.DataBean;
import com.zmy.diamond.utli.bean.PlatformBean;
import com.zmy.diamond.utli.dao.DaoUtlis;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 营销列表，数据详情页面
 * Created by zhangmengyun on 2018/6/26.
 */

public class MarketingDataActivity extends MyBaseSwipeBackActivity {

    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_menu_1)
    TextView tv_menu_1;
    @BindView(R.id.tv_menu_2)
    TextView tv_menu_2;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    int platformId;
    String key;
    String city;
    String collectId;
    String userId;


    HomeDataAdapter mAdapter;

    @Override
    public void initUI() {
        setContentView(R.layout.activity_marketing_data);
        super.initUI();
        tv_title.setText(getString(R.string.text_all_data));
        tv_back.setTypeface(MyUtlis.getTTF());
        tv_menu_1.setTypeface(MyUtlis.getTTF());
        tv_menu_2.setTypeface(MyUtlis.getTTF());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);


    }

    /**
     * 全部数据
     */
    List<DataBean> mAllData = new ArrayList<>();
    /**
     * 手机数据
     */
    List<DataBean> mPhoneData;
    /**
     * 固话数据
     */
    List<DataBean> mTelData;

    @Override
    public void initData() {
        key = getIntent().getStringExtra(AppConstant.ExtraKey.KEY);

        city = getIntent().getStringExtra(AppConstant.ExtraKey.CITY);

        tv_title.setText(city.replace("市", "") + key);

        collectId = getIntent().getStringExtra(AppConstant.ExtraKey.COLLECT_ID);
        userId = getIntent().getStringExtra(AppConstant.ExtraKey.USER_ID);
        platformId = getIntent().getIntExtra(AppConstant.ExtraKey.PLATFORM_ID, 0);

        mAllData = DaoUtlis.getDataByCityKey(platformId, key, city, userId);
        mAdapter = new HomeDataAdapter(mAllData);
        mAdapter.setEmptyView(MyUtlis.getEmptyView(this, "暂无数据"));
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mRecyclerView.setAdapter(mAdapter);

    }


    AlertView menuView1;
    AlertView menuView2;


    @OnClick(R.id.tv_menu_1)
    public void tv_menu_1() {


        if (null == menuView1) {
            menuView1 = new AlertView(MarketingDataActivity.this.getString(R.string.hint_select), null, "取消", new String[]{}, new String[]{"导出数据", "群发短信", "删除数据"}, MarketingDataActivity.this,
                    AlertView.Style.ActionSheet, new com.bigkoo.alertview.OnItemClickListener() {
                @Override
                public void onItemClick(Object o, final int position) {
                    menuView1.dismissImmediately();
                    //0=发布需求 1=发布账户 -1=取消
                    if (0 == position) {
                        dataExport();
                    } else if (1 == position) {
                        ToastUtils.showShort("群发短信，功能开发中");
                    } else if (2 == position) {
                        dataClear();
                    }
                }
            }).setCancelable(true);
        }
        menuView1.show();


    }

    /**
     * 数据删除
     */
    private void dataClear() {

        final List<DataBean> currentData = getCurrentData();
        if (currentData == null || currentData.isEmpty()) {
            MyUtlis.showShort(this, getString(R.string.hint_no_data_2));
            return;
        }


        new AlertView(getString(R.string.hint_text), getString(R.string.hint_confirm_delete_list_current_data), null, new String[]{getString(R.string.hint_confirm)}, new String[]{getString(R.string.hint_cancel)}, MarketingDataActivity.this,
                AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                //0=好的
                if (position == 0) {
                    DaoUtlis.deleteAllData(currentData);
                    DaoUtlis.updateCollectRecordDataCount(collectId, userId, currentData.size());
                    clearCurrentData();
                }

            }
        }).show();


    }


    /**
     * 删除当前数据 并更新UI
     *
     * @return
     */
    public void clearCurrentData() {

        switch (mCurrentFilterType) {
            case AppConstant.SHOW_TYPE_ALL:
                mAllData.clear();
                mAdapter.setNewData(mAllData);
                break;
            case AppConstant.SHOW_TYPE_PHONE:
                mPhoneData.clear();
                mAdapter.setNewData(mPhoneData);
                break;
            case AppConstant.SHOW_TYPE_TEL:
                mTelData.clear();
                mAdapter.setNewData(mTelData);
                break;
        }


    }

    /**
     * 获取当前列表数据
     *
     * @return
     */
    public List<DataBean> getCurrentData() {

        switch (mCurrentFilterType) {
            case 0:
                return mAllData;
            case 1:
                return mPhoneData;
            case 2:
                return mTelData;
        }

        return new ArrayList<>();
    }

    /**
     * 数据导出
     */
    public void dataExport() {


        final List<DataBean> currentData = getCurrentData();


        if (currentData == null || currentData.isEmpty()) {
            MyUtlis.showShort(this, getString(R.string.hint_no_data_2));
            return;
        }


        new AlertView(getString(R.string.hint_text), getString(R.string.hint_confirm_export_current_platform_data, currentData.get(0).name, currentData.size()), null, new String[]{getString(R.string.hint_export_contact), getString(R.string.hint_export_excel)}, new String[]{getString(R.string.hint_cancel)}, this,
                AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                LogUtils.e(position);
                if (position == 0) {
                    //导出到手机通讯录
                    MyUtlis.addContacts(MarketingDataActivity.this, currentData);
                } else if (position == 1) {
                    //导出csv文件
                    //存储到哪里.
                    List<PlatformBean> platformBeanList = MyUtlis.getPlatformBeanList();
                    PlatformBean platformBean = null;
                    for (PlatformBean bean : platformBeanList) {
                        if (bean.platformId == currentData.get(0).getPlatformId()) {
                            platformBean = bean;
                            break;
                        }
                    }
                    if (null == platformBean) {
                        platformBean = new PlatformBean();
                        platformBean.platformId = AppConstant.Platform.PLATFORM_ID[0];
                        platformBean.name = AppConstant.Platform.NAME[0];
                    }
                    MyUtlis.exportCSVFile(MarketingDataActivity.this, platformBean, currentData);

                }

            }
        }).show();
    }


    @OnClick(R.id.tv_menu_2)
    public void tv_menu_2() {
        if (null == menuView2) {
            menuView2 = new AlertView(this.getString(R.string.hint_select), null, "取消", new String[]{}, new String[]{"显示全部数据", "只显示手机", "只显示固话"}, this,
                    AlertView.Style.ActionSheet, new com.bigkoo.alertview.OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    //0=发布需求 1=发布账户 -1=取消
                    if (0 == position) {
                        showFilterData(AppConstant.SHOW_TYPE_ALL);
                    } else if (1 == position) {
                        showFilterData(AppConstant.SHOW_TYPE_PHONE);
                    } else if (2 == position) {
                        showFilterData(AppConstant.SHOW_TYPE_TEL);
                    }

                }
            }).setCancelable(true);
        }
        menuView2.show();

    }


    /**
     * 当前过滤类型
     * 0=全部 默认
     * 1=手机
     * 2=固话
     */
    public int mCurrentFilterType = AppConstant.SHOW_TYPE_ALL;


    /**
     * 过滤数据后显示
     * 如果当前显示的是对应过滤数据，则不过滤数据，滚动到顶部，否则对数据进行过滤后，滚动到顶部
     */
    private void showFilterData(int filterType) {

        if (mCurrentFilterType == filterType) {
            mRecyclerView.scrollToPosition(0);
            return;
        }
        mCurrentFilterType = filterType;
        switch (filterType) {
            case AppConstant.SHOW_TYPE_ALL://全部
//                tv_title.setText(getString(R.string.text_all_data));
                mAdapter.setPhoneTelShowType(AppConstant.SHOW_TYPE_ALL);
                mAdapter.setNewData(mAllData);
                mRecyclerView.scrollToPosition(0);
                break;
            case AppConstant.SHOW_TYPE_PHONE://手机
                if (null == mPhoneData) {
                    mPhoneData = new ArrayList<>();
                    for (int i = 0; i < mAllData.size(); i++) {
                        DataBean dataBean = mAllData.get(i);
                        if (!TextUtils.isEmpty(dataBean.getPhone())) {
                            mPhoneData.add(dataBean);
                        }
                    }
                }
//                tv_title.setText(getString(R.string.text_phone_data));
                mAdapter.setPhoneTelShowType(AppConstant.SHOW_TYPE_PHONE);
                mAdapter.setNewData(mPhoneData);
                mRecyclerView.scrollToPosition(0);
                break;
            case AppConstant.SHOW_TYPE_TEL://固话
                if (null == mTelData) {
                    mTelData = new ArrayList<>();
                    for (int i = 0; i < mAllData.size(); i++) {
                        DataBean dataBean = mAllData.get(i);
                        if (!TextUtils.isEmpty(dataBean.getTel())) {
                            mTelData.add(dataBean);
                        }
                    }
                }
//                tv_title.setText(getString(R.string.text_tel_data));
                mAdapter.setPhoneTelShowType(AppConstant.SHOW_TYPE_TEL);
                mAdapter.setNewData(mTelData);
                mRecyclerView.scrollToPosition(0);
                break;


        }

    }


    /**
     * @param context
     * @param collectId
     * @param platformId
     * @param userId
     * @param key
     * @param city
     */
    public static void start(Context context, String collectId, int platformId, String userId, String key, String city) {
        Intent intent = new Intent(context, MarketingDataActivity.class);
        intent.putExtra(AppConstant.ExtraKey.KEY, key);
        intent.putExtra(AppConstant.ExtraKey.COLLECT_ID, collectId);
        intent.putExtra(AppConstant.ExtraKey.PLATFORM_ID, platformId);
        intent.putExtra(AppConstant.ExtraKey.USER_ID, userId);
        intent.putExtra(AppConstant.ExtraKey.CITY, city);
        context.startActivity(intent);
    }

    @OnClick(R.id.tv_back)
    public void tv_back() {
        ActivityUtils.finishActivity(this, true);
    }
}
