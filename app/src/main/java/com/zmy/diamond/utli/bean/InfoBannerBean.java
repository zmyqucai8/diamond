package com.zmy.diamond.utli.bean;

/**
 * Created by zhangmengyun on 2018/7/3.
 */

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
/**
 * 信息页Banner bean
 */
@Entity
public class InfoBannerBean {
    @Id
    public Long id;
    //bannerId 唯一
    @Index(unique = true)
    public String bannerId;
    //图片url
    public String imageUrl;
    //点击操作类型
    //1=web窗口打开detailsUrl
    //2=data窗口打开,通过dataId,请求数据接口,获取数据显示.
    public int actionType;
    //当actionType=2时,必须有值
    public String dataId;
    //当actionType=1时,必须有值
    public String detailsUrl;
    //标题
    public String title;
    //内容
    public String content;
    //所属用户id
    public String userId;
    @Generated(hash = 206310565)
    public InfoBannerBean(Long id, String bannerId, String imageUrl, int actionType,
            String dataId, String detailsUrl, String title, String content,
            String userId) {
        this.id = id;
        this.bannerId = bannerId;
        this.imageUrl = imageUrl;
        this.actionType = actionType;
        this.dataId = dataId;
        this.detailsUrl = detailsUrl;
        this.title = title;
        this.content = content;
        this.userId = userId;
    }
    @Generated(hash = 2088944635)
    public InfoBannerBean() {
    }
    public String getBannerId() {
        return this.bannerId;
    }
    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }
    public String getImageUrl() {
        return this.imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public int getActionType() {
        return this.actionType;
    }
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }
    public String getDataId() {
        return this.dataId;
    }
    public void setDataId(String dataId) {
        this.dataId = dataId;
    }
    public String getDetailsUrl() {
        return this.detailsUrl;
    }
    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

}