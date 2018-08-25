package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zmy.diamond.R;
import com.zmy.diamond.adapter.MarketingDataSelectAdapter;
import com.zmy.diamond.base.MyBaseSwipeBackActivity;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.MessageEvent;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.DataBean;
import com.zmy.diamond.utli.bean.MarketingDataSelectGroupBean;
import com.zmy.diamond.utli.bean.MarketingDataSelectSingleBean;
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
 * 短信群发
 * Created by zhangmengyun on 2018/6/13.
 */

public class SMSBatchActivity extends MyBaseSwipeBackActivity {
    MarketingDataSelectAdapter mAdapter;
    UserBean user;
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    @Override
    public void initUI() {
        setContentView(R.layout.activity_sms_batch);
        super.initUI();
        EventBus.getDefault().register(this);
        tv_back.setTypeface(MyUtlis.getTTF());
        tv_title.setText(getString(R.string.title_sms_batch));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        mAdapter = new MarketingDataSelectAdapter(getdefultData(), MarketingDataSelectAdapter.TYPE_DELETE);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.setEmptyView(MyUtlis.getEmptyView(this, "暂无数据，请添加数据"));
        recyclerView.setAdapter(mAdapter);
    }


    /**
     * 如果有传 数据进来，获取一下设置默认数据
     *
     * @return
     */
    private ArrayList<MultiItemEntity> getdefultData() {

        ArrayList<MultiItemEntity> list = new ArrayList<>();
        String userId = getIntent().getStringExtra(AppConstant.ExtraKey.USER_ID);
        String key = getIntent().getStringExtra(AppConstant.ExtraKey.KEY);
        String city = getIntent().getStringExtra(AppConstant.ExtraKey.CITY);
        int platformId = getIntent().getIntExtra(AppConstant.ExtraKey.PLATFORM_ID, 0);
        if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(key) && !TextUtils.isEmpty(city)) {
            //获取默认数据 转换格式
            List<DataBean> dataList = DaoUtlis.getDataByCityKey(platformId, key, city, userId);
            if (null != dataList && dataList.size() > 0) {
                MarketingDataSelectGroupBean groupBean = new MarketingDataSelectGroupBean();
                groupBean.name = city.replace("市", "") + key;
                for (DataBean dataBean : dataList) {
                    //过滤不是手机的数据
                    if (TextUtils.isEmpty(dataBean.getPhone())) {
                        continue;
                    }
                    MarketingDataSelectSingleBean singleBean = new MarketingDataSelectSingleBean();
                    singleBean.name = dataBean.getName();
                    singleBean.phone = dataBean.getPhone();
                    singleBean.address = dataBean.getAddress();
                    groupBean.addSubItem(singleBean);

                }
                if (null != groupBean.getSubItems() && groupBean.getSubItems().size() > 0) {
                    groupBean.size = groupBean.getSubItems().size();
                    list.add(groupBean);
                }
            }
        }

        return list;

    }


    @Override
    public void initData() {


        user = DaoUtlis.getCurrentLoginUser();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

        if (event.eventType == MessageEvent.SELECT_MARKETING_DATA_COMPLETE) {
            //选择数据完成
            if (null != event.objectValue) {
                List<MultiItemEntity> list = (List<MultiItemEntity>) event.objectValue;
                mAdapter.setNewData(list);
            }
        }
    }

    @OnClick(R.id.btn_add)
    public void btn_add() {
        MarketingDataSelectActivity.start(this, AppConstant.SHOW_TYPE_PHONE, "已选择的数据");
    }

    @OnClick(R.id.btn_send)
    public void btn_send() {
        ToastUtils.showShort("发送");
    }

    @OnClick(R.id.tv_sms_template)
    public void tv_sms_template() {
        ToastUtils.showShort("短信模板");
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, SMSBatchActivity.class);

        context.startActivity(intent);
    }


    public static void start(Context context, String userId, int platformId, String key, String city) {
        Intent intent = new Intent(context, SMSBatchActivity.class);
        intent.putExtra(AppConstant.ExtraKey.KEY, key);
        intent.putExtra(AppConstant.ExtraKey.USER_ID, userId);
        intent.putExtra(AppConstant.ExtraKey.PLATFORM_ID, platformId);
        intent.putExtra(AppConstant.ExtraKey.CITY, city);
        context.startActivity(intent);

    }


    @OnClick(R.id.tv_back)
    public void tv_back() {
        ActivityUtils.finishActivity(this, true);
    }


}
