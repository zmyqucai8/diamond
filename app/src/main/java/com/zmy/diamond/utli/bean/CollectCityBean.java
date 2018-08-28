package com.zmy.diamond.utli.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * 采集城市 表
 * Created by zhangmengyun on 2018/8/28.
 */
@Entity
public class CollectCityBean {
    @Id
    private Long id;

    @Index(unique = true)
    private String collectCity; // city+key 组成的唯一码
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
    //当前分块的位置索引，表示当前城市，已经采集到哪个点了。下次从这里继续， 如果超出了，归零。
    private int currentIndex;

    //当前城市分块的数量
    private int blockCount;

    @Generated(hash = 271247132)
    public CollectCityBean(Long id, String collectCity, @NotNull String key,
            @NotNull String city, int platformId, String userId, int currentIndex,
            int blockCount) {
        this.id = id;
        this.collectCity = collectCity;
        this.key = key;
        this.city = city;
        this.platformId = platformId;
        this.userId = userId;
        this.currentIndex = currentIndex;
        this.blockCount = blockCount;
    }

    @Generated(hash = 1857651585)
    public CollectCityBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCollectCity() {
        return this.collectCity;
    }

    public void setCollectCity(String collectCity) {
        this.collectCity = collectCity;
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

    public int getCurrentIndex() {
        return this.currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getBlockCount() {
        return this.blockCount;
    }

    public void setBlockCount(int blockCount) {
        this.blockCount = blockCount;
    }


}
