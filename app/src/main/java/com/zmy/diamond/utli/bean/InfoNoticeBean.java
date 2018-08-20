package com.zmy.diamond.utli.bean;

/**
 * Created by zhangmengyun on 2018/7/3.
 */

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

/**
 * 信息页公告数据
 */
@Entity
public class InfoNoticeBean {
    @Id
    public Long id;
    //公告 id 唯一
    @Index(unique = true)
    public String noticeId;
    //公告标题
    public String title;
    //公告内容,备用字段,暂时不需要设值
    public String content;
    //点击操作类型
    //1=web窗口打开detailsUrl
    //2=data窗口打开,通过dataId,请求数据接口,获取数据显示.
    public int  actionType;
    //当actionType=2时,必须有值
    public String dataId;
    //当actionType=1时,必须有值
    public String detailsUrl;
    //公告所属用户
    public String userId;
    @Generated(hash = 264370684)
    public InfoNoticeBean(Long id, String noticeId, String title, String content,
            int actionType, String dataId, String detailsUrl, String userId) {
        this.id = id;
        this.noticeId = noticeId;
        this.title = title;
        this.content = content;
        this.actionType = actionType;
        this.dataId = dataId;
        this.detailsUrl = detailsUrl;
        this.userId = userId;
    }
    @Generated(hash = 1275673903)
    public InfoNoticeBean() {
    }
    public String getNoticeId() {
        return this.noticeId;
    }
    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
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
