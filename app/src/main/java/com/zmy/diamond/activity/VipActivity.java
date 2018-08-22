package com.zmy.diamond.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zmy.diamond.R;
import com.zmy.diamond.adapter.VipAdapter;
import com.zmy.diamond.base.MyBaseSwipeBackActivity;
import com.zmy.diamond.utli.AppConstant;
import com.zmy.diamond.utli.MessageEvent;
import com.zmy.diamond.utli.MyUtlis;
import com.zmy.diamond.utli.bean.UserBean;
import com.zmy.diamond.utli.dao.DaoUtlis;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * VIP页面
 * Created by zhangmengyun on 2018/6/13.
 */

public class VipActivity extends MyBaseSwipeBackActivity {
    VipAdapter vipAdapter;
    UserBean user;
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    TextView tv_vip;
    TextView tv_vip_time;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    @Override
    public void initUI() {
        setContentView(R.layout.activity_vip);
        super.initUI();
        EventBus.getDefault().register(this);
        tv_back.setTypeface(MyUtlis.getTTF());
        tv_title.setText(getString(R.string.title_vip));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }



    @Override
    public void initData() {


        user = DaoUtlis.getCurrentLoginUser();

        vipAdapter = new VipAdapter(MyUtlis.getVipItemData());
        vipAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        vipAdapter.addHeaderView(getVipHeadView());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(vipAdapter);
            }
        }, 0);

        vipAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                PayActivity.start(VipActivity.this, vipAdapter.getData().get(position).grade);

            }
        });


        setVip();
    }

    //1=黄金会员2=白金会员
    private void setVip() {
        if (null != user) {

            int grade = user.getGrade();

            if (grade == AppConstant.VIP_GRADE_1 || grade == AppConstant.VIP_GRADE_2) {
                tv_vip_time.setVisibility(View.GONE);
                tv_vip.setText("您当前已是" + MyUtlis.getVipName(grade));
                ActivityUtils.finishActivity(this);
            } else {
                tv_vip.setText("您还不是会员");
                tv_vip_time.setText("您可以选择购买会员");
                tv_vip_time.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.eventType == MessageEvent.UPDATE_LOGIN_USER_INFO) {

            LogUtils.e("VIP页面刷新数据");
            user = DaoUtlis.getCurrentLoginUser();
            setVip();
        }
    }

    /**
     * 获取vipheadview
     *
     * @return
     */
    private View getVipHeadView() {
        View headViewVip = View.inflate(VipActivity.this, R.layout.view_head_vip, null);
        tv_vip_time = headViewVip.findViewById(R.id.tv_vip_day);
        TextView tv_ttf_vip = headViewVip.findViewById(R.id.tv_ttf_vip);
        tv_vip = headViewVip.findViewById(R.id.tv_vip_state);

        tv_ttf_vip.setTypeface(MyUtlis.getTTF());
        return headViewVip;

    }

    public static void start(Context context) {
        Intent intent = new Intent(context, VipActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.tv_back)
    public void tv_back() {
        ActivityUtils.finishActivity(this, true);
    }


}
