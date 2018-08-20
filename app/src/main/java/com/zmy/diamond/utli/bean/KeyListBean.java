package com.zmy.diamond.utli.bean;

import java.util.List;

/**
 * Created by zhangmengyun on 2018/6/12.
 */

public class KeyListBean  {

    public List<KeyItemBean> listData;

    public String title_name;
    public String title_ttf;
    public int title_color;



    public static class KeyItemBean {
        public String name;
        public String name_ttf;
        public int  name_ttf_color;
    }
}
