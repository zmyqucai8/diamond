package com.zmy.diamond.utli.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;


/**
 * Created by zhangmengyun on 2018/9/4.
 */
@Entity
public class MapKeyBean {
    @Override
    public String toString() {
        return "MapKeyBean{" +
                "id=" + id +
                ", mapKeyId=" + mapKeyId +
                ", userId='" + userId + '\'' +
                ", status_excess=" + status_excess +
                ", status_concurr=" + status_concurr +
                ", mapKey='" + mapKey + '\'' +
                ", mapType=" + mapType +
                '}';
    }

    @Id
    private Long id;
    @NotNull
    @Index(unique = true)
    private int mapKeyId;
    //所属用户id
    private String userId;
    //超额
    public boolean status_excess;
    //并发
    public boolean status_concurr;
    //key
    @NotNull
    public String mapKey;
    //0=百度1=高德
    public int mapType;
    @Generated(hash = 1385090266)
    public MapKeyBean(Long id, int mapKeyId, String userId, boolean status_excess,
            boolean status_concurr, @NotNull String mapKey, int mapType) {
        this.id = id;
        this.mapKeyId = mapKeyId;
        this.userId = userId;
        this.status_excess = status_excess;
        this.status_concurr = status_concurr;
        this.mapKey = mapKey;
        this.mapType = mapType;
    }
    @Generated(hash = 1403272677)
    public MapKeyBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getMapKeyId() {
        return this.mapKeyId;
    }
    public void setMapKeyId(int mapKeyId) {
        this.mapKeyId = mapKeyId;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public boolean getStatus_excess() {
        return this.status_excess;
    }
    public void setStatus_excess(boolean status_excess) {
        this.status_excess = status_excess;
    }
    public boolean getStatus_concurr() {
        return this.status_concurr;
    }
    public void setStatus_concurr(boolean status_concurr) {
        this.status_concurr = status_concurr;
    }
    public String getMapKey() {
        return this.mapKey;
    }
    public void setMapKey(String mapKey) {
        this.mapKey = mapKey;
    }
    public int getMapType() {
        return this.mapType;
    }
    public void setMapType(int mapType) {
        this.mapType = mapType;
    }



}
