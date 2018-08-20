package com.zmy.diamond.utli.bean;

import java.io.Serializable;

/**
 * Created by zhangmengyun on 2018/7/28.
 */

public class JoinVipResponseBean {


    /**
     * msg : 成功
     * code : 200
     * data : {"api_user":"821805ef","order_id":"10533101516","order_info":"235","price":"3998.00","redirect":"http://112.74.200.150/pays/notifyPay","signature":"a1d489fdd26325dcc46276e98b5f2517","type":"2"}
     */

    private String msg;
    private int code;
    /**
     * api_user : 821805ef
     * order_id : 10533101516
     * order_info : 235
     * price : 3998.00
     * redirect : http://112.74.200.150/pays/notifyPay
     * signature : a1d489fdd26325dcc46276e98b5f2517
     * type : 2
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

    public static class DataBean implements Serializable {
        private String api_user;
        private String order_id;
        private String order_info;
        private String price;
        private String redirect;

        @Override
        public String toString() {
            return "DataBean{" +
                    "api_user='" + api_user + '\'' +
                    ", order_id='" + order_id + '\'' +
                    ", order_info='" + order_info + '\'' +
                    ", price='" + price + '\'' +
                    ", redirect='" + redirect + '\'' +
                    ", signature='" + signature + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }

        private String signature;
        private String type;

        public String getApi_user() {
            return api_user;
        }

        public void setApi_user(String api_user) {
            this.api_user = api_user;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getOrder_info() {
            return order_info;
        }

        public void setOrder_info(String order_info) {
            this.order_info = order_info;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getRedirect() {
            return redirect;
        }

        public void setRedirect(String redirect) {
            this.redirect = redirect;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }




}
