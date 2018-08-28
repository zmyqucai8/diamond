package com.zmy.diamond.utli;


import android.content.Context;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.LogUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.zmy.diamond.utli.bean.AboutAppBean;
import com.zmy.diamond.utli.bean.AppVersionBean;
import com.zmy.diamond.utli.bean.BankCardBean;
import com.zmy.diamond.utli.bean.JoinVipResponseBean;
import com.zmy.diamond.utli.bean.LoginResponseBean;
import com.zmy.diamond.utli.bean.MyTradingModifyBean;
import com.zmy.diamond.utli.bean.PublicResponseBean;
import com.zmy.diamond.utli.bean.SystemTimeBean;
import com.zmy.diamond.utli.bean.TradingDataBean;
import com.zmy.diamond.utli.bean.UserBean;
import com.zmy.diamond.utli.bean.VipOrderQueryBean;
import com.zmy.diamond.utli.bean.WalletDetailsBean;
import com.zmy.diamond.utli.dao.DaoUtlis;

import java.util.Map;
import java.util.TreeMap;

/**
 * api工具类
 * Created by zhangmengyun on 2018/8/3.
 */

public class ApiUtlis {
    /**
     * 登录
     *
     * @return
     */
    public static void login(Context context, final JsonCallBack<LoginResponseBean> callback)

    {
        String macAddress = DeviceUtils.getMacAddress();
        LogUtils.e("macAddress=" + macAddress);

        OkGo.<LoginResponseBean>post(AppConstant.Api.login)
                .tag(context)
//                .params("sign", SignUtli.getSignature(new String[]{macAddress}))
                .params("macAddress", macAddress).execute(new JsonCallBack<LoginResponseBean>(LoginResponseBean.class) {
            @Override
            public void onSuccess(Response<LoginResponseBean> response) {
                MyUtlis.clickEvent(AppConstant.CLICK.umeng_login);
                if (null != response.body()) {
                    if (response.body().getCode() == AppConstant.CODE_SUCCESS && null != response.body().getData()) {
                        LoginResponseBean.DataBean data = response.body().getData();
                        //更新本地用户信息
                        UserBean userBean = new UserBean();
                        userBean.setUserId(data.getId());
                        userBean.setMacAddress(data.getMacAddress());
                        userBean.setCode(data.getCode());
                        userBean.setMoney(data.getMoney());
                        userBean.setGrade(data.getGrade());
                        userBean.setRecomCode(data.getRecomCode());
                        userBean.setToken(data.getToken());
                        userBean.setIntegral(data.getIntegral());
                        userBean.setNickName(data.getUserName());
                        userBean.setLastSignTime(data.getSigin_time());

                        userBean.setDownNumber(data.getDownNumber());
                        userBean.setSaveNumber(data.getSaveNumber());

                        userBean.setVip_time(data.getVip_time());
                        userBean.setVip_valid_time(MyUtlis.getVipValidTime(data.getVip_time()));
                        userBean.setRigister_time(data.getRigister_time());
                        userBean.setDown_number_time(data.getDown_number_time());

                        boolean addUser = DaoUtlis.addUser(userBean);
                        if (addUser) {
                            MyUtlis.setLoginUserId(userBean.getUserId());
                            MyUtlis.setToken(userBean.getToken());
                            callback.onSuccess(response);
                        } else {
////                        login();
//                            if (AppConstant.LOG)
//                                MyUtlis.showShort(SplashActivity.this, "写入数据，请稍后重试");
                            callback.onSuccess(response);
                        }
                    } else {
                        callback.onSuccess(response);
//                        MyUtlis.showShort(SplashActivity.this, response.body().getCode() + " " + response.body().getMsg());
                    }
                }


            }
        });
    }


    /**
     * 登录获取数据更新后，关闭页面
     */
    public static void loginUpdate(Context context, final JsonCallBack<LoginResponseBean> callBack) {
        login(context, new JsonCallBack<LoginResponseBean>(LoginResponseBean.class) {
            @Override
            public void onSuccess(Response<LoginResponseBean> response) {
                if (null != response.body() && null != response.body().getData() && null != DaoUtlis.getCurrentLoginUser()) {
                    MyUtlis.eventUpdateLoginUserInfo();

                    if (null != callBack) {
                        callBack.onSuccess(response);
                    }
                }

            }
        });
    }

    /**
     * 登录获取数据更新后，不关闭页面
     */
    public static void loginUpdate(Context context) {
        loginUpdate(context, null);
    }

    /**
     * 保存数量更新
     *
     * @return
     */
    public static void saveNumberUpdate(Context context, int saveNumber, String token, JsonCallBack<LoginResponseBean> callback) {

        Map<String, String> map = new TreeMap<>();
        map.put("token", token);
        map.put("saveNumber", String.valueOf(saveNumber));

        String sign = SignUtli.getSignature(map);
        OkGo.<LoginResponseBean>post(AppConstant.Api.saveNumberUpdate)
                .tag(context)
                .params("sign", sign)
                .params("token", token)
                .params("saveNumber", saveNumber)
                .execute(callback);
    }

    /**
     * 下载获取数量更新
     *
     * @return
     */
    public static void downNumberUpdate(Context context, int downNumber, String token, JsonCallBack<LoginResponseBean> callback) {

        Map<String, String> map = new TreeMap<>();
        map.put("token", token);
        map.put("downNumber", String.valueOf(downNumber));
        String sign = SignUtli.getSignature(map);
        OkGo.<LoginResponseBean>post(AppConstant.Api.downNumberUpdate)
                .tag(context)
                .params("sign", sign)
                .params("token", token)
                .params("downNumber", downNumber)
                .execute(callback);
    }

    /**
     * 意见反馈
     *
     * @return
     */
    public static void opinion(Context context, String message, String token, JsonCallBack<PublicResponseBean> callback) {


        Map<String, String> map = new TreeMap<>();
        map.put("token", token);

        map.put("message", message);
        String sign = SignUtli.getSignature(map);
        OkGo.<PublicResponseBean>post(AppConstant.Api.opinion)
                .tag(context)
                .params("sign", sign)
                .params("token", token)
                .params("message", message)
                .execute(callback);
    }


    /**
     * 签到
     *
     * @return
     */
    public static void signIn(Context context, String token, JsonCallBack<LoginResponseBean> callback) {
        Map<String, String> map = new TreeMap<>();
        map.put("token", token);

        String sign = SignUtli.getSignature(map);
        OkGo.<LoginResponseBean>post(AppConstant.Api.signIn)
                .tag(context)
                .params("sign", sign)
                .params("token", token)
                .execute(callback);
    }


    /**
     * 关于app
     *
     * @return
     */
    public static void aboutApp(Context context, String token, JsonCallBack<AboutAppBean> callback) {
        Map<String, String> map = new TreeMap<>();
        map.put("token", token);

        String sign = SignUtli.getSignature(map);
        OkGo.<AboutAppBean>post(AppConstant.Api.aboutApp)
                .tag(context)
                .params("token", token)
                .params("sign", sign)
                .execute(callback);
    }


    /**
     * 加入vip
     *
     * @param grade    1=黄金会员2=白金会员
     * @param token
     * @param callback
     */
    public static void joinVIP(Context context, String token, int grade, String recomCode, JsonCallBack<JoinVipResponseBean> callback) {
        Map<String, String> map = new TreeMap<>();
        map.put("token", token);
        map.put("grade", String.valueOf(grade));
        map.put("recomCode", recomCode);
        String sign = SignUtli.getSignature(map);
        OkGo.<JoinVipResponseBean>post(AppConstant.Api.joinVIP)
                .tag(context)
                .params("token", token)
                .params("sign", sign)
                .params("grade", grade)
                .params("recomCode", recomCode)
                .execute(callback);
    }


    /**
     * 获取买账号 ， 卖账号
     *
     * @param token
     * @param action   1=卖账号 2= 买账号
     * @param page     页码
     * @param callback
     */
    public static void getTrade(Context context, String token, int action, int page, JsonCallBack<TradingDataBean> callback) {
        Map<String, String> map = new TreeMap<>();
        map.put("token", token);
        map.put("action", String.valueOf(action));
        map.put("page", String.valueOf(page));
        String sign = SignUtli.getSignature(map);
        OkGo.<TradingDataBean>post(AppConstant.Api.getTrade)
                .tag(context)
                .params("token", token)
                .params("sign", sign)
                .params("action", action)
                .params("page", page)
                .execute(callback);
    }

    /**
     * 操作发布
     * * ----------------------------------（卖  action="1"）
     * action、token、weixin、price、trade_describe、industry、city、friend_number ， type
     *
     * @param token
     * @param
     * @param type     1=卖账号 2=买账号
     * @param callback {"msg":"成功","code":200,"data":null}
     */
    public static void operationPublish(Context context, String token, int type, String weixin, int price, String trade_describe, String industry, String city, int friend_number, JsonCallBack<PublicResponseBean> callback) {


        Map<String, String> map = new TreeMap<>();
        map.put("token", token);
        map.put("action", String.valueOf(1));
        map.put("weixin", weixin);
        map.put("price", String.valueOf(price));
        map.put("trade_describe", trade_describe);
        map.put("industry", industry);
        map.put("city", city);
        map.put("friend_number", String.valueOf(friend_number));
        map.put("type", String.valueOf(type));
        String sign = SignUtli.getSignature(map);

        OkGo.<PublicResponseBean>post(AppConstant.Api.operationPublish)
                .tag(context)
                .params("sign", sign)
                .params("token", token)
                .params("action", 1)
                .params("weixin", weixin)
                .params("price", price)
                .params("trade_describe", trade_describe)
                .params("industry", industry)
                .params("city", city)
                .params("friend_number", friend_number)
                .params("type", type)
                .execute(callback);
    }

    /**
     * 操作发布
     * ----------------------------------（修改  action="2"）
     * action、token、id、weixin、describe、price、industry、city、friend_number
     *
     * @param token
     * @param callback
     */
    public static void operationPublishModify(Context context, String token, String id, int type, String weixin, int price, String trade_describe, String industry, String city, int friend_number, JsonCallBack<MyTradingModifyBean> callback) {
        Map<String, String> map = new TreeMap<>();
        map.put("token", token);
        map.put("id", id);
        map.put("action", String.valueOf(2));
        map.put("weixin", weixin);
        map.put("price", String.valueOf(price));
        map.put("trade_describe", trade_describe);
        map.put("industry", industry);
        map.put("city", city);
        map.put("friend_number", String.valueOf(friend_number));
        map.put("type", String.valueOf(type));
        String sign = SignUtli.getSignature(map);
        OkGo.<MyTradingModifyBean>post(AppConstant.Api.operationPublish)
                .tag(context)
                .params("sign", sign)
                .params("token", token)
                .params("id", id)
                .params("action", 2)
                .params("weixin", weixin)
                .params("price", price)
                .params("trade_describe", trade_describe)
                .params("industry", industry)
                .params("city", city)
                .params("friend_number", friend_number)
                .params("type", type)
                .execute(callback);
    }

    /**
     * 操作发布
     * ---------------------------------（删除  action="3"）
     * action、token、id
     *
     * @param token
     * @param callback
     */
    public static void operationPublishDelete(Context context, String token, String id, JsonCallBack<PublicResponseBean> callback) {

        Map<String, String> map = new TreeMap<>();
        map.put("token", token);
        map.put("id", id);
        map.put("action", String.valueOf(3));
        String sign = SignUtli.getSignature(map);
        OkGo.<PublicResponseBean>post(AppConstant.Api.operationPublish)
                .tag(context)
                .params("sign", sign)
                .params("token", token)
                .params("action", 3)
                .params("id", id)
                .execute(callback);
    }


    /**
     * 操作发布
     * ----------------------------------(查  action="4")  查询的时候最多会返回4条数据， 数据中有type类型，判断是买还是卖
     * action、token
     *
     * @param token
     * @param callback
     */
    public static void operationPublishQuery(Context context, String token, JsonCallBack<TradingDataBean> callback) {

        Map<String, String> map = new TreeMap<>();
        map.put("token", token);
        map.put("action", String.valueOf(4));
        String sign = SignUtli.getSignature(map);
        OkGo.<TradingDataBean>post(AppConstant.Api.operationPublish)
                .tag(context)
                .params("sign", sign)
                .params("token", token)
                .params("action", 4)
                .execute(callback);
    }

    /**
     * 获取零钱明细
     *
     * @param token
     * @param page
     * @param callback
     */
    public static void walletDetails(Context context, String token, int page, JsonCallBack<WalletDetailsBean> callback) {
        //{"msg":"没有数据","code":201,"data":null}
//        {"msg":"成功","code":200,"data":[{"user_id":235,"id":4,"message":"您发起的1000元提现申请正在审核中","status":0},{"user_id":235,"id":5,"message":"您发起的1000元提现申请正在审核中","status":0}]}
        Map<String, String> map = new TreeMap<>();
        map.put("token", token);
        map.put("action", String.valueOf(2));
        map.put("page", String.valueOf(page));
        String sign = SignUtli.getSignature(map);
        OkGo.<WalletDetailsBean>post(AppConstant.Api.wallet)
                .tag(context)
                .params("sign", sign)
                .params("token", token)
                .params("action", 2)
                .params("page", page)
                .execute(callback);
    }


    /**
     * 查询订单
     *
     * @param orderId
     * @param token
     * @param callback
     */
    public static void orderQuery(Context context, String token, String orderId, JsonCallBack<VipOrderQueryBean> callback) {

        Map<String, String> map = new TreeMap<>();
        map.put("token", token);
        map.put("orderId", orderId);
        String sign = SignUtli.getSignature(map);
        OkGo.<VipOrderQueryBean>post(AppConstant.Api.orderQuery)
                .tag(context)
                .params("sign", sign)
                .params("token", token)
                .params("orderId", orderId)
                .execute(callback);
    }


    /**
     * 获取钱包银行卡列表
     *
     * @param token
     * @param callBack
     */

    public static void walletBankCardList(Context context, String token, JsonCallBack<BankCardBean> callBack) {
        Map<String, String> map = new TreeMap<>();
        map.put("token", token);
        map.put("action", String.valueOf(4));
        String sign = SignUtli.getSignature(map);

        OkGo.<BankCardBean>post(AppConstant.Api.wallet)
                .tag(context)
                .params("sign", sign)
                .params("token", token)
                .params("action", 4)
                .execute(callBack);
    }


    /**
     * 绑定银行卡
     *
     * @param token
     * @param cardCode
     * @param phone
     * @param name
     * @param bankName
     */
    public static void walletBindingBankCard(Context context, String token, String cardCode, String phone, String name, String bankName, JsonCallBack<PublicResponseBean> callBack) {
        // {"msg":"成功","code":200,"data":null}

        Map<String, String> map = new TreeMap<>();
        map.put("token", token);
        map.put("action", String.valueOf(3));
        map.put("card", cardCode);
        map.put("phone_number", phone);
        map.put("name", name);
        map.put("bank_name", bankName);
        String sign = SignUtli.getSignature(map);
        OkGo.<PublicResponseBean>post(AppConstant.Api.wallet)
                .tag(context)
                .params("sign", sign)
                .params("token", token)
                .params("action", 3)
                .params("card", cardCode)
                .params("phone_number", phone)
                .params("name", name)
                .params("bank_name", bankName)
                .execute(callBack);
    }


    /**
     * 解除银行卡绑定
     *
     * @param token
     * @param cardId
     * @param callBack
     */
    public static void walletUnBinDingBankCard(Context context, String token, String cardId, JsonCallBack<PublicResponseBean> callBack) {

        Map<String, String> map = new TreeMap<>();
        map.put("token", token);
        map.put("action", String.valueOf(5));
        map.put("card_id", cardId);
        String sign = SignUtli.getSignature(map);

        OkGo.<PublicResponseBean>post(AppConstant.Api.wallet)
                .tag(context)
                .params("sign", sign)
                .params("token", token)
                .params("action", 5)
                .params("card_id", cardId)
                .execute(callBack);
    }


    /**
     * @param token
     * @param amount 提现金额
     * @param cardId 提现卡id
     */
    public static void walletTiXian(Context context, String token, String amount, String cardId, JsonCallBack<LoginResponseBean> callBack) {
//        {"msg":"成功","code":200,"data":{"macAddress":"52:54:00:12:34:56","code":null,"money":99000.0,"integral":20,"grade":0,"downNumber":20,"recomCode":null,"id":235,"userName":null,"saveNumber":20,"token":null}}

        Map<String, String> map = new TreeMap<>();
        map.put("token", token);
        map.put("action", String.valueOf(1));
        map.put("card_id", cardId);
        map.put("amount", amount);
        String sign = SignUtli.getSignature(map);
        OkGo.<LoginResponseBean>post(AppConstant.Api.wallet)
                .tag(context)
                .params("sign", sign)
                .params("token", token)
                .params("action", 1)
                .params("amount", amount)
                .params("card_id", cardId)
                .execute(callBack);
    }


    /**
     * 获取服务器系统时间
     * {"msg":"成功","code":200,"data":1535016406242}
     *
     * @param context
     * @param token
     */
    public static void getSystemTime(Context context, String token) {

        Map<String, String> map = new TreeMap<>();
        map.put("token", token);
        String sign = SignUtli.getSignature(map);

        OkGo.<SystemTimeBean>post(AppConstant.Api.getSystemTime)
                .tag(context)
                .params("sign", sign)
                .params("token", token)
                .execute(new JsonCallBack<SystemTimeBean>(SystemTimeBean.class) {
                    @Override
                    public void onSuccess(Response<SystemTimeBean> response) {
                        if (null != response.body()) {
                            MyUtlis.setSystemTime(response.body().getData());
                        }
                    }
                });
    }


    /**
     * 获取app版本
     *
     * @param context
     * @param token
     */
    public static void getAppVersion(Context context, String token, JsonCallBack<AppVersionBean> callBack) {

        Map<String, String> map = new TreeMap<>();
        map.put("token", token);
        String sign = SignUtli.getSignature(map);
        OkGo.<AppVersionBean>post(AppConstant.Api.getAppVersion)
                .tag(context)
                .params("sign", sign)
                .params("token", token)
                .execute(callBack);
    }
}