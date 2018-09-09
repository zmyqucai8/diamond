package com.zmy.diamond.utli.bean;

import java.util.List;

/**
 * Created by zhangmengyun on 2018/9/9.
 */

public class VipPriceJsonBean {


    /**
     * msg : 成功
     * code : 200
     * data : [{"price":"1.00","grade":1,"message":"黄金Vip"},{"price":"10.00","grade":1,"message":"白金Vip"}]
     */

    private String msg;
    private int code;
    /**
     * price : 1.00
     * grade : 1
     * message : 黄金Vip
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

    @Override
    public String toString() {
        return "VipPriceJsonBean{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + (null == data ? "null" : data.toString()) +
                '}';
    }

    public static class DataBean {
        private String price;

        @Override
        public String toString() {
            return "DataBean{" +
                    "price='" + price + '\'' +
                    ", grade=" + grade +
                    ", message='" + message + '\'' +
                    '}';
        }

        private int grade;
        private String message;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
