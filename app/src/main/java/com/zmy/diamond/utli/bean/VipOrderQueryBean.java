package com.zmy.diamond.utli.bean;

/**
 * Created by zhangmengyun on 2018/8/7.
 */

public class VipOrderQueryBean {


    @Override
    public String toString() {
        return "VipOrderQueryBean{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }

    /**
     * msg : 成功
     * code : 200
     * data : {"callback_time":null,"create_time":"1533610445914","user_id":245,"signature":"35b896d627dfb179742ccdf5e9de32ed","price":"0.05","call_back":false,"recom_code":null,"type":"2","order_id":"10349570030","order_info":"245"}
     */

    private String msg;
    private int code;
    /**
     * callback_time : null
     * create_time : 1533610445914
     * user_id : 245
     * signature : 35b896d627dfb179742ccdf5e9de32ed
     * price : 0.05
     * call_back : false
     * recom_code : null
     * type : 2
     * order_id : 10349570030
     * order_info : 245
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
        private Object callback_time;
        private String create_time;
        private String user_id;
        private String signature;

        @Override
        public String toString() {
            return "DataBean{" +
                    "callback_time=" + callback_time +
                    ", create_time='" + create_time + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", signature='" + signature + '\'' +
                    ", price='" + price + '\'' +
                    ", call_back=" + call_back +
                    ", recom_code='" + recom_code + '\'' +
                    ", type='" + type + '\'' +
                    ", order_id='" + order_id + '\'' +
                    ", order_info='" + order_info + '\'' +
                    '}';
        }

        private String price;
        private boolean call_back;
        private String recom_code;
        private String type;
        private String order_id;
        private String order_info;

        public Object getCallback_time() {
            return callback_time;
        }

        public void setCallback_time(Object callback_time) {
            this.callback_time = callback_time;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public boolean isCall_back() {
            return call_back;
        }

        public void setCall_back(boolean call_back) {
            this.call_back = call_back;
        }

        public String getRecom_code() {
            return recom_code;
        }

        public void setRecom_code(String recom_code) {
            this.recom_code = recom_code;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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
    }
}
