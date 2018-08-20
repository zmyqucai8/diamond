package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;
import com.zmy.diamond.R;
import com.zmy.diamond.base.MyBaseSwipeBackActivity;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.KeyPicker;
import com.zmy.diamond.utli.MyCityPicker;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.LocationBean;
import com.zmy.diamond.utli.dao.DaoUtlis;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 采集设置页面
 * 采集范围
 * 采集关键字
 * 采集目标数据
 * 采集速度
 * Created by zhangmengyun on 2018/6/26.
 */

public class CollectSettingActivity extends MyBaseSwipeBackActivity {

    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_ttf_data_phone_type)
    TextView tv_ttf_data_phone_type;
    @BindView(R.id.tv_data_phone_type)
    TextView tv_data_phone_type;
    @BindView(R.id.tv_ttf_data_phone_type_r)
    TextView tv_ttf_data_phone_type_r;
    @BindView(R.id.tv_ttf_collect_speed)
    TextView tv_ttf_collect_speed;
    @BindView(R.id.tv_collect_speed)
    TextView tv_collect_speed;
    @BindView(R.id.tv_ttf_collect_speed_r)
    TextView tv_ttf_collect_speed_r;
    @BindView(R.id.tv_ttf_collect_key_r)
    TextView tv_ttf_collect_key_r;
    @BindView(R.id.tv_ttf_collect_city_r)
    TextView tv_ttf_collect_city_r;
    @BindView(R.id.tv_collect_city)
    TextView tv_collect_city;
    @BindView(R.id.tv_collect_key)
    TextView tv_collect_key;
    @BindView(R.id.tv_ttf_collect_city)
    TextView tv_ttf_collect_city;
    @BindView(R.id.tv_ttf_collect_key)
    TextView tv_ttf_collect_key;
    @BindView(R.id.radioGroup_data_phone_type)
    RadioGroup radioGroup_data_phone_type;

    @Override
    public void initUI() {
        setContentView(R.layout.activity_collect_setting);
        super.initUI();
        tv_title.setText(getString(R.string.title_collect_setting));
        tv_back.setTypeface(MyUtlis.getTTF());
        tv_ttf_collect_key.setTypeface(MyUtlis.getTTF());
        tv_ttf_collect_city.setTypeface(MyUtlis.getTTF());
        tv_ttf_collect_city_r.setTypeface(MyUtlis.getTTF());
        tv_ttf_collect_key_r.setTypeface(MyUtlis.getTTF());
        tv_ttf_data_phone_type.setTypeface(MyUtlis.getTTF());
        tv_ttf_data_phone_type_r.setTypeface(MyUtlis.getTTF());
        tv_ttf_collect_speed.setTypeface(MyUtlis.getTTF());
        tv_ttf_collect_speed_r.setTypeface(MyUtlis.getTTF());
    }

    @Override
    public void initData() {
        //1.读取当前设置的目标数据手机类型
        //2.读取当前设置的采集速度
        //3.设置1.2.数据
        tv_data_phone_type.setText(MyUtlis.getCollectDataPhoneTypeStr());
        radioGroup_data_phone_type.check(MyUtlis.getCollectDataPhoneType() == AppConstant.COLLECT_DATA_PHONE_TYPE_ALL ? R.id.rb_all : MyUtlis.getCollectDataPhoneType() == AppConstant.COLLECT_DATA_PHONE_TYPE_PHONE_TEL ? R.id.rb_tel_phone : R.id.rb_phone);
//        ((RadioButton) radioGroup_data_phone_type.getChildAt(MyUtlis.getCollectDataPhoneType())).setChecked(true);
        radioGroup_data_phone_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                rl_data_phone_type();
                MyUtlis.setCollectDataPhoneType(checkedId == R.id.rb_all ? AppConstant.COLLECT_DATA_PHONE_TYPE_ALL : checkedId == R.id.rb_tel_phone ? AppConstant.COLLECT_DATA_PHONE_TYPE_PHONE_TEL : AppConstant.COLLECT_DATA_PHONE_TYPE_PHONE);
                tv_data_phone_type.setText(MyUtlis.getCollectDataPhoneTypeStr());

            }
        });

        String city = MyUtlis.getCollectCity();
        if (!TextUtils.isEmpty(city)) {
            tv_collect_city.setText(city);
        }
        String key = MyUtlis.getCollectKey();
        if (!TextUtils.isEmpty(key)) {
            tv_collect_key.setText(key);
        }
    }


    //是否显示数据电话类型设置选项
    public boolean isShow_data_phone_type;

    @OnClick(R.id.rl_data_phone_type)
    public void rl_data_phone_type() {
        if (!isShow_data_phone_type) {
            radioGroup_data_phone_type.setVisibility(View.VISIBLE);
        } else {
            radioGroup_data_phone_type.setVisibility(View.GONE);
        }
        isShow_data_phone_type = !isShow_data_phone_type;
    }

    @OnClick(R.id.rl_collect_speed)
    public void rl_collect_speed() {
//        MyUtlis.showShort(this, "采集速度");
    }

    @OnClick(R.id.rl_collect_city)
    public void rl_collect_city() {
        selectCity();
    }

    @OnClick(R.id.rl_collect_key)
    public void rl_collect_key() {
        selectKey();
    }

    /**
     * 选择关键词
     */
    private void selectKey() {

        {
//            if (isCollectIng) {
//                MyUtlis.showShort(getActivity(), getString(R.string.hint_collect_ing));
//                return;
//            }
            KeyPicker.getInstance()
                    .setFragmentManager(getSupportFragmentManager())    //此方法必须调用
                    .enableAnimation(true)    //启用动画效果
//                .setAnimationStyle(anim)	//自定义动画
                    .setOnPickListener(new OnPickListener() {
                        @Override
                        public void onPick(int position, City data) {
                            if (null != data && !TextUtils.isEmpty(data.getName())) {
                                tv_collect_key.setText(data.getName());
                                MyUtlis.setCollectKey(data.getName());
                            }

                        }

                        @Override
                        public void onLocate() {

                        }
                    })
                    .show();
        }
    }

    /**
     * 选择采集范围
     */
    public void selectCity() {
//        if (isCollectIng) {
//            MyUtlis.showShort(getActivity(), "正在采集中,请稍后...");
//            return;
//        }

        List<HotCity> hotCities = new ArrayList<>();
        hotCities.add(new HotCity("北京", "北京", "101010100"));
        hotCities.add(new HotCity("上海", "上海", "101020100"));
        hotCities.add(new HotCity("广州", "广东", "101280101"));
        hotCities.add(new HotCity("深圳", "广东", "101280601"));
        hotCities.add(new HotCity("杭州", "浙江", "101210101"));

        LocationBean location = DaoUtlis.getLocation();

        MyCityPicker.getInstance()
                .setFragmentManager(getSupportFragmentManager())    //此方法必须调用
                .enableAnimation(true)    //启用动画效果
//                .setAnimationStyle(anim)	//自定义动画
                .setLocatedCity(location == null ? null : new LocatedCity(null == location ? "" : location.getCity(), null == location ? "" : location.getProvince(), null == location ? "" : location.getCitycode()))  //APP自身已定位的城市，默认为null（定位失败）
                .setHotCities(hotCities)    //指定热门城市
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {

                        if (null != data && !TextUtils.isEmpty(data.getName())) {
                            tv_collect_city.setText(data.getName());
                            MyUtlis.setCollectCity(data.getName());
                        }

                    }

                    @Override
                    public void onLocate() {
                        //开始定位，这里模拟一下定位
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                LocationBean location = DaoUtlis.getLocation();
                                if (null != location) {
                                    //定位完成之后更新数据
//                                    101280601
//                                    LogUtils.e("定位数据=" + location.getCitycode());
                                    MyCityPicker.getInstance()
                                            .locateComplete(new LocatedCity(location.getCity(), location.getProvince(), location.getCitycode()), LocateState.SUCCESS);
                                } else {
                                    MyCityPicker.getInstance()
                                            .locateComplete(new LocatedCity("", "", ""), LocateState.FAILURE);
//                                    LogUtils.e("定位数据=");
                                }
                            }
                        }, 2000);
                    }
                })
                .show();
    }


    public static void start(Context context) {
        Intent intent = new Intent(context, CollectSettingActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.tv_back)
    public void tv_back() {
        ActivityUtils.finishActivity(this, true);
    }
}
