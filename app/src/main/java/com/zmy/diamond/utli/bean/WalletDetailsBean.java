package com.zmy.diamond.utli.bean;

import java.util.List;

/**
 * Created by zhangmengyun on 2018/8/6.
 */

public class WalletDetailsBean {

    @Override
    public String toString() {
        return "WalletDetailsBean{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }

    /**
     * msg : 成功
     * code : 200
     * data : [{"user_id":235,"id":4,"message":"您发起的1000元提现申请正在审核中","status":0},{"user_id":235,"id":5,"message":"您发起的1000元提现申请正在审核中","status":0}]
     */

    private String msg;
    private int code;
    /**
     * user_id : 235
     * id : 4
     * message : 您发起的1000元提现申请正在审核中
     * status : 0
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
        private int user_id;
        private int id;

        @Override
        public String toString() {
            return "DataBean{" +
                    "user_id=" + user_id +
                    ", id=" + id +
                    ", cash_amount=" + cash_amount +
                    ", time=" + time +
                    ", message='" + message + '\'' +
                    ", status=" + status +
                    '}';
        }

        public int getCash_amount() {
            return cash_amount;
        }

        public void setCash_amount(int cash_amount) {
            this.cash_amount = cash_amount;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        private int cash_amount;
        private long time;

        private String message;
        //        其中status=ture 表示支出（提现）
//false 表示收入（推荐费用）
        private boolean status;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean getStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }
}
