package com.zmy.diamond.utli.bean;

/**
 * Created by zhangmengyun on 2018/8/6.
 */

public class MyTradingModifyBean {


    /**
     * msg : 成功
     * code : 200
     * data : {"weixin":"kk","user_id":245,"city":"jjj","price":22,"friend_number":22,"industry":"ccaaaamd","id":83,"trade_describe":null,"type":0}
     */

    private String msg;
    private int code;
    /**
     * weixin : kk
     * user_id : 245
     * city : jjj
     * price : 22
     * friend_number : 22
     * industry : ccaaaamd
     * id : 83
     * trade_describe : null
     * type : 0
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
        private String weixin;
        private int user_id;
        private String city;
        private int price;
        private int friend_number;
        private String industry;
        private int id;
        private Object trade_describe;
        private int type;

        public String getWeixin() {
            return weixin;
        }

        public void setWeixin(String weixin) {
            this.weixin = weixin;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getFriend_number() {
            return friend_number;
        }

        public void setFriend_number(int friend_number) {
            this.friend_number = friend_number;
        }

        public String getIndustry() {
            return industry;
        }

        public void setIndustry(String industry) {
            this.industry = industry;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getTrade_describe() {
            return trade_describe;
        }

        public void setTrade_describe(Object trade_describe) {
            this.trade_describe = trade_describe;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
