package com.zmy.diamond.utli.dao;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.Utils;
import com.zmy.diamond.base.BaseApp;
import com.zmy.diamond.utli.ApiUtlis;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.CollectCityBean;
import com.zmy.diamond.utli.bean.CollectCityBeanDao;
import com.zmy.diamond.utli.bean.CollectRecordBean;
import com.zmy.diamond.utli.bean.CollectRecordBeanDao;
import com.zmy.diamond.utli.bean.DaoSession;
import com.zmy.diamond.utli.bean.DataBean;
import com.zmy.diamond.utli.bean.DataBeanDao;
import com.zmy.diamond.utli.bean.InfoBannerBean;
import com.zmy.diamond.utli.bean.InfoBean;
import com.zmy.diamond.utli.bean.InfoBeanDao;
import com.zmy.diamond.utli.bean.InfoDataBean;
import com.zmy.diamond.utli.bean.InfoMenuBean;
import com.zmy.diamond.utli.bean.InfoNoticeBean;
import com.zmy.diamond.utli.bean.LocationBean;
import com.zmy.diamond.utli.bean.MapKeyBean;
import com.zmy.diamond.utli.bean.MapKeyBeanDao;
import com.zmy.diamond.utli.bean.UserBean;
import com.zmy.diamond.utli.bean.UserBeanDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangmengyun on 2018/6/14.
 */

public class DaoUtlis {

    /**
     * 取得DaoSession
     *
     * @return
     */
    public static DaoSession getDaoSession() {

        return BaseApp.getInstance().getDaoSession();
    }


    /**
     * 获取当前用户所有采集的数据
     *
     * @param userId
     * @return
     */
    public static List<DataBean> getAllData(String userId) {
        List<DataBean> list = new ArrayList<>();
        try {
            UserBean user = getUser(userId);

            if (null == user)
                return list;
            list = getDaoSession().getDataBeanDao().queryBuilder().where(DataBeanDao.Properties.UserId.eq(user.getUserId())).build().list();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }


    /**
     * 删除所有数据
     *
     * @param list
     * @return
     */
    public static boolean deleteAllData(List<DataBean> list) {

        try {
            if (null != list) {
                getDaoSession().getDataBeanDao().deleteInTx(list);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除当前用户所有平台下的数据
     *
     * @return
     */
    public static boolean deleteAllData(String userId) {


        try {
            UserBean user = getUser(userId);
            if (null == user)
                return false;
            List<DataBean> list = getDaoSession().getDataBeanDao().queryBuilder().where(DataBeanDao.Properties.UserId.eq(user.getPhone())).build().list();
            getDaoSession().getDataBeanDao().deleteInTx(list);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除当前用户当前平台数据下的所有数据
     *
     * @param userId
     * @param platformId
     */
    public static boolean deleteAllData(String userId, int platformId) {
        try {
            UserBean user = getUser(userId);
            if (null == user)
                return false;
            List<DataBean> list = getDaoSession().getDataBeanDao().queryBuilder().where(DataBeanDao.Properties.UserId.eq(user.getUserId()), DataBeanDao.Properties.PlatformId.eq(platformId)).build().list();
            getDaoSession().getDataBeanDao().deleteInTx(list);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 获取所有 用户
     *
     * @return
     */
    public static List<UserBean> getAllUser() {
        return getDaoSession().getUserBeanDao().queryBuilder().list();
    }


    /**
     * 更新修改用户密码
     *
     * @param phone
     * @param newPwd
     * @return
     */
    public static boolean updateUserPwd(String phone, String newPwd) {
//        UserBean userByPhone = getUserByPhone(phone);
//        if (null == userByPhone)
//            return false;
//        userByPhone.setPwd(newPwd);
//        return addUser(userByPhone);

        return true;

    }


    /**
     * 获取当前登录的用户
     *
     * @return
     */
    public static UserBean getCurrentLoginUser() {
        String userId = SPUtils.getInstance().getString(AppConstant.SPKey.LAST_LOGIN_USER);
        return getUserById(userId);
    }

    /**
     * 更新用户头像
     *
     * @param userId
     * @param avatarPath
     * @return
     */
    public static boolean updateUserAvatar(String userId, String avatarPath) {
        UserBean userBean = getUser(userId);
        if (null == userBean)
            return false;
        userBean.setAvatarUrl(avatarPath);
        return addUser(userBean);
    }

    /**
     * 更新用户昵称
     *
     * @param nickName
     * @return
     */
    public static boolean updateUserNickName(String nickName) {
        UserBean userBean = getUser(MyUtlis.getLoginUserId());
        if (null == userBean)
            return false;
        userBean.setNickName(nickName);
        return addUser(userBean);
    }


    /**
     * @param
     * @return
     */
    public static UserBean getUserByPhone(String phone) {

        UserBean userBean = null;
        try {
            userBean = getDaoSession().getUserBeanDao().queryBuilder().where(UserBeanDao.Properties.Phone.eq(phone)).build().unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userBean;
    }


    /**
     * @param
     * @return
     */
    public static UserBean getUserById(String userId) {

        UserBean userBean = null;
        try {
            userBean = getDaoSession().getUserBeanDao().queryBuilder().where(UserBeanDao.Properties.UserId.eq(userId)).build().unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userBean;
    }

    /**
     * 注册用户
     *
     * @param userBean
     * @return @link STATE_REGIST_
     */
    public static int registUser(UserBean userBean) {
        int state = MyUtlis.STATE_REGIST_NO;
        UserBean userByPhone = getUserByPhone(userBean.getPhone());
        if (null == userByPhone) {
            if (addUser(userBean)) {
                state = MyUtlis.STATE_REGIST_YES;
            }
        } else {
            state = MyUtlis.STATE_REGIST_USER_REPEAT;
        }
        return state;
    }


    /**
     * 更新用户零钱
     *
     * @param money
     */
    public static void updateUserMoney(double money) {

        UserBean user = getCurrentLoginUser();
        if (null != user) {
            user.setMoney(money);
            addUser(user);
        }

    }

    /**
     * 更新用户数据
     *
     * @param userBean
     * @return
     */
    public static boolean updateUser(UserBean userBean) {

        try {
            long lineId = getDaoSession().getUserBeanDao().insertOrReplace(userBean);
            if (lineId > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * 添加一个用户,存在则更新
     *
     * @param userBean
     * @return
     */
    public static boolean addUser(UserBean userBean) {
        boolean isSuccess = false;


        try {
            if (null == userBean) {
                isSuccess = false;
            } else {

                UserBean oldUser = getUserById(userBean.getUserId());
                if (null != oldUser) {
                    userBean.setCity(oldUser.getCity());
                    userBean.setAvatarUrl(oldUser.getAvatarUrl());
//                    userBean.setLastSignTime(oldUser.getLastSignTime());
                    userBean.setProvince(oldUser.getProvince());
                    userBean.setPhone(oldUser.getPhone());
                    userBean.setLongitudeAndLatitude(oldUser.getLongitudeAndLatitude());
                    if (TextUtils.isEmpty(oldUser.getNickName())) {
                        userBean.setNickName("用户" + userBean.getUserId());
                    } else {
                        userBean.setNickName(oldUser.getNickName());
                    }

                }


                long lineId = getDaoSession().getUserBeanDao().insertOrReplace(userBean);
                if (lineId > 0) {
                    isSuccess = true;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (isSuccess) {
            LogUtils.e("插入更新用户成功");
        } else {
            LogUtils.e("插入更新用户失败");
        }
        return isSuccess;
    }


    /**
     * 获取用户
     *
     * @param userId userId
     * @return
     */
    public static UserBean getUser(String userId) {
        UserBean userBean = getUserById(userId);
        if (null == userBean) {
            return null;
        } else {
            return userBean;
        }

    }


    /**
     * 采集成功都会往这里 , 添加数据
     *
     * @param beanList
     * @return
     */
    public static boolean addData(List<DataBean> beanList) {

        if (null == beanList || beanList.size() == 0) {
            return false;
        }


        try {
            //每添加一次数据,我就可以认为是抓潜一次, 只要key 和city是一样那么数据最终是1条, 如果数据不一样,则是2条
            //这里仅仅只是产生一个营销列表数据的头部, 当真正点击进入某个数据时, 再去data表搜索相关数据.
            DataBean dataBean = beanList.get(0);
            if (null != dataBean) {
                CollectRecordBean collectRecordBean = new CollectRecordBean();
                collectRecordBean.setCollectId(dataBean.getKey() + dataBean.getCity());//dataBean.getPlatformId()
                collectRecordBean.setKey(dataBean.getKey());
                collectRecordBean.setCity(dataBean.getCity());
                collectRecordBean.setPlatformId(dataBean.getPlatformId());
                collectRecordBean.setSource(dataBean.getSource());
                collectRecordBean.setUserId(dataBean.getUserId());
                collectRecordBean.setUpdateTime(TimeUtils.getNowMills());
                //+上之前采集的数量
                CollectRecordBean oldBean = getCollectRecord(collectRecordBean.getCollectId(), collectRecordBean.getUserId());
                if (oldBean != null) {
                    collectRecordBean.setCount(beanList.size() + oldBean.getCount());
                } else {
                    collectRecordBean.setCount(beanList.size());
                }
                //数量
                addCollectRecord(collectRecordBean);
                //通知营销页面刷新列表数据
                MyUtlis.eventAddDataOK();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("添加采集记录错误");
        }

        BaseApp.currentDownloadDataCount += beanList.size();
        LogUtils.e("BaseApp.currentDownloadDataCount=" + BaseApp.currentDownloadDataCount);

        try {
            getDaoSession().getDataBeanDao().insertOrReplaceInTx(beanList);

//            //更新获取的数据总数
            MyUtlis.updateAllDataCont(beanList.size(), true);
//
//
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }


    /**
     * 获取采集数据通过 平台id + key+city + userId
     *
     * @return
     */
    public static List<DataBean> getDataByCityKey(int plaftormId, String key, String city, String userId) {
        List<DataBean> list = new ArrayList<>();
        try {
            list = getDaoSession().getDataBeanDao().queryBuilder().where(
//                    DataBeanDao.Properties.PlatformId.eq(plaftormId),
                    DataBeanDao.Properties.UserId.eq(userId),
                    DataBeanDao.Properties.Key.eq(key),
                    DataBeanDao.Properties.City.eq(city)
            ).build().list();

            LogUtils.e("getDataByCityKey 获取数据成功 list=" + list.size());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("getDataByCityKey 获取数据失败 ");
        }

        return list;
    }

    /**
     * 获取采集数据通过 平台id
     *
     * @return
     */
    public static List<DataBean> getDataByPlatformId(int plaftormId) {

        List<DataBean> list = new ArrayList<>();
        try {
            UserBean user = getUser(SPUtils.getInstance().getString(AppConstant.SPKey.LAST_LOGIN_USER));
            if (null != user) {
                list = getDaoSession().getDataBeanDao().queryBuilder().where(DataBeanDao.Properties.UserId.eq(user.getUserId()), DataBeanDao.Properties.PlatformId.eq(plaftormId)).build().list();
            }
            LogUtils.e("获取数据成功 list=" + list.size() + " plaftormId=" + plaftormId);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("获取数据失败 plaftormId=" + plaftormId);
        }

        return list;
    }


    /**
     * 保存位置信息
     *
     * @param bean
     */
    public static void saveLocation(LocationBean bean) {
        try {
            long l = getDaoSession().getLocationBeanDao().insertOrReplace(bean);

            //设置采集位置,如果为空的话
            String city = MyUtlis.getCollectCity();
            if (TextUtils.isEmpty(city)) {
                MyUtlis.setCollectCity(city);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取位置
     *
     * @return
     */
    public static LocationBean getLocation() {
        try {
            return getDaoSession().getLocationBeanDao().queryBuilder().build().unique();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 设置用户保存通讯录联系人数量
     *
     * @param count
     * @return
     */
    public static boolean setUserSaveDataCount(int count) {

        try {
            if (count >= 0) {

                UserBean user = getUser(MyUtlis.getLoginUserId());
                if (null == user) {
                    return false;
                } else {
                    user.setSaveNumber(count);
                    return addUser(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * 添加bannerbean
     *
     * @param infoBannerBeanList
     * @return
     */
    public static boolean addBannerBean(List<InfoBannerBean> infoBannerBeanList) {
        try {
            getDaoSession().getInfoBannerBeanDao().insertOrReplaceInTx(infoBannerBeanList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加bannerbean
     *
     * @param infoMenuBeanList
     * @return
     */
    public static boolean addInfoMenuBean(List<InfoMenuBean> infoMenuBeanList) {
        try {
            getDaoSession().getInfoMenuBeanDao().insertOrReplaceInTx(infoMenuBeanList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加
     *
     * @param infoNoticeBeanList
     * @return
     */
    public static boolean addInfoNoticeBean(List<InfoNoticeBean> infoNoticeBeanList) {
        try {
            getDaoSession().getInfoNoticeBeanDao().insertOrReplaceInTx(infoNoticeBeanList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加
     *
     * @param infoDataBeanList
     * @return
     */
    public static boolean addInfoDataBean(List<InfoDataBean> infoDataBeanList) {
        try {
            getDaoSession().getInfoDataBeanDao().insertOrReplaceInTx(infoDataBeanList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 插入info表
     *
     * @param infoBean
     * @return
     */
    public static boolean addInfoBean(InfoBean infoBean) {
        try {
            getDaoSession().getInfoBeanDao().insertOrReplace(infoBean);

            LogUtils.e("addInfoBean 成功=" + infoBean.getUserId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("addInfoBean 失败=" + infoBean.getUserId());
            return false;
        }
    }

    /**
     * 获取当前用户infobean  ,在获取失败时,尝试去获取默认用户的infobean
     *
     * @return
     */
    public static InfoBean getInfoBean() {
        try {

            UserBean currentLoginUser = getCurrentLoginUser();
            if (null != currentLoginUser) {

                InfoBean infoBean = getDaoSession().getInfoBeanDao().queryBuilder().where(InfoBeanDao.Properties.UserId.eq(currentLoginUser.getUserId())).unique();

                if (null == infoBean) {
                    return getDaoSession().getInfoBeanDao().queryBuilder().where(InfoBeanDao.Properties.UserId.eq(AppConstant.DEFAULT_USER_ID)).unique();
                } else {
                    return infoBean;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 删除当前用户info表
     *
     * @param
     * @return
     */
    public static boolean deleteInfoBean() {

        try {
            InfoBean infoBean = getInfoBean();
            if (null != infoBean) {


                getDaoSession().getInfoBannerBeanDao().deleteInTx(infoBean.getInfoBannerList());
                getDaoSession().getInfoMenuBeanDao().deleteInTx(infoBean.getInfoMenuList());
                getDaoSession().getInfoNoticeBeanDao().deleteInTx(infoBean.getInfoNoticeList());
                getDaoSession().getInfoDataBeanDao().deleteInTx(infoBean.getInfoDataList());
                getDaoSession().getInfoBeanDao().delete(infoBean);
            } else {
                getDaoSession().getInfoBannerBeanDao().deleteAll();
                getDaoSession().getInfoMenuBeanDao().deleteAll();
                getDaoSession().getInfoNoticeBeanDao().deleteAll();
                getDaoSession().getInfoDataBeanDao().deleteAll();
                getDaoSession().getInfoBeanDao().deleteAll();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 插入一条采集记录
     *
     * @param bean
     * @return
     */
    public static boolean addCollectRecord(CollectRecordBean bean) {
        try {
            getDaoSession().getCollectRecordBeanDao().insertOrReplace(bean);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 获取唯一的 采集记录   key city  platformId 都要一致
     *
     * @param
     * @param
     * @param
     * @return
     */
    public static CollectRecordBean getCollectRecord(String collectId, String userId) {

        try {
            return getDaoSession().getCollectRecordBeanDao().queryBuilder().where(
                    CollectRecordBeanDao.Properties.UserId.eq(userId),

                    CollectRecordBeanDao.Properties.CollectId.eq(collectId)).build().unique();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    /**
     * 获取采集添加数据成功记录
     *
     * @return
     */
    public static List<CollectRecordBean> getCollectRecord(String userId) {
        List<CollectRecordBean> list = new ArrayList<>();
        try {
            UserBean user = getUser(userId);


            if (null == user) {
                LogUtils.e("getCollectRecord user=null");
                return list;
            }
            list = getDaoSession().getCollectRecordBeanDao().queryBuilder().where(CollectRecordBeanDao.Properties.UserId.eq(user.getUserId())).build().list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 更新采集记录 数据数量
     *
     * @param collectId
     * @param clearCount 清除数量
     */
    public static void updateCollectRecordDataCount(String collectId, String userId, int clearCount) {

        return;
//        CollectRecordBean collectRecord = getCollectRecord(collectId, userId);
//        if (null != collectRecord) {
//            int count = collectRecord.getCount() - clearCount;
//
//            if (count < 0) {
//                count = 0;
//            }
//            collectRecord.setCount(count);
//            addCollectRecord(collectRecord);
//            MyUtlis.eventRefreshMarketingFragmentData();
//
//        }
    }


    /**
     * 删除用户的所有采集记录
     *
     * @param
     * @return
     */
    public static boolean deleteCollectRecord() {

        try {
            getDaoSession().getCollectRecordBeanDao().deleteAll();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 删除一条采集记录
     *
     * @param collectId
     * @param userId
     * @return
     */
    public static boolean deleteCollectRecord(String collectId, String userId) {
        try {
            CollectRecordBean collectRecord = getCollectRecord(collectId, userId);
            if (null != collectRecord) {
                getDaoSession().getCollectRecordBeanDao().delete(collectRecord);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 获取当前采集城市的信息数据
     *
     * @param collectCity
     * @param userId
     * @return
     */
    public static CollectCityBean getCollectCity(String collectCity, String userId) {
        try {
            return getDaoSession().getCollectCityBeanDao().queryBuilder().where(CollectCityBeanDao.Properties.CollectCity.eq(collectCity), CollectCityBeanDao.Properties.UserId.eq(userId)).unique();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 插入更新采集城市的信息数据
     *
     * @return
     */
    public static boolean addCollectCity(CollectCityBean collectCityBean) {
        try {
            getDaoSession().getCollectCityBeanDao().insertOrReplace(collectCityBean);
            LogUtils.e("插入更新采集城市的信息数据 成功=" + collectCityBean.getCollectCity());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("插入更新采集城市的信息数据 失败=" + collectCityBean.getCollectCity());
            return false;
        }
    }


    /**
     * 添加更新mapkey
     *
     * @param bean
     * @return
     */
    public static boolean addMapKey(List<MapKeyBean> bean) {
        if (null == bean) {
            return false;
        }
        //判断一下新的mapkey 和原来的mapKey
//        List<MapKeyBean> oldMapKeyList= getDaoSession().getMapKeyBeanDao().queryBuilder().build().list();
//        if(bean.size()>0&&null!=oldMapKeyList&&oldMapKeyList.size()>0){
//
//            for(MapKeyBean newBean:bean){
//                for(MapKeyBean oldBean:oldMapKeyList){
//                    if(oldBean.getMapType()==newBean.getMapType() &&oldBean.getMapKey().equals(newBean.getMapKey())){
//
//                        //mapType和mapkey一致认为同一个数据.使用本地的状态？不需要啊。
//                        newBean.setStatus_concurr(oldBean.getStatus_concurr());
//                        newBean.setStatus_excess(oldBean.getStatus_excess());
//                    }
//
//
//                }
//
//            }
//
//
//        }

        //直接删除原有key
        getDaoSession().getMapKeyBeanDao().deleteAll();

        try {
            getDaoSession().getMapKeyBeanDao().insertOrReplaceInTx(bean);
            LogUtils.e("addMapKey 成功=" + bean.size());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("addMapKey 失败=" + bean.size());
            return false;
        }
    }

    /**
     * 获取数据库中，mapType= 且不是超额和并发状态的数据
     *
     * @param mapType
     * @return
     */
    public static List<MapKeyBean> getMapKey(int mapType) {

        //查询数据库中，mapType= 且不是超额和并发状态的数据
        List<MapKeyBean> list = new ArrayList<>();
        try {
            list = getDaoSession().getMapKeyBeanDao().queryBuilder().where(MapKeyBeanDao.Properties.MapType.eq(mapType), MapKeyBeanDao.Properties.Status_concurr.eq(false), MapKeyBeanDao.Properties.Status_excess.eq(false)).build().list();
            LogUtils.e("getMapKey 成功=" + list.size());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("getMapKey 失败");
        }
        return list;

    }

    /**
     * 获取一个可用的mapkey
     *
     * @param mapType
     * @return
     */
    public static String getMapKey2(int mapType) {
        List<MapKeyBean> mapKeyList = getMapKey(mapType);
        String mapKey = "";
        if (null != mapKeyList && mapKeyList.size() > 0) {
            mapKey = mapKeyList.get(0).getMapKey();
        } else {
//            mapKey = mapType == AppConstant.MAP_KEY_TYPE_BAIDU ? AppConstant.Platform.KEY_BAIDU_MAP : AppConstant.Platform.KEY_GAODE_MAP;
        }
        LogUtils.e("getMapKey2=" + mapKey);
        return mapKey;
    }

    /**
     * @param mapType
     * @param mapKey
     * @param Status_concurr 是否并发
     * @param Status_excess  是否超额
     */
    public static void updateMapKey(int mapType, String mapKey, boolean Status_concurr, boolean Status_excess) {

        try {
            MapKeyBean keyBean = getDaoSession().getMapKeyBeanDao().queryBuilder().where(MapKeyBeanDao.Properties.MapType.eq(mapType), MapKeyBeanDao.Properties.MapKey.eq(mapKey)).build().unique();
            keyBean.setStatus_excess(Status_excess);
            keyBean.setStatus_concurr(Status_concurr);
            getDaoSession().getMapKeyBeanDao().insertOrReplace(keyBean);
            ApiUtlis.updateMapKey(Utils.getApp(), MyUtlis.getToken(), keyBean.getMapKeyId(), Status_excess, Status_concurr);
            LogUtils.e("更新本地mapKey成功=" + mapKey);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("更新本地mapKey失败=" + mapKey);
        }
    }


    /**
     * 删除mapkey
     * @param mapType
     * @param mapKey
     * @return
     */
    public static boolean deleteMapKey(int mapType, String mapKey) {
        try {
            MapKeyBean keyBean = getDaoSession().getMapKeyBeanDao().queryBuilder().where(MapKeyBeanDao.Properties.MapType.eq(mapType), MapKeyBeanDao.Properties.MapKey.eq(mapKey)).build().unique();
            if (null != keyBean) {
                getDaoSession().getMapKeyBeanDao().delete(keyBean);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}



