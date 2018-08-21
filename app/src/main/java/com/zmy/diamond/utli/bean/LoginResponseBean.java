package com.zmy.diamond.utli.bean;

/**
 * Created by zhangmengyun on 2018/7/28.
 */

public class LoginResponseBean {


    /**
     * msg : 成功
     * code : 200
     * data : {"code":null,"vip_time":null,"sigin_time":"1534766856930","rigister_time":null,"userName":null,"down_number_time":null,"saveNumber":10,"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHQiOjE1MzQ5NDA2MTA2MDIsInVpZCI6MjQ1LCJpYXQiOjE1MzQ4NTQyMTA2MDJ9.yGXwZwjZji6f2bVxpM_Wp0nS_jjHpTv-d2R6g0l_dxE","macAddress":"d8:9a:34:0e:ba:f2","money":10000,"integral":20,"grade":0,"downNumber":0,"recomCode":null,"id":245}
     */

    private String msg;
    private int code;
    /**
     * code : null
     * vip_time : null
     * sigin_time : 1534766856930
     * rigister_time : null
     * userName : null
     * down_number_time : null
     * saveNumber : 10
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHQiOjE1MzQ5NDA2MTA2MDIsInVpZCI6MjQ1LCJpYXQiOjE1MzQ4NTQyMTA2MDJ9.yGXwZwjZji6f2bVxpM_Wp0nS_jjHpTv-d2R6g0l_dxE
     * macAddress : d8:9a:34:0e:ba:f2
     * money : 10000.0
     * integral : 20
     * grade : 0
     * downNumber : 0
     * recomCode : null
     * id : 245
     */

    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String code;
        private long vip_time;
        private long sigin_time;
        private long rigister_time;
        private String userName;
        private long down_number_time;
        private int saveNumber;
        private String token;
        private String macAddress;
        private double money;
        private int integral;
        private int grade;
        private int downNumber;
        private String recomCode;
        private String id;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public long getVip_time() {
            return vip_time;
        }

        public void setVip_time(long vip_time) {
            this.vip_time = vip_time;
        }

        public long getSigin_time() {
            return sigin_time;
        }

        public void setSigin_time(long sigin_time) {
            this.sigin_time = sigin_time;
        }

        public long getRigister_time() {
            return rigister_time;
        }

        public void setRigister_time(long rigister_time) {
            this.rigister_time = rigister_time;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public long getDown_number_time() {
            return down_number_time;
        }

        public void setDown_number_time(long down_number_time) {
            this.down_number_time = down_number_time;
        }

        public int getSaveNumber() {
            return saveNumber;
        }

        public void setSaveNumber(int saveNumber) {
            this.saveNumber = saveNumber;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getMacAddress() {
            return macAddress;
        }

        public void setMacAddress(String macAddress) {
            this.macAddress = macAddress;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public int getIntegral() {
            return integral;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public int getDownNumber() {
            return downNumber;
        }

        public void setDownNumber(int downNumber) {
            this.downNumber = downNumber;
        }

        public String getRecomCode() {
            return recomCode;
        }

        public void setRecomCode(String recomCode) {
            this.recomCode = recomCode;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
