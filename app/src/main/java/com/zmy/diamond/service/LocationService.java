package com.zmy.diamond.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.blankj.utilcode.util.LogUtils;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.LocationBean;

/**
 * 定位服务
 * Created by zhangmengyun on 2018/6/19.
 */

public class LocationService extends Service {


    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    //一次服务启动 最大定位次数.即失败超过10次就关闭定位
    public int MAX_LOCATRION_COUNT = 10;
    //当前定位次数
    public int locationCount;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化定位代码
        LogUtils.e("定位服务 onCreate");
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
//可选，设置定位模式，默认高精度
//LocationMode.Hight_Accuracy：高精度；
//LocationMode. Battery_Saving：低功耗；
//LocationMode. Device_Sensors：仅使用设备；
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
        option.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setCoorType("bd09ll");

//可选，设置返回经纬度坐标类型，默认gcj02
//gcj02：国测局坐标；
//bd09ll：百度经纬度坐标；
//bd09：百度墨卡托坐标；
//海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标

        option.setScanSpan(10000);
//可选，设置发起定位请求的间隔，int类型，单位ms
//如果设置为0，则代表单次定位，即仅定位一次，默认为0
//如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(false);
//可选，设置是否使用gps，默认false
//使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(false);
//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(true);
//可选，定位SDK内部是一个service，并放到了独立进程。
//设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
//可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
//可选，7.2版本新增能力
//如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位

        option.setEnableSimulateGps(false);
//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        mLocationClient.setLocOption(option);
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明


        mLocationClient.start();
        LogUtils.e("开始定位");
//mLocationClient为第二步初始化过的LocationClient对象
//调用LocationClient的start()方法，便可发起定位请求
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            locationCount++;
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                LocationBean locationBean = MyUtlis.saveLocationInfo(location);
                MyUtlis.eventUpdateLocation(locationBean);
                mLocationClient.stop();
                stopSelf();
            } else {
                LogUtils.e("定位失败 :" + (location == null ? "location=null" : "BDLocation.TypeServerError=" + (location.getLocType() == BDLocation.TypeServerError)));
                if (locationCount >= MAX_LOCATRION_COUNT) {
                    mLocationClient.stop();
                    stopSelf();
                }
            }

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.e("定位服务 onStartCommand");

//        if (null != mLocationClient) {
//            mLocationClient.start();
//        }
        return super.onStartCommand(intent, flags, startId);

//mLocationClient为第二步初始化过的LocationClient对象
//调用LocationClient的start()方法，便可发起定位请求
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.e("定位服务 onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.e("定位服务 onBind");
        return null;
    }
}
