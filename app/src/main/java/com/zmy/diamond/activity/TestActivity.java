package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;
import com.baidu.mapapi.utils.AreaUtil;
import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zmy.diamond.R;
import com.zmy.diamond.base.MyBaseSwipeBackActivity;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.LocationBean;
import com.zmy.diamond.utli.dao.DaoUtlis;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhangmengyun on 2018/7/28.
 */

public class TestActivity extends MyBaseSwipeBackActivity {


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
    OnGetDistricSearchResultListener listener = new OnGetDistricSearchResultListener() {
        @Override
        public void onGetDistrictResult(DistrictResult districtResult) {
            if (districtResult.error == SearchResult.ERRORNO.NO_ERROR) {


                if (null == districtResult) {
                    ToastUtils.showShort("行政区域获取结果为空");
                    return;
                }

                LatLng centerPt = districtResult.getCenterPt();//获取行政区中心坐标点


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

                    sb.append("第" + i + "组数据*******\n");

                    PolylineOptions ooPolyline11 = new PolylineOptions().width(2)
                            .points(latLngs).dottedLine(true).color(Color.RED);
                    mBaiduMap.addOverlay(ooPolyline11);

                    for (LatLng latLng : latLngs) {

                        sb.append(latLng.longitude + "," + latLng.latitude + "\n");

//                        LogUtils.e("经度=" + latLng.longitude + " 纬度=" + latLng.latitude);
                        builder.include(latLng);
                    }
                }
                mBaiduMap.setMapStatus(MapStatusUpdateFactory
                        .newLatLngBounds(builder.build()));


                LogUtils.e(sb.toString());

                LogUtils.e("中心坐标=" + centerPt.longitude + "," + centerPt.latitude);

                //获取四个角坐标
                LatLngBounds build = builder.build();
                LatLng northeast = build.northeast;//东北角
                LatLng southwest = build.southwest;//西南角

                LogUtils.e("东北角=" + northeast.longitude + "," + northeast.latitude);
                LogUtils.e("西南角=" + southwest.longitude + "," + southwest.latitude);

                LogUtils.e("东=" + northeast.longitude);
                LogUtils.e("南=" + southwest.latitude);
                LogUtils.e("西=" + southwest.longitude);
                LogUtils.e("北=" + northeast.latitude);


                double v = AreaUtil.calculateArea(northeast, southwest);

                LogUtils.e("面积=" + v + "平方米");


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


        if (null == polyLines) {
            ToastUtils.showShort("请先获取城市经纬度");
            return;
        }

        //开始切块


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

    }


}
