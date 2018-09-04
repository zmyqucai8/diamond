package com.zmy.diamond.utli.bean;

import java.util.List;

/**
 * Created by zhangmengyun on 2018/6/29.
 */

public class AppVersionBean {


    /**
     * msg : 成功
     * code : 200
     * data : [{"need_update":false,"down_url":"https://www.pgyer.com/gyyd","id":1,"new_version":24,"message":"https://www.pgyer.com/guys"},{"need_update":false,"down_url":"www.yscoco.com/ios","id":2,"new_version":44,"message":"哈哈哈"}]
     */

    private String msg;
    private int code;
    /**
     * need_update : false
     * down_url : https://www.pgyer.com/gyyd
     * id : 1
     * new_version : 24
     * message : https://www.pgyer.com/guys
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
        private boolean need_update;
        private String down_url;
        private int id;
        private int new_version;
        private String message;

        public boolean isNeed_update() {
            return need_update;
        }

        public void setNeed_update(boolean need_update) {
            this.need_update = need_update;
        }

        public String getDown_url() {
            return down_url;
        }

        public void setDown_url(String down_url) {
            this.down_url = down_url;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getNew_version() {
            return new_version;
        }

        public void setNew_version(int new_version) {
            this.new_version = new_version;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
