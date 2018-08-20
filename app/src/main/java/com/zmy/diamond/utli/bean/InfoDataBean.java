package com.zmy.diamond.utli.bean;

/**
 * Created by zhangmengyun on 2018/7/3.
 */

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
/**
 * 信息页数据 bean
 */
@Entity
public class InfoDataBean {
    @Id
    public Long id;
    //数据 id 唯一
    @Index(unique = true)
    public String infoDataId;
    //数据标题
    public String title;
    //数据内容
    public String content;
    //点击操作类型
    //1=web窗口打开detailsUrl
    //2=data窗口打开,通过dataId,请求数据接口,获取数据显示.
    public int  actionType;
    //当actionType=2时,必须有值
    public String dataId;
    //当actionType=1时,必须有值
    public String detailsUrl;
    //图标url
    public String imageUrl;
    //数据所属用户
    public String userId;
    @Generated(hash = 106536936)
    public InfoDataBean(Long id, String infoDataId, String title, String content,
            int actionType, String dataId, String detailsUrl, String imageUrl,
            String userId) {
        this.id = id;
        this.infoDataId = infoDataId;
        this.title = title;
        this.content = content;
        this.actionType = actionType;
        this.dataId = dataId;
        this.detailsUrl = detailsUrl;
        this.imageUrl = imageUrl;
        this.userId = userId;
    }
    @Generated(hash = 123708428)
    public InfoDataBean() {
    }
    public String getInfoDataId() {
        return this.infoDataId;
    }
    public void setInfoDataId(String infoDataId) {
        this.infoDataId = infoDataId;
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
    public String getImageUrl() {
        return this.imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
