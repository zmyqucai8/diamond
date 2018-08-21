package com.zmy.diamond.utli.bean;

/**
 * Created by zhangmengyun on 2018/8/21.
 */

public class PayBean {

    /**
     * qrcode : http://xxx/xxx.png
     * pay_url : wxp://...
     * pay_type : 1
     * real_price : 6.5
     * ttl : 295
     * redirect : http://xxx
     */

    private InfoBean info;
    /**
     * info : {"qrcode":"http://xxx/xxx.png","pay_url":"wxp://...","pay_type":1,"real_price":6.5,"ttl":295,"redirect":"http://xxx"}
     * message : 继续支付之前的订单
     * code : 200
     * result : true
     */

    private String message;
    private int code;
    private boolean result;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public static class InfoBean {
        private String qrcode;
        private String pay_url;
        private int pay_type;
        private double real_price;
        private int ttl;
        private String redirect;

        public String getQrcode() {
            return qrcode;
        }

        public void setQrcode(String qrcode) {
            this.qrcode = qrcode;
        }

        public String getPay_url() {
            return pay_url;
        }

        public void setPay_url(String pay_url) {
            this.pay_url = pay_url;
        }

        public int getPay_type() {
            return pay_type;
        }

        public void setPay_type(int pay_type) {
            this.pay_type = pay_type;
        }

        public double getReal_price() {
            return real_price;
        }

        public void setReal_price(double real_price) {
            this.real_price = real_price;
        }

        public int getTtl() {
            return ttl;
        }

        public void setTtl(int ttl) {
            this.ttl = ttl;
        }

        public String getRedirect() {
            return redirect;
        }

        public void setRedirect(String redirect) {
            this.redirect = redirect;
        }
    }
}
