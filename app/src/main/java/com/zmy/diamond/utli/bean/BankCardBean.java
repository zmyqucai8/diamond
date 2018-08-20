package com.zmy.diamond.utli.bean;

import java.util.List;

/**
 * Created by zhangmengyun on 2018/8/7.
 */

public class BankCardBean {


    @Override
    public String toString() {
        return "BankCardBean{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + (null == data ? "null" : data) +
                '}';
    }

    /**
     * msg : 成功
     * code : 200
     * data : [{"user_id":245,"name":"张梦云","bank_name":"招商银行","phone_number":"137000000001","id":4,"card":"412121202010"}]
     */


    private String msg;
    private int code;
    /**
     * user_id : 245
     * name : 张梦云
     * bank_name : 招商银行
     * phone_number : 137000000001
     * id : 4
     * card : 412121202010
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

    public static class DataBean {
        @Override
        public String toString() {
            return "DataBean{" +
                    "user_id='" + user_id + '\'' +
                    ", name='" + name + '\'' +
                    ", bank_name='" + bank_name + '\'' +
                    ", phone_number='" + phone_number + '\'' +
                    ", id='" + id + '\'' +
                    ", card='" + card + '\'' +
                    '}';
        }

        private String user_id;
        private String name;
        private String bank_name;
        private String phone_number;
        private String id;
        private String card;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
        }
    }
}
