package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.ThreadUtils;
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

    @BindView(R.id.tv_ttf_add)
    TextView tv_ttf_add;

    @BindView(R.id.edit_content)
    EditText edit_content;

    @Override
    public void initUI() {
        setContentView(R.layout.activity_sms_batch);
        super.initUI();
        EventBus.getDefault().register(this);
        user = DaoUtlis.getCurrentLoginUser();
        tv_back.setTypeface(MyUtlis.getTTF());
        tv_ttf_add.setTypeface(MyUtlis.getTTF());
        tv_title.setText(getString(R.string.title_sms_batch));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        mAdapter = new MarketingDataSelectAdapter(user, getdefultData(), MarketingDataSelectAdapter.TYPE_DELETE);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.setEmptyView(MyUtlis.getEmptyView(this, "暂无接收者"));
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
                    singleBean.phone = MyUtlis.getPhoneByVip(dataBean.getPhone(), user.getGrade());
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
        mSmsManager = SmsManager.getDefault();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        KeyboardUtils.hideSoftInput(this);
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

    @OnClick(R.id.tv_ttf_add)
    public void tv_ttf_add() {
        MarketingDataSelectActivity.start(this, AppConstant.SHOW_TYPE_PHONE, "已选择的数据");
    }

    @OnClick(R.id.btn_send)
    public void btn_send() {
//        ToastUtils.showShort("发送");

        final List<String> receiverPhone = getReceiverPhone();
        if (null == receiverPhone || receiverPhone.isEmpty()) {
            ToastUtils.showShort("接收者不能为空");
            return;
        }
        final String smsContent = edit_content.getText().toString();
        if (TextUtils.isEmpty(smsContent)) {
            //短信内容为空
            ToastUtils.showShort("短信内容不能为空");
            return;
        }


        new AlertView("选择发送模式", null, "取消", new String[]{}, new String[]{"本机发送", "平台短信通道"}, this,
                AlertView.Style.ActionSheet, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {

                if (0 == position) {
                    sendSMS(receiverPhone, smsContent);
                } else if (1 == position) {
                    ToastUtils.showShort("暂未开放");
                }

            }
        }).show();
    }


    /**
     * 获取接收者手机号
     *
     * @return
     */
    public List<String> getReceiverPhone() {
        List<MultiItemEntity> data = mAdapter.getData();

        List<String> phoneList = new ArrayList<>();
        for (MultiItemEntity entity : data) {

            if (entity.getItemType() == MarketingDataSelectAdapter.TYPE_LEVEL_GROUP) {

                MarketingDataSelectGroupBean groupBean = (MarketingDataSelectGroupBean) entity;
                List<MarketingDataSelectSingleBean> items = groupBean.getSubItems();
                if (null != items) {

                    for (MarketingDataSelectSingleBean singleBean : items) {
                        if (!TextUtils.isEmpty(singleBean.phone)) {
                            phoneList.add(singleBean.phone);
                        }
                    }
                }
            }
        }
        return phoneList;
    }


    public SmsManager mSmsManager;

    /**
     * 发送短信
     */
    private void sendSMS(final List<String> phoneList, final String smsContent) {

        if (!PhoneUtils.isSimCardReady()) {
            //没有sim卡
            ToastUtils.showShort("请先插入SIM卡，否则无法发送短信");
            return;
        }

        PermissionUtils.permission(PermissionConstants.SMS, PermissionConstants.PHONE)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(final ShouldRequest shouldRequest) {
                        MyUtlis.showRationaleDialog(shouldRequest);
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {


                        new AlertView("提示", "短信发送中，不可取消，\n确定信息无误，开始发送短信吗？", null, new String[]{"确定发送"}, new String[]{"取消"}, SMSBatchActivity.this,
                                AlertView.Style.Alert, new OnItemClickListener() {
                            @Override
                            public void onItemClick(Object o, int position) {
                                if (position == 0) {
                                    showLoading(false, "正在发送...");
                                    ThreadUtils.executeByFixed(AppConstant.THREAD_SIZE, new ThreadUtils.SimpleTask<Boolean>() {


                                        @Override
                                        public Boolean doInBackground() {

                                            for (String phone : phoneList) {

                                                try {
                                                    //每条短信延时100毫秒
                                                    Thread.sleep(100);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                // 创建一个PendingIntent对象
                                                try {
                                                    LogUtils.e(phone);
                                                    PhoneUtils.sendSmsSilent(phone, smsContent);

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    LogUtils.e("短信发送失败 =" + phone);
                                                    continue;
                                                }
                                            }
                                            return true;
                                        }

                                        @Override
                                        public void onSuccess(Boolean result) {
                                            hideLoading();
                                            if (result) {
                                                ToastUtils.showShort("发送完成");
                                                ActivityUtils.finishActivity(SMSBatchActivity.this);
                                            } else {
                                                ToastUtils.showShort("发送失败");
                                            }
                                        }


                                    });

                                }
                            }
                        }).show();


                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever,
                                         List<String> permissionsDenied) {
                        if (!permissionsDeniedForever.isEmpty()) {
                            MyUtlis.showOpenAppSettingDialog();
                        }
                    }
                })
                .request();


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
