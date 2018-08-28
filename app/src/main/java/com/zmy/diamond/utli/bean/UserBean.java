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
 * Created by zhangmengyun on 2018/6/14.
 * 用户表
 */
@Entity
public class UserBean {


    @Id
    private Long id;
    @NotNull
    @Index(unique = true)
    private String userId;

    @Index(unique = true)
    private String phone;
    @Index(unique = true)
    private String macAddress;
    private String code;//自己的推荐码
    private double money;//零钱
    private int grade; //会员等级  1=黄金会员2=白金会员
    private String recomCode;//推荐人推荐码
    private String token;
    private String openId;
    private String nickName;
    private String avatarUrl;
    private String city;//城市
    private String province;//省份
    private String longitudeAndLatitude;//经纬度 ,分割

    //    private String vipTime; //成为vip的时间
    //    private int vipDay;//VIP剩余天数
//    private int vipDayTotal;//VIP总天数
//    private int vipType;//VIP类型
//    private String vipTypeDes;//VIP类型描述
    private int integral;//积分
    //    private int signDay;//签到总天数
    private int downNumber;//今日累计

    private int saveNumber;//保存到通讯录数据的累计总数 （废弃）

    private long lastSignTime;//最后一次签到时间

    public long getVip_time() {
        return vip_time;
    }

    public void setVip_time(long vip_time) {
        this.vip_time = vip_time;
    }

    public String getVip_valid_time() {
        return vip_valid_time;
    }

    public void setVip_valid_time(String vip_valid_time) {
        this.vip_valid_time = vip_valid_time;
    }

    public long getRigister_time() {
        return rigister_time;
    }

    public void setRigister_time(long rigister_time) {
        this.rigister_time = rigister_time;
    }

    public long getDown_number_time() {
        return down_number_time;
    }

    public void setDown_number_time(long down_number_time) {
        this.down_number_time = down_number_time;
    }

    private long vip_time;//成为vip的时间
    private String vip_valid_time;//vip有效时间 2018-08-21
    private long rigister_time;//注册时间
    private long down_number_time;//最后一次更新下今日累计的时间


    //    private String recommendedPhone;//推荐人手机号
    @ToMany(joinProperties = {
            @JoinProperty(name = "userId", referencedName = "userId")
    })
    private List<DataBean> dataBeanList;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 83707551)
    private transient UserBeanDao myDao;

    @Generated(hash = 1521059849)
    public UserBean(Long id, @NotNull String userId, String phone, String macAddress,
                    String code, double money, int grade, String recomCode, String token,
                    String openId, String nickName, String avatarUrl, String city, String province,
                    String longitudeAndLatitude, int integral, int downNumber, int saveNumber,
                    long lastSignTime, long vip_time, String vip_valid_time, long rigister_time,
                    long down_number_time) {
        this.id = id;
        this.userId = userId;
        this.phone = phone;
        this.macAddress = macAddress;
        this.code = code;
        this.money = money;
        this.grade = grade;
        this.recomCode = recomCode;
        this.token = token;
        this.openId = openId;
        this.nickName = nickName;
        this.avatarUrl = avatarUrl;
        this.city = city;
        this.province = province;
        this.longitudeAndLatitude = longitudeAndLatitude;
        this.integral = integral;
        this.downNumber = downNumber;
        this.saveNumber = saveNumber;
        this.lastSignTime = lastSignTime;
        this.vip_time = vip_time;
        this.vip_valid_time = vip_valid_time;
        this.rigister_time = rigister_time;
        this.down_number_time = down_number_time;
    }

    @Generated(hash = 1203313951)
    public UserBean() {
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

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getMoney() {
        return this.money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getGrade() {
        return this.grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getRecomCode() {
        return this.recomCode;
    }

    public void setRecomCode(String recomCode) {
        this.recomCode = recomCode;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOpenId() {
        return this.openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getLongitudeAndLatitude() {
        return this.longitudeAndLatitude;
    }

    public void setLongitudeAndLatitude(String longitudeAndLatitude) {
        this.longitudeAndLatitude = longitudeAndLatitude;
    }

    public int getIntegral() {
        return this.integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public int getDownNumber() {
        return this.downNumber;
    }

    public void setDownNumber(int downNumber) {
        this.downNumber = downNumber;
    }

    public int getSaveNumber() {
        return this.saveNumber;
    }

    public void setSaveNumber(int saveNumber) {
        this.saveNumber = saveNumber;
    }

    public long getLastSignTime() {
        return this.lastSignTime;
    }

    public void setLastSignTime(long lastSignTime) {
        this.lastSignTime = lastSignTime;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 383190596)
    public List<DataBean> getDataBeanList() {
        if (dataBeanList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DataBeanDao targetDao = daoSession.getDataBeanDao();
            List<DataBean> dataBeanListNew = targetDao
                    ._queryUserBean_DataBeanList(userId);
            synchronized (this) {
                if (dataBeanList == null) {
                    dataBeanList = dataBeanListNew;
                }
            }
        }
        return dataBeanList;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 160725530)
    public synchronized void resetDataBeanList() {
        dataBeanList = null;
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
    @Generated(hash = 1491512534)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserBeanDao() : null;
    }


}
