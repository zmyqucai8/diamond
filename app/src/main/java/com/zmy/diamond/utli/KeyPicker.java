package com.zmy.diamond.utli;

import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zaaach.citypicker.adapter.OnPickListener;
import com.zmy.diamond.fragment.KeyPickerDialogFragment;

/**
 * @Author: Bro0cL
 * @Date: 2018/2/6 17:52
 */
public class KeyPicker {
    private static final String TAG = "CityPicker";

    private static KeyPicker mInstance;

    private KeyPicker(){}

    public static KeyPicker getInstance(){
        if (mInstance == null){
            synchronized (KeyPicker.class){
                if (mInstance == null){
                    mInstance = new KeyPicker();
                }
            }
        }
        return mInstance;
    }

    private FragmentManager mFragmentManager;
    private Fragment mTargetFragment;

    private boolean enableAnim;
    private int mAnimStyle;

    private OnPickListener mOnPickListener;

    public KeyPicker setFragmentManager(FragmentManager fm) {
        this.mFragmentManager = fm;
        return this;
    }

    public KeyPicker setTargetFragment(Fragment targetFragment) {
        this.mTargetFragment = targetFragment;
        return this;
    }

    /**
     * 设置动画效果
     * @param animStyle
     * @return
     */
    public KeyPicker setAnimationStyle(@StyleRes int animStyle) {
        this.mAnimStyle = animStyle;
        return this;
    }

//    /**
//     * 设置当前已经定位的城市
//     * @param location
//     * @return
//     */
//    public CityPicker setLocatedCity(LocatedCity location) {
//        this.mLocation = location;
//        return this;
//    }

//    public CityPicker setHotCities(List<HotCity> data){
//        this.mHotCities = data;
//        return this;
//    }

    /**
     * 启用动画效果，默认为false
     * @param enable
     * @return
     */
    public KeyPicker enableAnimation(boolean enable){
        this.enableAnim = enable;
        return this;
    }

    /**
     * 设置选择结果的监听器
     * @param listener
     * @return
     */
    public KeyPicker setOnPickListener(OnPickListener listener){
        this.mOnPickListener = listener;
        return this;
    }

    public void show(){
        if (mFragmentManager == null){
            throw new UnsupportedOperationException("CityPicker：method setFragmentManager() must be called.");
        }
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        final Fragment prev = mFragmentManager.findFragmentByTag(TAG);
        if (prev != null){
            ft.remove(prev).commit();
            ft = mFragmentManager.beginTransaction();
        }
        ft.addToBackStack(null);
        final KeyPickerDialogFragment keyPickerDialogFragment =
                KeyPickerDialogFragment.newInstance(enableAnim);
//        keyPickerDialogFragment.setLocatedCity(mLocation);
//        cityPickerFragment.setHotCities(mHotCities);
        keyPickerDialogFragment.setAnimationStyle(mAnimStyle);
        keyPickerDialogFragment.setOnPickListener(mOnPickListener);
        if (mTargetFragment != null){
            keyPickerDialogFragment.setTargetFragment(mTargetFragment, 0);
        }
        keyPickerDialogFragment.show(ft, TAG);
    }

//    /**
//     * 定位完成
//     * @param location
//     * @param state
//     */
//    public void locateComplete(LocatedCity location, @LocateState.State int state){
//        CityPickerDialogFragment fragment = (CityPickerDialogFragment) mFragmentManager.findFragmentByTag(TAG);
//        if (fragment != null){
//            fragment.locationChanged(location, state);
//        }
//    }
}
