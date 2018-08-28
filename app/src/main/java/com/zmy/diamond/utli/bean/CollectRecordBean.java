package com.zmy.diamond.utli.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by zhangmengyun on 2018/6/12.
 * 采集记录bean , 用于营销页面的列表数据
 * //每添加一次数据,我就可以认为是抓潜一次, 只要key 和city是一样那么数据最终是1条, 如果数据不一样,则是2条
 * //这里仅仅只是产生一个营销列表数据的头部, 当真正点击进入某个数据时, 再去data表搜索相关数据.
 */

@Entity
public class CollectRecordBean {


    @Id
    private Long id;


    @Index(unique = true)
    private String collectId; // key+city+platformId

    //此次采集的关键词
    @NotNull
    private String key;
    //此次采集的城市
    @NotNull
    public String city;
    //数据所属平台id
    @NotNull
    public int platformId;
    //所属用户id
    private String userId;
    //来源
    public String source;
    //最后一次更新时间
    public long updateTime;



    //数据数量
    public int count;



    @Generated(hash = 926693603)
    public CollectRecordBean(Long id, String collectId, @NotNull String key,
            @NotNull String city, int platformId, String userId, String source,
            long updateTime, int count) {
        this.id = id;
        this.collectId = collectId;
        this.key = key;
        this.city = city;
        this.platformId = platformId;
        this.userId = userId;
        this.source = source;
        this.updateTime = updateTime;
        this.count = count;
    }



    @Generated(hash = 2046816929)
    public CollectRecordBean() {
    }



    public Long getId() {
        return this.id;
    }



    public void setId(Long id) {
        this.id = id;
    }



    public String getCollectId() {
        return this.collectId;
    }



    public void setCollectId(String collectId) {
        this.collectId = collectId;
    }



    public String getKey() {
        return this.key;
    }



    public void setKey(String key) {
        this.key = key;
    }



    public String getCity() {
        return this.city;
    }



    public void setCity(String city) {
        this.city = city;
    }



    public int getPlatformId() {
        return this.platformId;
    }



    public void setPlatformId(int platformId) {
        this.platformId = platformId;
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



    public long getUpdateTime() {
        return this.updateTime;
    }



    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }



    public int getCount() {
        return this.count;
    }



    public void setCount(int count) {
        this.count = count;
    }





}
