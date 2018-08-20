package com.zmy.diamond.utli.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zmy.diamond.R;
import com.zmy.diamond.activity.BankCardAddActivity;
import com.zmy.diamond.activity.WalletTiXianActivity;
import com.zmy.diamond.adapter.SelectBankCardAdapter;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.BankCardBean;

import java.util.List;

/**
 * 选择银行卡 弹框
 * Created by zhangmengyun on 2018/8/7.
 */

public class SelectBankCardPop extends PopupWindow {


    SelectBankCardAdapter mAdapter;
    RecyclerView recyclerView;
    public WalletTiXianActivity mActivity;
    BaseQuickAdapter.OnItemClickListener listener;

    public SelectBankCardPop(Context context, List<BankCardBean.DataBean> list, BaseQuickAdapter.OnItemClickListener listener) {
        super(context);
        this.mActivity = (WalletTiXianActivity) context;
        this.listener = listener;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View mContentView = mInflater.inflate(R.layout.view_pop_select_bank_card, null);

        TextView tv_ttf_close = (TextView) mContentView.findViewById(R.id.tv_ttf_close);

        recyclerView = (RecyclerView) mContentView.findViewById(R.id.recyclerView);

        tv_ttf_close.setTypeface(MyUtlis.getTTF());
        tv_ttf_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new SelectBankCardAdapter(list);
        mAdapter.setFooterView(getBankCardView());
        mAdapter.setOnItemClickListener(listener);
        recyclerView.setAdapter(mAdapter);
        //设置View
        setContentView(mContentView);
        //设置宽与高
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
//        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setHeight(ScreenUtils.getScreenHeight() / 2);
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

    /**
     * 获取vipheadview
     *
     * @return
     */
    private View getBankCardView() {
        View footerView = View.inflate(mActivity, R.layout.view_bank_card_footer, null);
        TextView tv_ttf_add = footerView.findViewById(R.id.tv_ttf_add);
        tv_ttf_add.setTypeface(MyUtlis.getTTF());
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BankCardAddActivity.start(mActivity);

            }
        });
        return footerView;

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

    /**
     * 设置新数据选择最后一个
     *
     * @param bankData
     */
    public void setNewData(List<BankCardBean.DataBean> bankData) {
        mAdapter.setNewData(bankData);
        recyclerView.scrollToPosition(bankData.size() - 1);
        listener.onItemClick(mAdapter, null, bankData.size() - 1);
    }
}
