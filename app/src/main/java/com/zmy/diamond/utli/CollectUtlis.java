package com.zmy.diamond.utli;

import android.app.Activity;
import android.text.TextUtils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;
import com.baidu.mapapi.utils.DistanceUtil;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.zmy.diamond.R;
import com.zmy.diamond.base.BaseApp;
import com.zmy.diamond.utli.bean.CollectCityBean;
import com.zmy.diamond.utli.bean.DataBean;
import com.zmy.diamond.utli.bean.JsonBean_BaiDuMap;
import com.zmy.diamond.utli.bean.JsonBean_GaoDeMap;
import com.zmy.diamond.utli.bean.PlatformBean;
import com.zmy.diamond.utli.dao.DaoUtlis;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据采集工具类
 * Created by zhangmengyun on 2018/6/20.
 */

public class CollectUtlis {
    /**
     * 采集回调接口
     */
    public interface OnCollectListener {
        /**
         * 采集中每次有数据都会回调
         *
         * @param dataList
         */
        void onCollect(List<DataBean> dataList);

        /**
         * 采集完成
         */
        void onCollectComplete();

        /**
         * 采集发生错误
         */
        void onCollectError();


    }


    private static CollectUtlis utlis;

    private PlatformBean mPlatformBean;

    /**
     * 获取实例对象
     *
     * @return
     */
    public synchronized static CollectUtlis getInstance() {
        if (null == utlis) {
            utlis = new CollectUtlis();
        }
        return utlis;
    }

    /**
     * 开始采集
     *
     * @param activity
     * @param platformBean 平台信息
     * @param city
     * @param key
     */
    public void startCollect(Activity activity, PlatformBean platformBean, String city, String key, int phoneType, OnCollectListener listener) {
        this.mPlatformBean = platformBean;
        //采集前判断网络是否可以用
        if (!NetworkUtils.isConnected()) {
            MyUtlis.showShort(activity, MyUtlis.getString(R.string.hint_network_no_connected));
            listener.onCollectError();
            return;
        }

        MyUtlis.clickEvent(AppConstant.CLICK.umeng_start_collect);
        switch (platformBean.platformId) {
            case AppConstant.PlatformType.BAIDU_MAP:
                collectBaiDuMap(platformBean, city, key, phoneType, listener);
                break;
            case AppConstant.PlatformType.GAODE_MAP:
                collectGaoDeMap(platformBean, city, key, phoneType, listener);
                break;
            case AppConstant.PlatformType.MEITUAN:
                collectMeiTuan(city, key, listener);
                break;
            case AppConstant.PlatformType.ELEME:
                collectELeMe(city, key, listener);
                break;
            case AppConstant.PlatformType.TAOBAO:
                collectTaoBao(city, key, listener);
                break;
            case AppConstant.PlatformType.JD:
                collectJD(city, key, listener);
                break;
            case AppConstant.PlatformType.WEIPINHUI:
                collectWeiPinHui(city, key, listener);
                break;

        }

    }


    /**
     * 采集百度地图数据
     *
     * @param platformBean
     * @param city
     * @param key
     * @param phoneType    目标类型- 电话类型 -
     * @param listener
     */
    private void collectBaiDuMap(final PlatformBean platformBean, final String city, final String key, final int phoneType, final OnCollectListener listener) {

        LogUtils.e("开始采集，百度数据");

        final String userId = MyUtlis.getLoginUserId();

        // city+key 组成唯一码，记录当前city采集的index， （暂不区分鱼塘）


        //1.先根据city 反编码获取城市的中心点经纬度
        //2.根据中心经纬度，开始分块
        //3.根据city+key获取当前city采集到哪个位置，从下一个位置开始采集

        //获取行政区域中心和边界
        DistrictSearch mDistrictSearch = DistrictSearch.newInstance();
        mDistrictSearch.setOnDistrictSearchListener(new OnGetDistricSearchResultListener() {
            @Override
            public void onGetDistrictResult(DistrictResult districtResult) {
                //城市反编码回调监听

                if (null == districtResult) {
                    LogUtils.e("获取行政区域中心坐标 districtResult=null");
                    return;
                }
                if (null == districtResult.getCenterPt()) {
                    LogUtils.e("获取行政区域中心坐标 districtResult.getCenterPt()=null");
                    return;
                }


                final List<LatLng> cityBlockLatLng = getCityBlockLatLng(districtResult.getCenterPt());

                if (null == cityBlockLatLng || cityBlockLatLng.isEmpty()) {
                    LogUtils.e("城市分块数据为空");
                    return;
                }

                CollectCityBean collectCity = DaoUtlis.getCollectCity(city + key, userId);
                //如果为空,新建
                if (null == collectCity) {
                    collectCity = new CollectCityBean();
                    collectCity.setCollectCity(city + key);
                    collectCity.setKey(key);
                    collectCity.setCity(city);
                    collectCity.setBlockCount(cityBlockLatLng.size());
                    collectCity.setCurrentIndex(0);
                    collectCity.setUserId(userId);
                    collectCity.setPlatformId(platformBean.platformId);
                    DaoUtlis.addCollectCity(collectCity);
                    LogUtils.e("新建采集城市数据=" + collectCity.getCollectCity());
                }


                //开始在子线程采集？
                final CollectCityBean finalCollectCity = collectCity;
                ThreadUtils.executeByFixed(AppConstant.THREAD_SIZE, new ThreadUtils.SimpleTask<Boolean>() {
                    @Override
                    public Boolean doInBackground() {
                        return getBaiDuMapData(cityBlockLatLng, finalCollectCity, userId, platformBean, city, key, phoneType, 0, finalCollectCity.getCurrentIndex() != 0);
                    }

                    @Override
                    public void onSuccess(Boolean result) {
//                MyUtlis.eventCollectComplete();
                        if (result) {
                            listener.onCollectComplete();
                        } else {
                            listener.onCollectError();
                        }


                    }
                });


            }
        });

        DistrictSearchOption districtSearchOption = new DistrictSearchOption();
        districtSearchOption.cityName(city);//检索城市名称
//        districtSearchOption.districtName(districtStr);//检索的区县名称
        mDistrictSearch.searchDistrict(districtSearchOption);//请求行政区数据


    }

    /**
     * 获取城市经纬度 分块
     *
     * @param centerLatLng 城市中心点
     * @return
     */
    private List<LatLng> getCityBlockLatLng(LatLng centerLatLng) {

        int pNum = 0;
        int num = 1;
        List<LatLng> latLngList = new ArrayList<>();

        latLngList.add(centerLatLng);
        LogUtils.e("分块开始=" + TimeUtils.getNowString());
        while (pNum < AppConstant.CITY_BLOCK_COUNT) {
            for (int i = 0; i < num; i++) {
//                y=竖=纬度=latitude  +=向上  -=向下
//                        x=横向=经度=longitude +=向右 -=向左;
                //在当前位置上，向右移动2公里 ， 次数=num
                centerLatLng = new LatLng(centerLatLng.latitude, centerLatLng.longitude + AppConstant.CITY_BLOCK_MARGIN);
                latLngList.add(centerLatLng);
                pNum++;
            }

            for (int i = 0; i < num; i++) {
                //在当前位置上，向上移动2公里  次数=num
                centerLatLng = new LatLng(centerLatLng.latitude + AppConstant.CITY_BLOCK_MARGIN, centerLatLng.longitude);
                latLngList.add(centerLatLng);
                pNum++;
            }
            //循环 ++
            num++;

            for (int i = 0; i < num; i++) {
                //在当前位置上，向左每次移动2公里 次数=num
                centerLatLng = new LatLng(centerLatLng.latitude, centerLatLng.longitude - AppConstant.CITY_BLOCK_MARGIN);
                latLngList.add(centerLatLng);
                pNum++;
            }

            for (int i = 0; i < num; i++) {
                //   //在当前位置上，向下每次移动2公里 次数=num
                centerLatLng = new LatLng(centerLatLng.latitude - AppConstant.CITY_BLOCK_MARGIN, centerLatLng.longitude);
                latLngList.add(centerLatLng);
                pNum++;
            }
            //循环 ++
            num++;
        }

        LogUtils.e("分块结束=" + TimeUtils.getNowString());
        LogUtils.e("块数=" + latLngList.size());
        //计算一下 0-1的距离
        double distance = DistanceUtil.getDistance(latLngList.get(0), latLngList.get(1));
        LogUtils.e("0和1的距离=" + distance + "米");

        return latLngList;
    }

    /**
     * 根据当前中心点坐标，获取2km*2km的范围， 左下角坐标和右上角坐标
     * y=竖=纬度=latitude  +=向上  -=向下
     * //                        x=横向=经度=longitude +=向右 -=向左;
     * //在当前位置上，向右移动2公里 ， 次数=num
     *
     * @param centerLatLng 中心点坐标
     * @param type         默认=0，返回的是左下和右上， 如果=1返回的是左上和右下
     * @return
     */
    public String getBounds(LatLng centerLatLng, int type) {
        List<LatLng> latLngs = new ArrayList<>();

        if (type == 0) {
            //左下角坐标
            LatLng lowerLeft = new LatLng(centerLatLng.latitude - AppConstant.CITY_BLOCK_MARGIN / 2, centerLatLng.longitude - AppConstant.CITY_BLOCK_MARGIN / 2);
            //右上角坐标
            LatLng upperRight = new LatLng(centerLatLng.latitude + AppConstant.CITY_BLOCK_MARGIN / 2, centerLatLng.longitude + AppConstant.CITY_BLOCK_MARGIN / 2);
            latLngs.add(lowerLeft);
            latLngs.add(upperRight);

        } else if (type == 1) {
            //左上
            LatLng upperLeft = new LatLng(centerLatLng.latitude + AppConstant.CITY_BLOCK_MARGIN / 2, centerLatLng.longitude - AppConstant.CITY_BLOCK_MARGIN / 2);
            //右下
            LatLng lowerRight = new LatLng(centerLatLng.latitude - AppConstant.CITY_BLOCK_MARGIN / 2, centerLatLng.longitude + AppConstant.CITY_BLOCK_MARGIN / 2);
            latLngs.add(upperLeft);
            latLngs.add(lowerRight);
        }


        StringBuilder sb = new StringBuilder();
        sb.append(latLngs.get(0).latitude);
        sb.append(",");
        sb.append(latLngs.get(0).longitude);
        sb.append(",");
        sb.append(latLngs.get(1).latitude);
        sb.append(",");
        sb.append(latLngs.get(1).longitude);

        return sb.toString();

    }

    /**
     * 获取百度地图数据
     *
     * @param userId
     * @param platformBean
     * @param city
     * @param key
     * @param page_num
     * @return
     */
    private boolean getBaiDuMapData(List<LatLng> cityBlockLatLng, CollectCityBean collectCityBean, String userId, PlatformBean platformBean, String city, String key, int phoneType, int page_num, boolean isNextBlock) {

        if (BaseApp.isStopCollect) {
            //是否停止采集
            BaseApp.isStopCollect = false;
            MyUtlis.clickEvent(AppConstant.CLICK.umeng_stop_collect);
            return true;
        }
        //是否超出限制
        if (MyUtlis.isVipCollectCountBeyond()) {
            LogUtils.e("vip采集数量超出限制，已停止采集");
            return true;
        }


        //是否需要判断索引是否超出
        int currentIndex = 0;
        if (isNextBlock) {

            currentIndex = collectCityBean.getCurrentIndex() + 1;
            if (cityBlockLatLng.size() <= currentIndex) {
                currentIndex = 0;
            }
            //记录更新当前点
            collectCityBean.setCurrentIndex(currentIndex);
            DaoUtlis.addCollectCity(collectCityBean);
        } else {
            currentIndex = collectCityBean.getCurrentIndex();
        }
        //获取当前要采集的块
        LatLng latLng = cityBlockLatLng.get(currentIndex);
        //获取当前采集的矩形范围
        String bounds = getBounds(latLng, 0);

        LogUtils.e("总块数=" + cityBlockLatLng.size());
        LogUtils.e("当前块数=" + currentIndex + " 当前页数=" + page_num);

        LogUtils.e("获取当前采集的矩形范围=" + bounds);

        JsonBean_BaiDuMap baiDuMapData = httpGetBaiDuMapData(bounds, city, key, page_num);


        if (null == baiDuMapData) {

            return false;
        }
        //先将数据格式化
        List<JsonBean_BaiDuMap.ResultsBean> results = baiDuMapData.getResults();
        if (results == null)
            return false;

        if (baiDuMapData.getTotal() != 0) {
            List<DataBean> dataBeanList = new ArrayList<DataBean>();
            for (int i = 0; i < results.size(); i++) {
                try {
                    JsonBean_BaiDuMap.ResultsBean resultsBean = results.get(i);
                    DataBean dataBean = new DataBean();
                    dataBean.setDataId(platformBean.platformId + resultsBean.getUid());
                    dataBean.setUserId(userId);
                    dataBean.setUId(resultsBean.getUid());
                    dataBean.setPlatformId(platformBean.platformId);
                    dataBean.setSource(platformBean.name);
                    dataBean.setName(resultsBean.getName());
                    dataBean.setProvince(resultsBean.getProvince());
                    dataBean.setCity(resultsBean.getCity());
                    dataBean.setDistrict(resultsBean.getArea());
                    dataBean.setStreet(resultsBean.getStreet_id());
                    dataBean.setKey(key);

                    String telephone = resultsBean.getTelephone();

                    if (TextUtils.isEmpty(telephone)) {
                        //1.假设电话数据为空,直接过滤掉该条数据 这个没毛病
                        continue;
                    }
                    //2.处理电话中包含座机与手机,用,隔开的数据
                    if (telephone.contains(",")) {
                        String[] split = telephone.split(",");
                        //切割后遍历+判断类型
                        for (String p : split) {
                            if (TextUtils.isEmpty(p)) {
                                continue;
                            }
                            if (RegexUtils.isMobileExact(p)) {
                                //手机
                                dataBean.setPhone(p);
                            } else if (p.length() > 3) {
                                //座机
                                dataBean.setTel(p);
                            }
                        }

                    } else {
                        //不包含,
                        if (RegexUtils.isMobileExact(telephone)) {
                            dataBean.setPhone(telephone);
                        } else if (telephone.length() > 3) {
                            dataBean.setTel(telephone);
                        }
                    }

                    //电话字段赋值完毕,现在来根据设置的采集目标数据电话类型做判断,
//                    if (phoneType == AppConstant.COLLECT_DATA_PHONE_TYPE_ALL) {
//                        //只要有1个就可以, 都没有的话,前面的逻辑已经过滤掉了数据
//                    } else if (phoneType == AppConstant.COLLECT_DATA_PHONE_TYPE_PHONE_TEL) {
//                        //固话手机都要,有一个为空都不行
//                        if (TextUtils.isEmpty(dataBean.getPhone()) || TextUtils.isEmpty(dataBean.getTel())) {
//                            continue;
//                        }
//                    } else if (phoneType == AppConstant.COLLECT_DATA_PHONE_TYPE_PHONE) {
//                        //必须要有手机
//                        if (TextUtils.isEmpty(dataBean.getPhone())) {
//                            continue;
//                        }
//                    }

                    dataBean.setAddress(MyUtlis.getAddress(resultsBean.getAddress(), resultsBean.getCity(), resultsBean.getArea()));
                    dataBean.setAddress_details(dataBean.getAddress());
                    dataBeanList.add(dataBean);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
            //sendMessage
            MyUtlis.eventCollectData(platformBean.platformId, dataBeanList);

            DaoUtlis.addData(dataBeanList);
        }
//判断数据是否获取完成
        if (results.size() >= 20) {
            //当前块还有数据继续请求
            return getBaiDuMapData(cityBlockLatLng, collectCityBean, userId, platformBean, city, key, phoneType, page_num + 1, false);
        } else if (results.size() < 20) {
            //当前块没有数据了,去下一块
            return getBaiDuMapData(cityBlockLatLng, collectCityBean, userId, platformBean, city, key, phoneType, 0, true);
        }
        return false;

    }


    /**
     * 获取高德地图数据
     *
     * @param userId
     * @param platformBean
     * @param city
     * @param key
     * @param page_num
     * @return
     */
    private boolean getGaoDeMapData(List<LatLng> cityBlockLatLng, CollectCityBean collectCityBean, String userId, PlatformBean platformBean, String city, String key, int phoneType, int page_num, boolean isNextBlock) {


        if (BaseApp.isStopCollect) {
            //停止采集
            BaseApp.isStopCollect = false;
            MyUtlis.clickEvent(AppConstant.CLICK.umeng_stop_collect);
            return true;
        }
        //是否超出限制
        if (MyUtlis.isVipCollectCountBeyond()) {
            LogUtils.e("vip采集数量超出限制，已停止采集");
            return true;
        }

        //是否需要判断索引是否超出
        int currentIndex = 0;
        if (isNextBlock) {

            currentIndex = collectCityBean.getCurrentIndex() + 1;
            if (cityBlockLatLng.size() <= currentIndex) {
                currentIndex = 0;
            }
            //记录更新当前点
            collectCityBean.setCurrentIndex(currentIndex);
            DaoUtlis.addCollectCity(collectCityBean);
        } else {
            currentIndex = collectCityBean.getCurrentIndex();
        }
        //获取当前要采集的块
        LatLng latLng = cityBlockLatLng.get(currentIndex);
        //获取当前采集的矩形范围
//        String bounds = getBounds(latLng, 1);
//
        LogUtils.e("总块数=" + cityBlockLatLng.size());
        LogUtils.e("当前块数=" + currentIndex + " 当前页数=" + page_num);
//
//        LogUtils.e("获取当前采集的矩形范围=" + bounds);

        JsonBean_GaoDeMap gaoDeMapData = httpGetGaoDeMapData(latLng, city, key, page_num);


        if (null == gaoDeMapData) {

            return false;
        }
        //先将数据格式化
        List<JsonBean_GaoDeMap.PoisBean> results = gaoDeMapData.getPois();
        if (results == null)
            return false;

        if (gaoDeMapData.getCount() != 0) {
            List<DataBean> dataBeanList = new ArrayList<DataBean>();
            for (int i = 0; i < results.size(); i++) {
                try {
                    JsonBean_GaoDeMap.PoisBean poisBean = results.get(i);
                    DataBean dataBean = new DataBean();
                    dataBean.setDataId(platformBean.platformId + poisBean.getId());
                    dataBean.setUserId(userId);
                    dataBean.setUId(poisBean.getId());
                    dataBean.setPlatformId(platformBean.platformId);
                    dataBean.setSource(platformBean.name);
                    dataBean.setName(poisBean.getName());
                    dataBean.setProvince(poisBean.getPname());
                    dataBean.setCity(poisBean.getCityname());
                    dataBean.setDistrict(poisBean.getAdname());
//                    dataBean.setStreet(""); //高德地图没有街道字段? 这个字段也没用到,先就这样吧
                    dataBean.setKey(key);

                    //电话的处理方式统一
                    String telephone = poisBean.getTel();
//                    LogUtils.e("telephone=" + telephone);
                    if (TextUtils.isEmpty(telephone) || telephone.length() < 3 || telephone == "[]") {
                        //1.假设电话数据为空,直接过滤掉该条数据
                        continue;
                    }
                    //2.处理电话中包含座机与手机,用;隔开的数据 ? 高德是; 百度是,
                    if (telephone.contains(";")) {
                        String[] split = telephone.split(";");
                        //切割后遍历+判断类型
                        for (String p : split) {
                            if (TextUtils.isEmpty(p)) {
                                continue;
                            }
                            if (RegexUtils.isMobileExact(p)) {
                                //手机
                                dataBean.setPhone(p);
                            } else if (p.length() > 3) {
                                //座机
                                dataBean.setTel(p);
                            }
                        }

                    } else {
                        //不包含,
                        if (RegexUtils.isMobileExact(telephone)) {
                            dataBean.setPhone(telephone);
                        } else if (telephone.length() > 3) {
                            dataBean.setTel(telephone);
                        }
                    }

                    //电话字段赋值完毕,现在来根据设置的采集目标数据电话类型做判断,
//                    if (phoneType == AppConstant.COLLECT_DATA_PHONE_TYPE_ALL) {
//                        //只要有1个就可以, 都没有的话,前面的逻辑已经过滤掉了数据
//                    } else if (phoneType == AppConstant.COLLECT_DATA_PHONE_TYPE_PHONE_TEL) {
//                        //固话手机都要,有一个为空都不行
//                        if (TextUtils.isEmpty(dataBean.getPhone()) || TextUtils.isEmpty(dataBean.getTel())) {
//                            continue;
//                        }
//                    } else if (phoneType == AppConstant.COLLECT_DATA_PHONE_TYPE_PHONE) {
//                        //必须要有手机
//                        if (TextUtils.isEmpty(dataBean.getPhone())) {
//                            continue;
//                        }
//                    }

                    dataBean.setAddress(MyUtlis.getAddress(poisBean.getAddress(), poisBean.getCityname(), poisBean.getAdname()));
                    dataBean.setAddress_details(dataBean.getAddress());
                    dataBeanList.add(dataBean);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
            //sendMessage
            MyUtlis.eventCollectData(platformBean.platformId, dataBeanList);
            DaoUtlis.addData(dataBeanList);
        }
//判断数据是否获取完成
        if (results.size() >= 20) {
            //当前块还有数据继续请求
            return getGaoDeMapData(cityBlockLatLng, collectCityBean, userId, platformBean, city, key, phoneType, page_num + 1, false);
        } else if (results.size() < 20) {
            //当前块没有数据了,去下一块
            return getGaoDeMapData(cityBlockLatLng, collectCityBean, userId, platformBean, city, key, phoneType, 0, true);
        }
        return false;

    }

    /**
     * 获取高德地图数据
     *
     * @param centerLatLng 百度的城市中心点
     * @param city
     * @param key
     * @param pageNum
     * @return
     */
    private JsonBean_GaoDeMap httpGetGaoDeMapData(LatLng centerLatLng, String city, String key, int pageNum) {


        MyUtlis.clickEvent(AppConstant.CLICK.umeng_collect_request);


        //左上
        LatLng upperLeft = new LatLng(centerLatLng.latitude + AppConstant.CITY_BLOCK_MARGIN / 2, centerLatLng.longitude - AppConstant.CITY_BLOCK_MARGIN / 2);
        upperLeft = MyUtlis.bdToGaoDe(upperLeft);
        //右下
        LatLng lowerRight = new LatLng(centerLatLng.latitude - AppConstant.CITY_BLOCK_MARGIN / 2, centerLatLng.longitude + AppConstant.CITY_BLOCK_MARGIN / 2);
        lowerRight = MyUtlis.bdToGaoDe(lowerRight);
        StringBuilder sb = new StringBuilder();
        sb.append(upperLeft.latitude);
        sb.append(",");
        sb.append(upperLeft.longitude);
        sb.append(",");
        sb.append(lowerRight.latitude);
        sb.append(",");
        sb.append(lowerRight.longitude);

        String polygon = sb.toString();
        LogUtils.e("获取当前采集的矩形范围=" + polygon);

        //子线程中, 同步请求
//        String url = "http://restapi.amap.com/v3/place/text";
        String url = "https://restapi.amap.com/v3/place/polygon";
//       文档： https://lbs.amap.com/api/webservice/guide/api/search#polygon
        //city=深圳 广东
        //高德的city字段不能出现省份, 只需要城市
//        LogUtils.e("city1=" + city);
//        city = city.contains(" ") ? city.substring(city.indexOf(" "), city.length()) : city;
//        LogUtils.e("city2=" + city);
        try {
            okhttp3.Response response = OkGo.<JsonBean_GaoDeMap>get(url).tag(this)
                    .params("key", AppConstant.Platform.KEY_GAODE_MAP)
                    .params("polygon", polygon)
                    .params("keywords", key)
                    .params("types", "")
                    .params("city", city)
                    .params("children", "")
                    .params("offset", 20)
                    .params("page", pageNum)
                    .params("extensions", "all")
                    .execute();

            String result = response.body().string();
//            LogUtils.e("高度地图=" + result);
            return JsonUtlis.fromJsonGaoDe(result);
//            return new Gson().fromJson(result, JsonBean_GaoDeMap.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 获取百度地图数据
     *
     * @param bounds
     * @param city
     * @param key
     * @param pageNum
     * @return
     */
    private JsonBean_BaiDuMap httpGetBaiDuMapData(String bounds, String city, String key, int pageNum) {


        MyUtlis.clickEvent(AppConstant.CLICK.umeng_collect_request);

        //子线程中, 同步请求
        String url = "http://api.map.baidu.com/place/v2/search";


        try {
            okhttp3.Response response = OkGo.<JsonBean_BaiDuMap>get(url).tag(this)
                    .params("query", key)
                    //                .params("bounds", bounds)
                    .params("bounds", bounds)
                    .params("output", "json")
                    .params("page_size", 20)//每页数据量
                    .params("page_num", pageNum)
                    .params("ak", AppConstant.Platform.KEY_BAIDU_MAP)
                    .execute();


            String result = response.body().string();
            return new Gson().fromJson(result, JsonBean_BaiDuMap.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 采集高德地图数据
     *
     * @param city
     * @param key
     * @param listener
     */
    private void collectGaoDeMap(final PlatformBean platformBean, final String city, final String key, final int phoneType, final OnCollectListener listener) {
        LogUtils.e("开始采集，高德数据");

        final String userId = MyUtlis.getLoginUserId();

        // city+key 组成唯一码，记录当前city采集的index， （暂不区分鱼塘）


        //1.先根据city 反编码获取城市的中心点经纬度
        //2.根据中心经纬度，开始分块
        //3.根据city+key获取当前city采集到哪个位置，从下一个位置开始采集

        //获取行政区域中心和边界
        DistrictSearch mDistrictSearch = DistrictSearch.newInstance();
        mDistrictSearch.setOnDistrictSearchListener(new OnGetDistricSearchResultListener() {
            @Override
            public void onGetDistrictResult(DistrictResult districtResult) {
                //城市反编码回调监听

                if (null == districtResult) {
                    LogUtils.e("获取行政区域中心坐标 districtResult=null");
                    return;
                }
                if (null == districtResult.getCenterPt()) {
                    LogUtils.e("获取行政区域中心坐标 districtResult.getCenterPt()=null");
                    return;
                }


                final List<LatLng> cityBlockLatLng = getCityBlockLatLng(districtResult.getCenterPt());

                if (null == cityBlockLatLng || cityBlockLatLng.isEmpty()) {
                    LogUtils.e("城市分块数据为空");
                    return;
                }

                CollectCityBean collectCity = DaoUtlis.getCollectCity(city + key, userId);
                //如果为空,新建
                if (null == collectCity) {
                    collectCity = new CollectCityBean();
                    collectCity.setCollectCity(city + key);
                    collectCity.setKey(key);
                    collectCity.setCity(city);
                    collectCity.setBlockCount(cityBlockLatLng.size());
                    collectCity.setCurrentIndex(0);
                    collectCity.setUserId(userId);
                    collectCity.setPlatformId(platformBean.platformId);
                    DaoUtlis.addCollectCity(collectCity);
                    LogUtils.e("新建采集城市数据=" + collectCity.getCollectCity());
                }


                //开始在子线程采集？
                final CollectCityBean finalCollectCity = collectCity;
                ThreadUtils.executeByFixed(AppConstant.THREAD_SIZE, new ThreadUtils.SimpleTask<Boolean>() {
                    @Override
                    public Boolean doInBackground() {
                        return getGaoDeMapData(cityBlockLatLng, finalCollectCity, userId, platformBean, city, key, phoneType, 0, finalCollectCity.getCurrentIndex() != 0);
                    }

                    @Override
                    public void onSuccess(Boolean result) {
//                MyUtlis.eventCollectComplete();
                        if (result) {
                            listener.onCollectComplete();
                        } else {
                            listener.onCollectError();
                        }

                    }
                });


            }
        });

        DistrictSearchOption districtSearchOption = new DistrictSearchOption();
        districtSearchOption.cityName(city);//检索城市名称
//        districtSearchOption.districtName(districtStr);//检索的区县名称
        mDistrictSearch.searchDistrict(districtSearchOption);//请求行政区数据


    }


    /**
     * 采集美团数据
     *
     * @param city
     * @param key
     * @param listener
     */
    private void collectMeiTuan(String city, String key, OnCollectListener listener) {
        LogUtils.e("collect MeiTuan");


        listener.onCollect(MyUtlis.getTestData(10, mPlatformBean));
        //数据采集完成
        listener.onCollectComplete();

    }

    /**
     * 采集饿了么数据
     *
     * @param city
     * @param key
     * @param listener
     */
    private void collectELeMe(String city, String key, OnCollectListener listener) {
        LogUtils.e("collect ELeMe");
        listener.onCollect(MyUtlis.getTestData(10, mPlatformBean));
        //数据采集完成
        listener.onCollectComplete();
    }

    /**
     * 采集淘宝商城数据
     *
     * @param city
     * @param key
     * @param listener
     */
    private void collectTaoBao(String city, String key, OnCollectListener listener) {
        LogUtils.e("collect TaoBao");
        listener.onCollect(MyUtlis.getTestData(10, mPlatformBean));
        //数据采集完成
        listener.onCollectComplete();
    }

    /**
     * 采集京东数据
     *
     * @param city
     * @param key
     * @param listener
     */
    private void collectJD(String city, String key, OnCollectListener listener) {
        LogUtils.e("collect JD");
        listener.onCollect(MyUtlis.getTestData(10, mPlatformBean));
        //数据采集完成
        listener.onCollectComplete();
    }

    /**
     * 采集唯品会数据
     *
     * @param city
     * @param key
     * @param listener
     */
    private void collectWeiPinHui(String city, String key, OnCollectListener listener) {
        LogUtils.e("collect WeiPinHui");
        listener.onCollect(MyUtlis.getTestData(10, mPlatformBean));
        //数据采集完成
        listener.onCollectComplete();
    }
}
