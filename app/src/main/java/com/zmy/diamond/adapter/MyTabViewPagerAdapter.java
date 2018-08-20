package com.zmy.diamond.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by zhangmengyun on 2018/1/27.
 */

public class MyTabViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mList;
    private Context mContext;
    private String[] titles;

    public MyTabViewPagerAdapter(Context context, FragmentManager fm, List<Fragment> list) {
        super(fm);

        this.mList = list;
        this.mContext = context;
    }
    public MyTabViewPagerAdapter(Context context, FragmentManager fm, List<Fragment> list, String[] titles) {
        super(fm);

        this.mList = list;
        this.mContext = context;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return null != mList ? mList.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if(null!=titles&&titles.length>0){
           return titles[position];
        }else {
            return super.getPageTitle(position);
        }
    }
}
