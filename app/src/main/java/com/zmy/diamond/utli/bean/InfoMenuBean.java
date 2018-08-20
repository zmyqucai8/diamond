package com.zmy.diamond.utli.bean;

/**
 * Created by zhangmengyun on 2018/7/3.
 */

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

/**
 * 信息页菜单bean
 */
@Entity
public class InfoMenuBean {
    @Id
    public Long id;
    //菜单id 唯一
    @Index(unique = true)
    public String menuId;
    //图标url
    public String imageUrl;
    //点击操作类型
    //1=web窗口打开detailsUrl
    //2=data窗口打开,通过dataId,请求数据接口,获取数据显示.
    public int  actionType;
    //当actionType=2时,必须有值
    public String dataId;
    //当actionType=1时,必须有值
    public String detailsUrl;
    //菜单标题名称
    public String title;
    //菜单所属用户
    public String userId;
    @Generated(hash = 718475587)
    public InfoMenuBean(Long id, String menuId, String imageUrl, int actionType,
            String dataId, String detailsUrl, String title, String userId) {
        this.id = id;
        this.menuId = menuId;
        this.imageUrl = imageUrl;
        this.actionType = actionType;
        this.dataId = dataId;
        this.detailsUrl = detailsUrl;
        this.title = title;
        this.userId = userId;
    }
    @Generated(hash = 618171590)
    public InfoMenuBean() {
    }
    public String getMenuId() {
        return this.menuId;
    }
    public void setMenuId(String menuId) {
        this.menuId = menuId;
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
