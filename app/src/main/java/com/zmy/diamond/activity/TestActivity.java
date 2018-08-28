package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zmy.diamond.R;
import com.zmy.diamond.base.MyBaseSwipeBackActivity;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.JsonBean_BaiDuMap;
import com.zmy.diamond.utli.bean.LocationBean;
import com.zmy.diamond.utli.dao.DaoUtlis;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhangmengyun on 2018/7/28.
 */

public class TestActivity extends MyBaseSwipeBackActivity {

    double margin = 0.018409; //大约2公里  1000=2000平方公里


    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;


    @BindView(R.id.edit_key)
    EditText edit_key;
    @BindView(R.id.edit_city)
    EditText edit_city;


    @BindView(R.id.mmap)
    MapView mMapView;
    private BaiduMap mBaiduMap;


    @OnClick(R.id.tv_back)
    public void tv_back() {
        ActivityUtils.finishActivity(this);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, TestActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void initUI() {
        setContentView(R.layout.activity_test);
        super.initUI();
        tv_back.setTypeface(MyUtlis.getTTF());
        tv_title.setText("测试");

        mBaiduMap = mMapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);


        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Bundle args = marker.getExtraInfo();


                ToastUtils.showShort(args.get("siteId").toString());


                return false;
            }

        });

    }

    @Override
    public void initData() {


        LocationBean location = DaoUtlis.getLocation();

        if (null != location) {


            //定位
            // 开启定位图层
            mBaiduMap.setMyLocationEnabled(true);

// 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLontitude()).build();

// 设置定位数据
            mBaiduMap.setMyLocationData(locData);
        }
// 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
//        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
//                .fromResource(R.drawable.d_img_logo);
//        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker);
//        mBaiduMap.setMyLocationConfiguration(config);

// 当不需要定位图层时关闭定位图层
//        mBaiduMap.setMyLocationEnabled(false);


    }

    @OnClick(R.id.btn_get_location)
    public void btn_get_location() {

        String city = edit_city.getText().toString().trim();

        LogUtils.e("输入的城市=" + city);
        //地理位置反编码


        if (TextUtils.isEmpty(city)) {
            ToastUtils.showShort("city不能为null");
            return;
        }

        //获取行政区域中心和边界
        DistrictSearch mDistrictSearch = DistrictSearch.newInstance();
        mDistrictSearch.setOnDistrictSearchListener(listener);//设置回调监听

        DistrictSearchOption districtSearchOption = new DistrictSearchOption();
        districtSearchOption.cityName(city);//检索城市名称

//        districtSearchOption.districtName(districtStr);//检索的区县名称
        mDistrictSearch.searchDistrict(districtSearchOption);//请求行政区数据

    }

    List<List<LatLng>> polyLines;
    LatLng centerPt;
    OnGetDistricSearchResultListener listener = new OnGetDistricSearchResultListener() {
        @Override
        public void onGetDistrictResult(DistrictResult districtResult) {
            if (districtResult.error == SearchResult.ERRORNO.NO_ERROR) {


                if (null == districtResult) {
                    ToastUtils.showShort("行政区域获取结果为空");
                    return;
                }

                centerPt = districtResult.getCenterPt();//获取行政区中心坐标点


                districtResult.getCityName();//获取行政区域名称
                //获取行政区域边界坐标点
                polyLines = districtResult.getPolylines();


                if (polyLines == null) {
                    ToastUtils.showShort("行政区域获取结果为空");
                    return;
                }

                mBaiduMap.clear();
                LatLngBounds.Builder builder = new LatLngBounds.Builder();


                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < polyLines.size(); i++) {

                    List<LatLng> latLngs = polyLines.get(i);

//                    sb.append("第" + i + "组数据*******\n");

                    PolylineOptions ooPolyline11 = new PolylineOptions().width(2)
                            .points(latLngs).dottedLine(true).color(Color.RED);
                    mBaiduMap.addOverlay(ooPolyline11);

                    for (LatLng latLng : latLngs) {

//                        sb.append(latLng.longitude + "," + latLng.latitude + "\n");

//                        LogUtils.e("经度=" + latLng.longitude + " 纬度=" + latLng.latitude);
                        builder.include(latLng);
                    }
                }
                mBaiduMap.setMapStatus(MapStatusUpdateFactory
                        .newLatLngBounds(builder.build()));


//                LogUtils.e(sb.toString());

                LogUtils.e("中心坐标=" + centerPt.longitude + "," + centerPt.latitude);

                //获取四个角坐标
                LatLngBounds build = builder.build();
                LatLng northeast = build.northeast;//东北角
                LatLng southwest = build.southwest;//西南角

                LogUtils.e("东北角=" + northeast.longitude + "," + northeast.latitude);
                LogUtils.e("西南角=" + southwest.longitude + "," + southwest.latitude);

//                LogUtils.e("东=" + northeast.longitude);
//                LogUtils.e("南=" + southwest.latitude);
//                LogUtils.e("西=" + southwest.longitude);
//                LogUtils.e("北=" + northeast.latitude);


//                double v = AreaUtil.calculateArea(northeast, southwest);
//
//                LogUtils.e("面积=" + v + "平方米");


                //从中心范围扩散，不超出城市最大边界， 暂以2公里*2公里分割为一个小块，（可配置)
                //分割的时候就以矩阵的形式分割， 然后按顺序存储到list
                //开始记录每次获取数据的坐标，下次从那个地方再开始。


            } else {

                ToastUtils.showShort("行政区域获取错误！！！");
            }

        }
    };


    /**
     * 判断点pt是否在mPoints构成的多边形内。 默认不存在
     *
     * @param polyLines
     * @param pt
     * @return
     */
    public boolean isPolygonContainsPoint2(List<List<LatLng>> polyLines, LatLng pt) {
        for (List<LatLng> latLngs : polyLines) {
            if (isPolygonContainsPoint(latLngs, pt)) {
                //存在
                return true;
            }
        }
        return false;
    }

    /**
     * 判断点pt是否在mPoints构成的多边形内。 默认不存在
     *
     * @param latLngs
     * @param pt
     * @return
     */
    public boolean isPolygonContainsPoint(List<LatLng> latLngs, LatLng pt) {
        return SpatialRelationUtil.isPolygonContainsPoint(latLngs, pt);
    }

    @OnClick(R.id.btn_block)
    public void btn_block() {


        if (null == centerPt) {
            ToastUtils.showShort("请先获取城市经纬度");
            return;
        }

        //开始切块

        getLatLngList(centerPt);
        //方案1
        //1.获取城市的行政区域边界数据
        //2.通过固定的算法， 将区域划分为若干个小格， 暂定小格范围 2km*2km
        //3.采集数据时，从中心往外扩散采集， 并且记录采集的位置，下次从该位置往后继续采集。

        //方案2
        //1.四叉树切割， 获取全城数据，大于400，进行切割成4块，开启线程继续递归，直到小于400
        /**
         * 方案1， 缺点：
         *      1.会有极少量丢失数据
         * 方案2 ， 缺点：
         *      1.采集大量重复数据。
         *      2.没有办法记录采集位置
         *      3.并发严重
         */


    }

    @OnClick(R.id.btn_start)
    public void btn_start() {

        for (int i = 0; i < latLngList.size(); i++) {

//            http://api.map.baidu.com/place/v2/search?query=银行&bounds=39.915,116.404,39.975,116.414&output=json&ak={您的密钥} //GET请求


//


            List<LatLng> bounds = getBounds(latLngList.get(i));

            StringBuilder sb = new StringBuilder();
            sb.append("http://api.map.baidu.com/place/v2/search?query=");
            sb.append(edit_key.getText().toString().trim());
            sb.append("&bounds=");

            sb.append(bounds.get(0).latitude);
            sb.append(",");
            sb.append(bounds.get(0).longitude);
            sb.append(",");
            sb.append(bounds.get(1).latitude);
            sb.append(",");
            sb.append(bounds.get(1).longitude);
            sb.append("&page_size=20");
            sb.append("&page_num=");
            sb.append(1);
            sb.append("&output=json&ak=");
            sb.append(AppConstant.Platform.KEY_BAIDU_MAP);
            String url = sb.toString();
            LogUtils.e(url);

            OkGo.<String>get(url).execute(new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {
                    LogUtils.e(response.body());
                }
            });


        }


    }


    public void startCollet(LatLng centerPt, int pageNum) {
        List<LatLng> bounds = getBounds(centerPt);

        StringBuilder sb = new StringBuilder();
        sb.append("http://api.map.baidu.com/place/v2/search?query=");
        sb.append(edit_key.getText().toString().trim());
        sb.append("&bounds=");

        sb.append(bounds.get(0).latitude);
        sb.append(",");
        sb.append(bounds.get(0).longitude);
        sb.append(",");
        sb.append(bounds.get(1).latitude);
        sb.append(",");
        sb.append(bounds.get(1).longitude);
        sb.append("&page_size=20");
        sb.append("&page_num=");
        sb.append(pageNum);
        sb.append("&output=json&ak=");
        sb.append(AppConstant.Platform.KEY_BAIDU_MAP);
        String url = sb.toString();
        LogUtils.e(url);

        OkGo.<String>get(url).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {

//                FileIOUtils.writeFileFromString(getTextFile(),)
                JsonBean_BaiDuMap bean = new Gson().fromJson(response.body(), JsonBean_BaiDuMap.class);

                bean.getStatus();
                bean.getMessage();
                List<JsonBean_BaiDuMap.ResultsBean> results = bean.getResults();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < results.size(); i++) {

                    JsonBean_BaiDuMap.ResultsBean poiBean = results.get(i);
                    sb.append("名称=");
                    sb.append(poiBean.getName());
                    sb.append("\n地址=");
                    sb.append(poiBean.getAddress());
                    sb.append("\n电话=");
                    sb.append(poiBean.getTelephone());
                    sb.append("\n**************************************\n");

                }
                FileIOUtils.writeFileFromString(filePath, results.toString(), true);
//                LogUtils.e(response.body());
                //保存到文本后， 判断此次请求


            }
        });
    }

    String filePath;

    private void getTextFile() {
        filePath = MyUtlis.getAppFileDirPath() + File.separator + "test_data.txt";
        if (FileUtils.isFileExists(filePath)) {
            FileUtils.deleteFile(filePath);
            FileUtils.createOrExistsFile(filePath);
        }


    }


    public List<LatLng> latLngList;

    public void getLatLngList(LatLng centerLatLng) {
        int pNum = 0;
        int num = 1;
        latLngList = new ArrayList<>();

        latLngList.add(centerLatLng);
        LogUtils.e("开始=" + TimeUtils.getNowString());
        while (pNum < 1001) {
            for (int i = 0; i < num; i++) {
//                y=竖=纬度=latitude  +=向上  -=向下
//                        x=横向=经度=longitude +=向右 -=向左;
                //在当前位置上，向右移动2公里 ， 次数=num
                centerLatLng = new LatLng(centerLatLng.latitude, centerLatLng.longitude + margin);
                latLngList.add(centerLatLng);
                pNum++;
            }

            for (int i = 0; i < num; i++) {
                //在当前位置上，向上移动2公里  次数=num
                centerLatLng = new LatLng(centerLatLng.latitude + margin, centerLatLng.longitude);
                latLngList.add(centerLatLng);
                pNum++;
            }
            //循环 ++
            num++;

            for (int i = 0; i < num; i++) {
                //在当前位置上，向左每次移动2公里 次数=num
                centerLatLng = new LatLng(centerLatLng.latitude, centerLatLng.longitude - margin);
                latLngList.add(centerLatLng);
                pNum++;
            }

            for (int i = 0; i < num; i++) {
                //   //在当前位置上，向下每次移动2公里 次数=num
                centerLatLng = new LatLng(centerLatLng.latitude - margin, centerLatLng.longitude);
                latLngList.add(centerLatLng);
                pNum++;
            }
            //循环 ++
            num++;
        }

        LogUtils.e("结束=" + TimeUtils.getNowString());
        LogUtils.e("块数=" + latLngList.size());
        LogUtils.e("第一块的经纬度=" + latLngList.get(0).longitude + "," + latLngList.get(0).latitude);
        LogUtils.e("最后块的经纬度=" + latLngList.get(latLngList.size() - 1).longitude + "," + latLngList.get(latLngList.size() - 1).latitude);


        //计算一下 0-1的距离
        double distance = DistanceUtil.getDistance(latLngList.get(0), latLngList.get(1));

        LogUtils.e("临近2点距离=" + distance + "米");


        mBaiduMap.clear();
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < latLngList.size(); i++) {
            LatLng latLng = latLngList.get(i);

            sb.append(i);
            sb.append("=");
            sb.append(latLng.longitude);
            sb.append(",");
            sb.append(latLng.latitude);
            Log.e("", sb.toString());

            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.dot);

            Bundle args = new Bundle(); //这个用于在所画的点上附加相关信息
            args.putString("siteId", String.valueOf(i));
            args.putString("siteName", String.valueOf(i));
            OverlayOptions option = new MarkerOptions().position(latLng).extraInfo(args).icon(bitmap);
//            builder.include(latLng);
            mBaiduMap.addOverlay(option);

            //现在画块看看对不对
            List<LatLng> bounds = getBounds(latLng);

            for (int x = 0; x < bounds.size(); x++) {
                BitmapDescriptor bitmap2 = BitmapDescriptorFactory.fromResource(R.mipmap.dot2);
                Bundle args2 = new Bundle(); //这个用于在所画的点上附加相关信息
                args2.putString("siteId", i + "-" + x);
                args2.putString("siteName", i + "-" + x);
                OverlayOptions option2 = new MarkerOptions().position(bounds.get(x)).extraInfo(args2).icon(bitmap2);
//            builder.include(latLng);
                mBaiduMap.addOverlay(option2);
            }


        }
//        PolylineOptions ooPolyline11 = new PolylineOptions().width(2)
//                .points(latLngList).dottedLine(true).color(Color.RED);
//        mBaiduMap.addOverlay(ooPolyline11);
//        mBaiduMap.setMapStatus(MapStatusUpdateFactory
//                .newLatLngBounds(builder.build()));


        //现在已经获取到每个点的矩阵列表， 每点距离2公里


    }


    /**
     * 根据当前中心点坐标，获取2km*2km的范围， 左下角坐标和右上角坐标
     *
     * @return LatLng[] 0=左下1=右上
     */
    public List<LatLng> getBounds(LatLng centerLatLng) {
        List<LatLng> latLngs = new ArrayList<>();
        //左下角坐标
        LatLng lowerLeft = new LatLng(centerLatLng.latitude - margin / 2, centerLatLng.longitude - margin / 2);
        //右上角坐标
        LatLng upperRight = new LatLng(centerLatLng.latitude + margin / 2, centerLatLng.longitude + margin / 2);
        latLngs.add(lowerLeft);
        latLngs.add(upperRight);
        return latLngs;

    }


}
