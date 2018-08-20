package com.zmy.diamond.utli.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangmengyun on 2018/7/28.
 */

public class TradingDataBean {
    @Override
    public String toString() {
        return "TradingDataBean{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + ((null != data) ? data.toString() : "null") +
                '}';
    }
//    public int dataType;
//
//    public String industry;
//    public String city;
//    public int friendCount;
//    public String price;
//    public String describe;

    /**
     * msg : 成功
     * code : 200
     * data : [{"weixin":"","user_id":240,"city":"æ\u009d­å·\u009e","price":88,"friend_number":885,"industry":"ä¸ªv","id":70,"trade_describe":null,"type":1},{"weixin":"","user_id":240,"city":"å®\u0089é¡º","price":57,"friend_number":877,"industry":"hH","id":72,"trade_describe":null,"type":1},{"weixin":"","user_id":240,"city":"å®\u0089åº\u0086","price":84,"friend_number":45,"industry":"ç»\u0099Ga","id":73,"trade_describe":null,"type":1}]
     */

    private String msg;
    private int code;
    /**
     * weixin :
     * user_id : 240
     * city : æ­å·
     * price : 88
     * friend_number : 885
     * industry : ä¸ªv
     * id : 70
     * trade_describe : null
     * type : 1
     */

    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        @Override
        public String toString() {
            return "DataBean{" +
                    "weixin='" + weixin + '\'' +
                    ", user_id=" + user_id +
                    ", city='" + city + '\'' +
                    ", price=" + price +
                    ", friend_number=" + friend_number +
                    ", industry='" + industry + '\'' +
                    ", id=" + id +
                    ", trade_describe='" + trade_describe + '\'' +
                    ", type=" + type +
                    '}';
        }

        private String weixin;
        private String user_id;
        private String city;
        private int price;
        private int friend_number;
        private String industry;
        private String id;
        private String trade_describe;
        private int type;

        public String getWeixin() {
            return weixin;
        }

        public void setWeixin(String weixin) {
            this.weixin = weixin;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTrade_describe() {
            return trade_describe;
        }

        public void setTrade_describe(String trade_describe) {
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
