package com.zmy.diamond.utli.bean;

/**
 * Created by zhangmengyun on 2018/8/5.
 */

public class AboutAppBean {


    /**
     * msg : 成功
     * code : 200
     * data : {"aboutAppContent":"xxxxxxxxxx","appName":"精准抓潛","serviceProtocol":"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx","version":"v-1.0"}
     */

    private String msg;
    private int code;
    /**
     * aboutAppContent : xxxxxxxxxx
     * appName : 精准抓潛
     * serviceProtocol : xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
     * version : v-1.0
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
        private String aboutAppContent;
        private String appName;
        private String serviceProtocol;
        private String version;

        public String getAboutAppContent() {
            return aboutAppContent;
        }

        public void setAboutAppContent(String aboutAppContent) {
            this.aboutAppContent = aboutAppContent;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getServiceProtocol() {
            return serviceProtocol;
        }

        public void setServiceProtocol(String serviceProtocol) {
            this.serviceProtocol = serviceProtocol;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}
