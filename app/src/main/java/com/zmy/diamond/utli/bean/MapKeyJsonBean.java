package com.zmy.diamond.utli.bean;

import java.util.List;

/**
 * Created by zhangmengyun on 2018/9/6.
 */

public class MapKeyJsonBean {
    @Override
    public String toString() {
        return "MapKeyJsonBean{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + ((null != data) ? data.toString() : "null") +
                '}';
    }

    /**
     * msg : 成功
     * code : 200
     * data : [{"map_type":0,"map_key":"48136dbc6078cd972407e131020bbfe2","status_excess":false,"status_concurr":false,"id":70},{"map_type":0,"map_key":"48136dbc6078cd972407e131020bbfe2","status_excess":false,"status_concurr":false,"id":69}]
     */

    private String msg;
    private int code;
    /**
     * map_type : 0
     * map_key : 48136dbc6078cd972407e131020bbfe2
     * status_excess : false
     * status_concurr : false
     * id : 70
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
                    "map_type=" + map_type +
                    ", map_key='" + map_key + '\'' +
                    ", status_excess=" + status_excess +
                    ", status_concurr=" + status_concurr +
                    ", id=" + id +
                    '}';
        }

        private int map_type;
        private String map_key;
        private boolean status_excess;
        private boolean status_concurr;
        private int id;

        public int getMap_type() {
            return map_type;
        }

        public void setMap_type(int map_type) {
            this.map_type = map_type;
        }

        public String getMap_key() {
            return map_key;
        }

        public void setMap_key(String map_key) {
            this.map_key = map_key;
        }

        public boolean isStatus_excess() {
            return status_excess;
        }

        public void setStatus_excess(boolean status_excess) {
            this.status_excess = status_excess;
        }

        public boolean isStatus_concurr() {
            return status_concurr;
        }

        public void setStatus_concurr(boolean status_concurr) {
            this.status_concurr = status_concurr;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
