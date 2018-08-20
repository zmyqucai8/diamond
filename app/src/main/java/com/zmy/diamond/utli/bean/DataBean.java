package com.zmy.diamond.utli.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

import java.io.Serializable;

/**
 * Created by zhangmengyun on 2018/6/12.
 * 首页菜单bean
 */

@Entity
public class DataBean implements Serializable {

    private static final long serialVersionUID = 913620790;
    @Id
    private Long id;
    @NotNull
    @Index(unique = true)
    private String dataId;//取值=   platformId+uId


    //所属用户id
    private String userId;
    //来源
    public String source;
    //名称
    public String name;
    //固话
    public String tel;
    //手机
    public String phone;
    //地址
    public String address;
    //地址详情
    public String address_details;
    //商家id,仅在当前平台下所属id.各平台可能取值返回的字段名称不一致,但都属于该平台下唯一ID
    public String uId;
    //数据所属平台id
    public int platformId;


    //该数据的关键词
    public String key;
    //省份
    public String province;
    //城市
    public String city;
    // 区
    public String district;
    // 街道
    public String street;


    @Generated(hash = 528936834)
    public DataBean(Long id, @NotNull String dataId, String userId, String source,
            String name, String tel, String phone, String address,
            String address_details, String uId, int platformId, String key,
            String province, String city, String district, String street) {
        this.id = id;
        this.dataId = dataId;
        this.userId = userId;
        this.source = source;
        this.name = name;
        this.tel = tel;
        this.phone = phone;
        this.address = address;
        this.address_details = address_details;
        this.uId = uId;
        this.platformId = platformId;
        this.key = key;
        this.province = province;
        this.city = city;
        this.district = district;
        this.street = street;
    }

    @Generated(hash = 908697775)
    public DataBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataId() {
        return this.dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return this.tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress_details() {
        return this.address_details;
    }

    public void setAddress_details(String address_details) {
        this.address_details = address_details;
    }

    public String getUId() {
        return this.uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public int getPlatformId() {
        return this.platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
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


}
