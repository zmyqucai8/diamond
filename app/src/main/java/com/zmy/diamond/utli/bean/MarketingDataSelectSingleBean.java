package com.zmy.diamond.utli.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zmy.diamond.adapter.MarketingDataSelectAdapter;

/**
 * Created by zhangmengyun on 2018/8/25.
 */

public class MarketingDataSelectSingleBean  implements MultiItemEntity {


    public String name;

//    public String size;

    public String address;

    public String phone;


    public boolean isCheck;




    @Override
    public int getItemType() {
        return MarketingDataSelectAdapter.TYPE_LEVEL_SINGLE;
    }
}
