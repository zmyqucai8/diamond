package com.zmy.diamond.utli.bean;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * Created by zhangmengyun on 2018/6/11.
 */
@Entity
public class InfoBean {
    //信息页面数据, 存储是为了可以离线查看
    @Id
    public Long id;
    @NotNull
    @Index(unique = true)
    public String userId;  //id唯一,是为了切换用户,不同用户可能不同数据
    //info banner
    @ToMany(joinProperties = {@JoinProperty(name = "userId", referencedName = "userId")})
    public List<InfoBannerBean> infoBannerList;
    //info menu
    @ToMany(joinProperties = {@JoinProperty(name = "userId", referencedName = "userId")})
    public List<InfoMenuBean> infoMenuList;
    //info notice
    @ToMany(joinProperties = {@JoinProperty(name = "userId", referencedName = "userId")})
    public List<InfoNoticeBean> infoNoticeList;
    //info data
    @ToMany(joinProperties = {@JoinProperty(name = "userId", referencedName = "userId")})
    public List<InfoDataBean> infoDataList;
    //更新时间
    public long updateTime;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1319350303)
    private transient InfoBeanDao myDao;
    @Generated(hash = 55292272)
    public InfoBean(Long id, @NotNull String userId, long updateTime) {
        this.id = id;
        this.userId = userId;
        this.updateTime = updateTime;
    }
    @Generated(hash = 134777477)
    public InfoBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public long getUpdateTime() {
        return this.updateTime;
    }
    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 396203287)
    public List<InfoBannerBean> getInfoBannerList() {
        if (infoBannerList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            InfoBannerBeanDao targetDao = daoSession.getInfoBannerBeanDao();
            List<InfoBannerBean> infoBannerListNew = targetDao._queryInfoBean_InfoBannerList(userId);
            synchronized (this) {
                if (infoBannerList == null) {
                    infoBannerList = infoBannerListNew;
                }
            }
        }
        return infoBannerList;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 300704502)
    public synchronized void resetInfoBannerList() {
        infoBannerList = null;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 387762206)
    public List<InfoMenuBean> getInfoMenuList() {
        if (infoMenuList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            InfoMenuBeanDao targetDao = daoSession.getInfoMenuBeanDao();
            List<InfoMenuBean> infoMenuListNew = targetDao._queryInfoBean_InfoMenuList(userId);
            synchronized (this) {
                if (infoMenuList == null) {
                    infoMenuList = infoMenuListNew;
                }
            }
        }
        return infoMenuList;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1402771162)
    public synchronized void resetInfoMenuList() {
        infoMenuList = null;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 2115789508)
    public List<InfoNoticeBean> getInfoNoticeList() {
        if (infoNoticeList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            InfoNoticeBeanDao targetDao = daoSession.getInfoNoticeBeanDao();
            List<InfoNoticeBean> infoNoticeListNew = targetDao._queryInfoBean_InfoNoticeList(userId);
            synchronized (this) {
                if (infoNoticeList == null) {
                    infoNoticeList = infoNoticeListNew;
                }
            }
        }
        return infoNoticeList;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1609774793)
    public synchronized void resetInfoNoticeList() {
        infoNoticeList = null;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1722838190)
    public List<InfoDataBean> getInfoDataList() {
        if (infoDataList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            InfoDataBeanDao targetDao = daoSession.getInfoDataBeanDao();
            List<InfoDataBean> infoDataListNew = targetDao._queryInfoBean_InfoDataList(userId);
            synchronized (this) {
                if (infoDataList == null) {
                    infoDataList = infoDataListNew;
                }
            }
        }
        return infoDataList;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1894186145)
    public synchronized void resetInfoDataList() {
        infoDataList = null;
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 312438056)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getInfoBeanDao() : null;
    }


    //1. List <banner >
    //2. 菜单 = ReclecyView gridview  长度必须%5=0 ,  控制菜单图标,内容,那菜单类型呢? 1=打开web页面,2=打开数据列表,传入id再获取列表数据.3=打开本地activity. value=calssName
    //3. 公告 = List<String> 最新公告, 滚动数据 ,点击进入webview
    //4. list 数据 , 点击进入webview


//    /**
//     * 信息页Banner bean
//     */
//    @Entity
//    public class InfoBannerBean {
//        //bannerId 唯一
//        @Index(unique = true)
//        public String bannerId;
//        //图片url
//        public String imageUrl;
//        //点击操作类型
//        //1=web窗口打开detailsUrl
//        //2=data窗口打开,通过dataId,请求数据接口,获取数据显示.
//        public String actionType;
//        //当actionType=2时,必须有值
//        public String dataId;
//        //当actionType=1时,必须有值
//        public String detailsUrl;
//        //标题
//        public String title;
//        //内容
//        public String content;
//        //所属用户id
//        public String userId;
//    }

//    /**
//     * 信息页菜单bean
//     */
//    @Entity
//    public class InfoMenuBean {
//        //菜单id 唯一
//        @Index(unique = true)
//        public String menuId;
//        //图标url
//        public String imageUrl;
//        //点击操作类型
//        //1=web窗口打开detailsUrl
//        //2=data窗口打开,通过dataId,请求数据接口,获取数据显示.
//        public String actionType;
//        //当actionType=2时,必须有值
//        public String dataId;
//        //当actionType=1时,必须有值
//        public String detailsUrl;
//        //菜单标题名称
//        public String title;
//        //菜单所属用户
//        public String userId;
//    }

//    /**
//     * 信息页公告数据
//     */
//    @Entity
//    public class InfoNoticeBean {
//
//        //公告 id 唯一
//        @Index(unique = true)
//        public String noticeId;
//        //公告标题
//        public String title;
//        //公告内容,备用字段,暂时不需要设值
//        public String content;
//        //点击操作类型
//        //1=web窗口打开detailsUrl
//        //2=data窗口打开,通过dataId,请求数据接口,获取数据显示.
//        public String actionType;
//        //当actionType=2时,必须有值
//        public String dataId;
//        //当actionType=1时,必须有值
//        public String detailsUrl;
//        //公告所属用户
//        public String userId;
//
//
//    }

//    /**
//     * 信息页数据 bean
//     */
//    @Entity
//    public class InfoDataBean {
//        //数据 id 唯一
//        @Index(unique = true)
//        public String infoDataId;
//        //数据标题
//        public String title;
//        //数据内容
//        public String content;
//        //点击操作类型
//        //1=web窗口打开detailsUrl
//        //2=data窗口打开,通过dataId,请求数据接口,获取数据显示.
//        public String actionType;
//        //当actionType=2时,必须有值
//        public String dataId;
//        //当actionType=1时,必须有值
//        public String detailsUrl;
//        //图标url
//        public String imageUrl;
//        //数据所属用户
//        public String userId;
//    }
}
