package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.rd.PageIndicatorView;
import com.zmy.diamond.MainActivity;
import com.zmy.diamond.R;
import com.zmy.diamond.adapter.GuidePagerAdapter;
import com.zmy.diamond.base.MyBaseActivity;
import com.zmy.diamond.utli.AppConstant;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhangmengyun on 2018/8/21.
 */

public class GuideActivity extends MyBaseActivity {

    @BindView(R.id.pageIndicatorView)
    PageIndicatorView mPageIndicatorView;

    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    @Override
    public void initUI() {
        ScreenUtils.setFullScreen(this);
        setContentView(R.layout.activity_guide);
        super.initUI();


    }

    @Override
    public void initData() {


//        MyUtlis.getGuideData();


        final int[] list = {0, 0, 0,};
        mPageIndicatorView.setCount(list.length);
        mPageIndicatorView.setSelection(0);
        GuidePagerAdapter mAdapter = new GuidePagerAdapter(this, list);

        mViewPager.setAdapter(mAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                mPageIndicatorView.setSelection(position);

                if (position == list.length - 1) {
                    //最后一个
//                    mPageIndicatorView.setVisibility(View.GONE);
                    btn_start.setVisibility(View.VISIBLE);
                } else {
                    btn_start.setVisibility(View.GONE);
//                    mPageIndicatorView.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @BindView(R.id.btn_start)
    Button btn_start;

    @OnClick(R.id.btn_start)
    public void btn_start() {
        SPUtils.getInstance().put(AppConstant.SPKey.IS_SHOW_GUIDE_PAGE, true);
        MainActivity.start(this);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, GuideActivity.class);
        context.startActivity(intent);
//        ActivityUtils.startActivity(intent, R.anim.a5, R.anim.a3);

    }
}
