package com.zmy.diamond.utli.bean;

/**
 * Created by zhangmengyun on 2018/6/29.
 */

public class AppVersionBean {


    /**
     * code : 105
     * error : invalid field name: bl!ng
     */

    private int code;
    private String error;
    /**
     * app_size : 16MB
     * createdAt : 2018-06-29 16:07:29
     * objectId : bC5RAAAU
     * update_content : 1.集成bmob后端云
     * updatedAt : 2018-06-29 16:08:21
     * version_code : 2
     * version_name : 1.1
     * version_time : 2018年06月29日
     */

    private String app_size;

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    private String downloadUrl;
    private String createdAt;
    private String objectId;
    private String update_content;
    private String updatedAt;
    private int version_code;
    private String version_name;
    private String version_time;

    public String getApp_size() {
        return app_size;
    }

    public void setApp_size(String app_size) {
        this.app_size = app_size;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUpdate_content() {
        return update_content;
    }

    public void setUpdate_content(String update_content) {
        this.update_content = update_content;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getVersion_code() {
        return version_code;
    }

    public void setVersion_code(int version_code) {
        this.version_code = version_code;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public String getVersion_time() {
        return version_time;
    }

    public void setVersion_time(String version_time) {
        this.version_time = version_time;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
