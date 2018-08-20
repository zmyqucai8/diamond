package com.zmy.diamond.utli.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by zhangmengyun on 2018/6/19.
 */
@Entity
public class LocationBean {

    @Id
    @Unique
    public Long id;
    //定位时间  2018-06-19 14:48:44
    public String time;
    // 定位类型
    public int locType;
    // *****对应的定位类型说明*****
    public String locTypeDescription;
    // 纬度
    public double latitude;
    // 经度
    public double lontitude;
    //    半径
    public float radius;
    // 国家码
    public String countryCode;
    //国家名称
    public String country;
    //城市编码
    public String citycode;
    //城市
    public String city;
    // 区
    public String district;
    // 街道
    public String street;
    //地址信息
    public String addr;
    // *****返回用户室内外判断结果*****
    public int userIndoorState;
    // 方向 并非所有的设备都有值
    public float direction;
    // 位置语义化信息 : 科技园附近
    public String locationdescribe;
    //poi name ,这里只取了第一个poi的名称存储.
    public String poiName;
    //省份
    public String province;
    @Generated(hash = 454649545)
    public LocationBean(Long id, String time, int locType,
            String locTypeDescription, double latitude, double lontitude,
            float radius, String countryCode, String country, String citycode,
            String city, String district, String street, String addr,
            int userIndoorState, float direction, String locationdescribe,
            String poiName, String province) {
        this.id = id;
        this.time = time;
        this.locType = locType;
        this.locTypeDescription = locTypeDescription;
        this.latitude = latitude;
        this.lontitude = lontitude;
        this.radius = radius;
        this.countryCode = countryCode;
        this.country = country;
        this.citycode = citycode;
        this.city = city;
        this.district = district;
        this.street = street;
        this.addr = addr;
        this.userIndoorState = userIndoorState;
        this.direction = direction;
        this.locationdescribe = locationdescribe;
        this.poiName = poiName;
        this.province = province;
    }
    @Generated(hash = 516751439)
    public LocationBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public int getLocType() {
        return this.locType;
    }
    public void setLocType(int locType) {
        this.locType = locType;
    }
    public String getLocTypeDescription() {
        return this.locTypeDescription;
    }
    public void setLocTypeDescription(String locTypeDescription) {
        this.locTypeDescription = locTypeDescription;
    }
    public double getLatitude() {
        return this.latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLontitude() {
        return this.lontitude;
    }
    public void setLontitude(double lontitude) {
        this.lontitude = lontitude;
    }
    public float getRadius() {
        return this.radius;
    }
    public void setRadius(float radius) {
        this.radius = radius;
    }
    public String getCountryCode() {
        return this.countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    public String getCountry() {
        return this.country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getCitycode() {
        return this.citycode;
    }
    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }
    public String getCity() {
        return this.city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getDistrict() {
        return this.district;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public String getStreet() {
        return this.street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public String getAddr() {
        return this.addr;
    }
    public void setAddr(String addr) {
        this.addr = addr;
    }
    public int getUserIndoorState() {
        return this.userIndoorState;
    }
    public void setUserIndoorState(int userIndoorState) {
        this.userIndoorState = userIndoorState;
    }
    public float getDirection() {
        return this.direction;
    }
    public void setDirection(float direction) {
        this.direction = direction;
    }
    public String getLocationdescribe() {
        return this.locationdescribe;
    }
    public void setLocationdescribe(String locationdescribe) {
        this.locationdescribe = locationdescribe;
    }
    public String getPoiName() {
        return this.poiName;
    }
    public void setPoiName(String poiName) {
        this.poiName = poiName;
    }
    public String getProvince() {
        return this.province;
    }
    public void setProvince(String province) {
        this.province = province;
    }

   
}
