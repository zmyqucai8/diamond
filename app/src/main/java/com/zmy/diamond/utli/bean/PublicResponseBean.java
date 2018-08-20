package com.zmy.diamond.utli.bean;

/**
 * http 请求响应实体base类
 * Created by zhangmengyun on 2018/7/28.
 *
 *
 * getCode
 * register
 */

public class PublicResponseBean {


    @Override
    public String toString() {
        return "PublicResponseBean{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", data='" + data + '\'' +
                '}';
    }

    /**
     * msg : 成功
     * code : 200
     * data : ""
     */

    private String msg;
    private int code;
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
