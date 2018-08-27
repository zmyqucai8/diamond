package com.zmy.diamond.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.zmy.diamond.R;
import com.zmy.diamond.activity.MyTradingActivity;
import com.zmy.diamond.activity.OpinionActivity;
import com.zmy.diamond.activity.SMSBatchActivity;
import com.zmy.diamond.activity.SettingActivity;
import com.zmy.diamond.activity.VipActivity;
import com.zmy.diamond.activity.WalletActivity;
import com.zmy.diamond.base.MyBaseFragment;
import com.zmy.diamond.utli.ApiUtlis;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.JsonCallBack;
import com.zmy.diamond.utli.MessageEvent;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.LoginResponseBean;
import com.zmy.diamond.utli.bean.UserBean;
import com.zmy.diamond.utli.dao.DaoUtlis;
import com.zmy.diamond.utli.view.VipInfoPop;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的页面
 * Created by zhangmengyun on 2018/6/11.
 */

public class MeFragment extends MyBaseFragment {

    @BindView(R.id.tv_ttf_sign)
    TextView tv_ttf_sign;
    @BindView(R.id.tv_ttf_vip)
    TextView tv_ttf_vip;
    @BindView(R.id.tv_ttf_service)
    TextView tv_ttf_service;
    @BindView(R.id.tv_ttf_feedback)
    TextView tv_ttf_feedback;
    @BindView(R.id.tv_ttf_feedback_r)
    TextView tv_ttf_feedback_r;
    @BindView(R.id.tv_ttf_service_r)
    TextView tv_ttf_service_r;

    @BindView(R.id.tv_ttf_setting)
    TextView tv_ttf_setting;
    @BindView(R.id.tv_ttf_setting_r)
    TextView tv_ttf_setting_r;
    @BindView(R.id.tv_all_count)
    TextView tv_all_count;
    @BindView(R.id.tv_save_count)
    TextView tv_save_count;
    @BindView(R.id.tv_integral_count)
    TextView tv_integral_count;
    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;
    @BindView(R.id.tv_ttf_wallet)
    TextView tv_ttf_wallet;
    @BindView(R.id.tv_ttf_wallet_r)
    TextView tv_ttf_wallet_r;
    @BindView(R.id.tv_ttf_trading)
    TextView tv_ttf_trading;
    @BindView(R.id.tv_ttf_trading_r)
    TextView tv_ttf_trading_r;
    @BindView(R.id.tv_ttf_sms)
    TextView tv_ttf_sms;
    @BindView(R.id.tv_ttf_sms_r)
    TextView tv_ttf_sms_r;

    @BindView(R.id.tv_vip)
    TextView tv_vip;
    @BindView(R.id.tv_vip_time)
    TextView tv_vip_time;


    @Override
    public int inflaterRootView() {
        return R.layout.fragment_me;
    }

    @Override
    public void initUI() {
        super.initUI();
        EventBus.getDefault().register(this);
        tv_ttf_sms_r.setTypeface(MyUtlis.getTTF());
        tv_ttf_sms.setTypeface(MyUtlis.getTTF());
        tv_ttf_trading.setTypeface(MyUtlis.getTTF());
        tv_ttf_trading_r.setTypeface(MyUtlis.getTTF());
        tv_ttf_wallet.setTypeface(MyUtlis.getTTF());
        tv_ttf_wallet_r.setTypeface(MyUtlis.getTTF());
        tv_ttf_setting.setTypeface(MyUtlis.getTTF());
        tv_ttf_setting_r.setTypeface(MyUtlis.getTTF());
        tv_ttf_service.setTypeface(MyUtlis.getTTF());
        tv_ttf_feedback.setTypeface(MyUtlis.getTTF());
        tv_ttf_sign.setTypeface(MyUtlis.getTTF());
        tv_ttf_vip.setTypeface(MyUtlis.getTTF());
        tv_ttf_service_r.setTypeface(MyUtlis.getTTF());
        tv_ttf_feedback_r.setTypeface(MyUtlis.getTTF());
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.eventType == MessageEvent.UPDATE_AVATAR) {
            setAvatar(event.stringValue);
        } else if (event.eventType == MessageEvent.UPDATE_NICI_NAME) {
//            setNickName(event.stringValue);
        } else if (event.eventType == MessageEvent.UPDATE_ALL_PLATFORM_DATA_COUNT) {
            setAllDataCount(event.intValue);
        } else if (event.eventType == MessageEvent.UPDATE_SAVE_DATA_COUNT) {
//            setSaveDataCount(event.intValue);
        } else if (event.eventType == MessageEvent.UPDATE_LOGIN_USER_INFO) {
            //更新数据
            LogUtils.e("我的页面刷新数据");
            initData();
        }
    }

    /**
     * 设置全部平台数据总量
     *
     * @param intValue
     */
    private void setAllDataCount(int intValue) {
        tv_all_count.setText(getString(R.string.text_all_data_count, intValue));
    }

//    /**
//     * 设置保存数据总量
//     *
//     * @param intValue
//     */
//    private void setSaveDataCount(int intValue) {
//        tv_save_count.setText(getString(R.string.text_save_data_count, intValue));
//    }

    UserBean currentLoginUser;

    @Override
    public void initData() {

        currentLoginUser = DaoUtlis.getCurrentLoginUser();
        if (null == currentLoginUser) {
            return;
        }

        setVip();
        setAvatar(currentLoginUser.getAvatarUrl());
//        setNickName(currentLoginUser.getNickName());
        UserBean user = DaoUtlis.getUser(MyUtlis.getLoginUserId());
        setAllDataCount(null == user ? 0 : user.getDownNumber());
//        setSaveDataCount(null == user ? 0 : user.getSaveNumber());
        setIntegralCount(null == user ? 0 : user.getIntegral(), false);
        updateSignState();
    }

    private void setVip() {

        if (null != currentLoginUser) {
            String vipName = MyUtlis.getVipName(currentLoginUser.getGrade(), "升级为会员");
            tv_vip.setText(vipName);
        }


    }

    @Override
    public void addListeners() {

    }

//    /**
//     * 设置昵称
//     *
//     * @param nickName
//     */
//    private void setNickName{
//        if (!TextUtils.isEmpty(nickName)) {
//            tv_name.setText(nickName);
//        } else if (null != currentLoginUser) {
//            tv_name.setText("用户" + currentLoginUser.getUserId());
//        } else {
//            tv_name.setText("用户" + TimeUtils.getNowMills());
//        }
//    }Name(String nickName)

    //修改头像
    private void setAvatar(String path) {
        if (FileUtils.isFileExists(path)) {
            if (DaoUtlis.updateUserAvatar(SPUtils.getInstance().getString(AppConstant.SPKey.LAST_LOGIN_USER), path)) {
                MyUtlis.showImage(path, iv_avatar);
            }
        }
    }


//    @OnClick(R.id.iv_avatar)
//    public void iv_avatar() {
//        MyUtlis.openPhoto(this, 1);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST://相册选择图片
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (null == selectList || selectList.size() == 0) {
                        MyUtlis.showShort(getActivity(), getString(R.string.hint_photo_no_file_reselect));
                        return;
                    }
                    LocalMedia localMedia = selectList.get(0);
                    String path = localMedia.getPath();
                    setAvatar(path);
                    MyUtlis.showShortYes(getActivity(), getString(R.string.hint_avatar_edit_ok));
                    break;
            }
        }

    }

//    @OnClick(R.id.tv_edit_info)
//    public void tv_edit_info() {
//        ToastUtils.showShort("编辑信息");
//        EditInfoActivity.start(getContext());
//    }

    @OnClick(R.id.tv_ttf_sign)
    public void tv_ttf_sign() {

        //签到
        //最终还是没走网络时间, 留着下次优化, 可以通过 MyUtlis.getNetDate(); 获取网络时间
        if (isSign) {
            MyUtlis.showShort(getActivity(), getString(R.string.hint_sign_rep));
        } else {
            ApiUtlis.signIn(getActivity(), MyUtlis.getToken(), new JsonCallBack<LoginResponseBean>(LoginResponseBean.class) {
                @Override
                public void onSuccess(Response<LoginResponseBean> response) {
                    if (null != response.body()) {
                        if (response.body().getCode() == AppConstant.CODE_SUCCESS && null != response.body().getData()) {
                            //刷新数据
                            UserBean user = DaoUtlis.getCurrentLoginUser();
                            user.setIntegral(response.body().getData().getIntegral());
                            user.setLastSignTime(TimeUtils.getNowMills());
                            boolean b = DaoUtlis.updateUser(user);
//                            if (b) {
                            MyUtlis.clickEvent(AppConstant.CLICK.umeng_sign);
                            updateSignState();
                            setIntegralCount(user.getIntegral(), true);
                            MyUtlis.showShortYes(getActivity(), response.body().getMsg());

//                            } else {
//                                updateSignState();
//                                MyUtlis.showShortNo(getActivity(), response.body().getMsg());
//                            }


                        } else {
                            MyUtlis.showShortNo(getActivity(), response.body().getMsg());
                        }
                    }
                }

                @Override
                public void onStart(Request<LoginResponseBean, ? extends Request> request) {
                    super.onStart(request);
//                    showLoading();
                }

                @Override
                public void onFinish() {
                    super.onFinish();
//                    hideLoading();
                }
            });


            //签到 ,更新时间和+积分

        }
    }

    /**
     * 设置积分 (执行动画)
     *
     * @param integralCount
     */
    private void setIntegralCount(int integralCount, boolean isShowAnim) {
        tv_integral_count.setText(getString(R.string.text_integral_count, integralCount));

        if (isShowAnim) {
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_sign);
            tv_integral_count.startAnimation(animation);
        }
    }


    /**
     * 今日是否签到
     */
    public boolean isSign;

    /**
     * 更新签到状态
     */
    public void updateSignState() {
        //获取上次签到时间
        UserBean user = DaoUtlis.getUser(MyUtlis.getLoginUserId());
        if (null != user) {
            long lastSignTime = user.getLastSignTime();
            boolean today = MyUtlis.isToday(lastSignTime);
            isSign = today;
            if (isSign) {
                //已签到
                tv_ttf_sign.setTextColor(ContextCompat.getColor(getActivity(), R.color.gold));
            } else {
                //未签到
                tv_ttf_sign.setTextColor(ContextCompat.getColor(getActivity(), R.color.select_text_white_color));
            }
        }
    }


    @OnClick(R.id.rl_vip)
    public void rl_vip() {
        //还不是vip才可以点击
        if (MyUtlis.isVip()) {

            VipInfoPop vipInfoPop = new VipInfoPop(this, currentLoginUser);
            vipInfoPop.show();

        } else {
            VipActivity.start(getContext());
            MyUtlis.clickEvent(AppConstant.CLICK.umeng_vip);
        }
    }

    @OnClick(R.id.rl_service)
    public void rl_service() {
        MyUtlis.openServiceQQ(getActivity());
    }

    @OnClick(R.id.rl_feedback)
    public void rl_feedback() {
        OpinionActivity.start(getContext());
//        WebViewActivity.start(getContext(), MyUtlis.HTML_JINSHUJU_feedback, getString(R.string.feedback));
    }


    @OnClick(R.id.rl_setting)
    public void rl_setting() {
        SettingActivity.start(getContext());
    }

    @OnClick(R.id.rl_wallet)
    public void rl_wallet() {
        WalletActivity.start(getContext());
    }

    @OnClick(R.id.rl_trading)
    public void rl_trading() {
        MyTradingActivity.start(getContext());
    }

    @OnClick(R.id.rl_sms)
    public void rl_sms() {
        SMSBatchActivity.start(getContext());
    }


}
