package com.zmy.diamond.utli.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zmy.diamond.MainActivity;
import com.zmy.diamond.R;
import com.zmy.diamond.fragment.MeFragment;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.UserBean;

/**
 * vip pop
 * Created by zhangmengyun on 2018/8/7.
 */

public class VipInfoPop extends PopupWindow {


    public MeFragment meFragment;
    MainActivity mActivity;

    public VipInfoPop(final MeFragment meFragment, final UserBean userBean) {
        super(meFragment.getContext());
        mActivity = (MainActivity) meFragment.getActivity();
        this.meFragment = meFragment;


        LayoutInflater mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View mContentView = mInflater.inflate(R.layout.view_pop_vip_info, null);
        TextView tv_ttf_vip = mContentView.findViewById(R.id.tv_ttf_vip);
        TextView tv_vip_name = mContentView.findViewById(R.id.tv_vip_name);
        TextView tv_vip_time = mContentView.findViewById(R.id.tv_vip_time);
        TextView tv_code = mContentView.findViewById(R.id.tv_code);
        TextView tv_recom_code = mContentView.findViewById(R.id.tv_recom_code);


        tv_ttf_vip.setTypeface(MyUtlis.getTTF());

        tv_vip_name.setText(MyUtlis.getVipName(userBean.getGrade(), "不是会员"));

        tv_vip_time.setText(userBean.getVip_valid_time());


        if (TextUtils.isEmpty(userBean.getCode())) {
            tv_code.setTextColor(ContextCompat.getColor(mActivity, R.color.color_text));
        } else {
            tv_code.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyUtlis.copyText(userBean.getCode());
                    ToastUtils.showShort("推荐码已复制");
                }
            });
        }
        tv_code.setText(MyUtlis.isEmpty(userBean.getCode()));


        if (TextUtils.isEmpty(userBean.getRecomCode())) {
            tv_recom_code.setTextColor(ContextCompat.getColor(mActivity, R.color.color_text));
        } else {
            tv_recom_code.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyUtlis.copyText(userBean.getRecomCode());
                    ToastUtils.showShort("推荐码已复制");
                }
            });
        }
        tv_recom_code.setText(MyUtlis.isEmpty(userBean.getRecomCode()));

        //设置View
        setContentView(mContentView);
        //设置宽与高
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
//        setHeight(ScreenUtils.getScreenHeight() / 2);
        /**
         * 设置进出动画
         */
        setAnimationStyle(R.style.DefaultCityPickerAnimation);
        /**
         * 设置背景只有设置了这个才可以点击外边和BACK消失
         */
        setBackgroundDrawable(new BitmapDrawable());
        /**
         * 设置可以获取集点
         */
        setFocusable(true);
        /**
         * 设置点击外边可以消失
         */
        setOutsideTouchable(true);
        /**
         *设置可以触摸
         */
        setTouchable(true);
        /**
         * 设置点击外部可以消失
         */
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                /**
                 * 判断是不是点击了外部
                 */
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    return true;
                }
                //不是点击外部
                return false;
            }
        });


        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                mActivity.view_bg.setVisibility(View.GONE);
//                MyUtlis.showBrightScreen(mActivity);
            }
        });

    }


    public void show() {
        if (!isShowing()) {
            //屏幕变暗
            KeyboardUtils.hideSoftInput(mActivity);
            mActivity.view_bg.setVisibility(View.VISIBLE);
//            MyUtlis.showDarkScreen(mActivity);
            showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        }


    }

}
