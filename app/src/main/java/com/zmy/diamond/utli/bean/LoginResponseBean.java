package com.zmy.diamond.utli.bean;

/**
 * Created by zhangmengyun on 2018/7/28.
 */

public class LoginResponseBean {

    @Override
    public String toString() {
        return "LoginResponseBean{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + (null == data ? "null" : data.toString()) +
                '}';
    }

    /**
     * msg : 成功
     * code : 200
     * data : {"macAddress":"52:54:00:12:34:56","code":null,"money":0,"integral":0,"grade":0,"downNumber":0,"recomCode":null,"id":235,"userName":null,"saveNumber":0,"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHQiOjE1MzI4NDQ5OTA5MDYsInVpZCI6MjM1LCJpYXQiOjE1MzI3NTg1OTA5MDZ9.AbvekBBUvkprbnemEZd8-JM56N1jmoDtOO06UVNcQPU"}
     */

    private String msg;
    private int code;
    /**
     * macAddress : 52:54:00:12:34:56
     * code : null
     * money : 0.0
     * integral : 0
     * grade : 0
     * downNumber : 0
     * recomCode : null
     * id : 235
     * userName : null
     * saveNumber : 0
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHQiOjE1MzI4NDQ5OTA5MDYsInVpZCI6MjM1LCJpYXQiOjE1MzI3NTg1OTA5MDZ9.AbvekBBUvkprbnemEZd8-JM56N1jmoDtOO06UVNcQPU
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
        private String macAddress;
        private String code;
        private double money;
        private int integral;
        private int grade;
        private int downNumber;

        @Override
        public String toString() {
            return "DataBean{" +
                    "macAddress='" + macAddress + '\'' +
                    ", code='" + code + '\'' +
                    ", money=" + money +
                    ", integral=" + integral +
                    ", grade=" + grade +
                    ", downNumber=" + downNumber +
                    ", recomCode='" + recomCode + '\'' +
                    ", id=" + id +
                    ", userName='" + userName + '\'' +
                    ", saveNumber=" + saveNumber +
                    ", token='" + token + '\'' +
                    '}';
        }

        private String recomCode;
        private String id;
        private String userName;
        private int saveNumber;
        private String token;

        public String getMacAddress() {
            return macAddress;
        }

        public void setMacAddress(String macAddress) {
            this.macAddress = macAddress;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
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

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
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
    }
}
