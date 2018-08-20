package com.zmy.diamond.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zmy.diamond.utli.AppConstant;

import java.util.ArrayList;

/**
 * Created by zhangmengyun on 2018/6/20.
 */

public class HomePagerAdapter extends FragmentPagerAdapter {


    ArrayList<Fragment> mFragments;

    public HomePagerAdapter(ArrayList<Fragment> fragments, FragmentManager fm) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return AppConstant.Platform.NAME[position];
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}