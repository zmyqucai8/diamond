package com.zmy.diamond.utli;

import com.zmy.diamond.utli.bean.DataBean;
import com.zmy.diamond.utli.bean.LocationBean;

import java.util.List;

/**
 * Created by zhangmengyun on 2018/6/16.
 */

public class MessageEvent {


    /**
     * 更新头像  value=avatarUrl
     * MyUtlis.eventUpdateAvatar(String avatarUrl)
     */
    public static final int UPDATE_AVATAR = 1;
    /**
     * 更新昵称  value=nickName
     * MyUtlis.eventNickName (String nickName)
     */
    public static final int UPDATE_NICI_NAME = 2;

    /**
     * 更新首页数据
     * MyUtlis.eventUpdateHomeData ()
     */
    public static final int UPDATE_HOME_DATA = 3;

    /**
     * 定位完成
     * MyUtlis.eventUpdateLocation (BDLocation bdLocation)
     */
    public static final int UPDATE_LOCATION = 4;


    /**
     * 采集完成
     * MyUtlis.eventCollectComplete ()
     */
    public static final int COLLECT_COMPLETE = 5;

    /**
     * 设置首页平台菜单 隐藏=false or 显示=true
     * MyUtlis.eventHomeMenuVisibility (isShow)   - booleanValue
     */
    public static final int HOME_MENU_VISIBILITY = 6;


    /**
     * 数据采集event
     * MyUtlis.eventCollectData ( List<DataBean> list )
     * <p>
     * - intValue =platformId
     * - dataList =List<DataBean>
     */
    public static final int COLLECT_DATA = 7;


    /**
     * 更新首页菜单上的当前平台总数
     * MyUtlis.eventUpdatePlatfromDataCount (platformId)
     * <p>
     * - intValue =platformId
     * - dataList =List<DataBean>
     */
    public static final int UPDATE_PLATFORM_DATA_COUNT = 8;


    /**
     * 更新我的页面, 累计数据总量
     * MyUtlis.eventUpdateAllPlatfromDataCount (intvalue)
     * <p>
     * - intValue =allDataCount
     */
    public static final int UPDATE_ALL_PLATFORM_DATA_COUNT = 9;

    /**
     * 更新我的页面, 保存通讯录数据总量
     * MyUtlis.eventUpdateSaveDataCount (intvalue)
     * <p>
     * - intValue =saveDataCount
     */
    public static final int UPDATE_SAVE_DATA_COUNT = 10;


    /**
     * 采集错误
     * MyUtlis.eventCollectError ()
     */
    public static final int COLLECT_ERROR = 11;


    /**
     * 添加采集数据成功
     */
    public static final int ADD_COLLECT_DATA_OK = 12;


    /**
     * 刷新交易大厅列表数据    intValue = dataType 1&2  当dataType=0时，刷新2个个列表，否则刷新对应列表
     */
    public static final int UPDATE_TRADING_DATA = 13;

    /**
     * 刷新我的交易页面数据
     */
    public static final int UPDATE_MY_TRADING_DATA = 14;


    /**
     * 调用登录成功 ，所有登录接口数据的相关页面进行数据更新 (读取当前登录用户）
     * 1.我的页面
     * 2.会员页面
     * 3.钱包页面 （暂未处理）
     * ...
     */
    public static final int UPDATE_LOGIN_USER_INFO = 15;

    /**
     * 添加银行卡成功
     */
    public static final int ADD_BANK_CARD_OK = 16;

    /**
     * 提现成功
     */
    public static final int TIXIAN_OK = 17;
    /**
     * 事件类型
     */
    public int eventType;

    /*字符串值*/
    public String stringValue;

    /*定位event value*/
    public LocationBean locationBean;

    /*布尔值*/
    public boolean booleanValue;

    /*int值*/
    public int intValue;

    /*Object值*/
    public Object objectValue;

    /**
     * 采集的数据
     */
    public List<DataBean> dataList;


}
